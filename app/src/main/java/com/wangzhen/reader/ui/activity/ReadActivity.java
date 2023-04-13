package com.wangzhen.reader.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.ActivityReadBinding;
import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.model.local.ReadSettingManager;
import com.wangzhen.reader.ui.adapter.CategoryAdapter;
import com.wangzhen.reader.ui.base.BaseActivity;
import com.wangzhen.reader.ui.dialog.ReadSettingDialog;
import com.wangzhen.reader.utils.BrightnessUtils;
import com.wangzhen.reader.utils.RxUtils;
import com.wangzhen.reader.utils.StringUtils;
import com.wangzhen.reader.widget.page.PageLoader;
import com.wangzhen.reader.widget.page.PageView;
import com.wangzhen.reader.widget.page.TxtChapter;

import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;

/**
 * ReadActivity
 * Created by wangzhen on 2023/4/11
 */
public class ReadActivity extends BaseActivity {
    private ActivityReadBinding binding;
    private static final String TAG = "ReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";

    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;

    View mBtnBack;
    TextView mBookName;
    DrawerLayout mDlSlide;
    View mTopMenuContainer;
    PageView mPvPage;
    TextView mTvPageTip;
    LinearLayout mLlBottomMenu;
    TextView mTvPreChapter;
    SeekBar mSbChapterProgress;
    TextView mTvNextChapter;
    TextView mTvCategory;
    TextView mTvNightMode;
    TextView mTvSetting;
    RecyclerView mRvCategory;

    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_CATEGORY:
                    mRvCategory.scrollToPosition(mPageLoader.getChapterPos());
                    break;
                case WHAT_CHAPTER:
                    mPageLoader.openChapter();
                    break;
            }
        }
    };
    // 接收电池信息和时间更新的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            // 监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };

    // 亮度调节监听
    // 由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private final ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            // 判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            // 如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setDefaultBrightness(ReadActivity.this);
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    private boolean isNightMode = false;
    private boolean isRegistered = false;

    private String mBookId;

    public static void startActivity(Context context, CollBookBean collBook) {
        context.startActivity(new Intent(context, ReadActivity.class).putExtra(EXTRA_COLL_BOOK, collBook));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fitDarkStatusBar();
        initData();
        initViews();
        initEvents();
        loadBook();
    }

    private void initData() {
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        mBookId = mCollBook.get_id();
    }

    private void initViews() {
        mBtnBack = binding.btnBack;
        mBookName = binding.bookName;
        mDlSlide = binding.readDlSlide;
        mTopMenuContainer = binding.topMenuContainer;
        mPvPage = binding.readPvPage;
        mTvPageTip = binding.readTvPageTip;
        mLlBottomMenu = binding.readLlBottomMenu;
        mTvPreChapter = binding.readTvPreChapter;
        mSbChapterProgress = binding.readSbChapterProgress;
        mTvNextChapter = binding.readTvNextChapter;
        mTvCategory = binding.readTvCategory;
        mTvNightMode = binding.readTvNightMode;
        mTvSetting = binding.readTvSetting;
        mRvCategory = binding.rvCategory;
    }

    private void initEvents() {
        mBtnBack.setOnClickListener(view -> finish());
        mBookName.setText(mCollBook.getTitle());

        //获取页面加载器
        mPageLoader = mPvPage.getPageLoader(mCollBook);
        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        mDlSlide.setFocusableInTouchMode(false);
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        setUpAdapter();

        //夜间模式按钮的状态
        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ireader:keep bright");

        //初始化BottomMenu
        initBottomMenu();

        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {

            @Override
            public void onChapterChange(int pos) {
                mCategoryAdapter.setChapter(pos);
            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {
                for (TxtChapter chapter : chapters) {
                    chapter.setTitle(chapter.getTitle());
                }
                mCategoryAdapter.setData(chapters);
            }

            @Override
            public void onPageCountChange(int count) {
                mSbChapterProgress.setMax(Math.max(0, count - 1));
                mSbChapterProgress.setProgress(0);
                // 如果处于错误状态，那么就冻结使用
                mSbChapterProgress.setEnabled(mPageLoader.getPageStatus() != PageLoader.STATUS_LOADING && mPageLoader.getPageStatus() != PageLoader.STATUS_ERROR);
            }

            @Override
            public void onPageChange(int pos) {
                mSbChapterProgress.post(() -> mSbChapterProgress.setProgress(pos));
            }
        });

        mSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mLlBottomMenu.getVisibility() == VISIBLE) {
                    //显示标题
                    mTvPageTip.setText(String.format(Locale.getDefault(), "%d/%d", progress + 1, mSbChapterProgress.getMax() + 1));
                    mTvPageTip.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进行切换
                int pagePos = mSbChapterProgress.getProgress();
                if (pagePos != mPageLoader.getPagePos()) {
                    mPageLoader.skipToPage(pagePos);
                }
                //隐藏提示
                mTvPageTip.setVisibility(GONE);
            }
        });

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public void prePage() {
            }

            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }
        });

        mTvCategory.setOnClickListener((v) -> {
            //移动到指定位置
            if (mCategoryAdapter.getDatas().size() > 0) {
                mRvCategory.scrollToPosition(mPageLoader.getChapterPos());
            }
            //切换菜单
            toggleMenu(true);
            //打开侧滑动栏
            mDlSlide.openDrawer(GravityCompat.START);
        });
        mTvSetting.setOnClickListener((v) -> {
            toggleMenu(false);
            mSettingDialog.show();
        });

        mTvPreChapter.setOnClickListener((v) -> {
            if (mPageLoader.skipPreChapter()) {
                mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
            }
        });

        mTvNextChapter.setOnClickListener((v) -> {
            if (mPageLoader.skipNextChapter()) {
                mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
            }
        });

        mTvNightMode.setOnClickListener((v) -> {
            isNightMode = !isNightMode;
            mPageLoader.setNightMode(isNightMode);
            toggleNightMode();
        });

        mSettingDialog.setOnDismissListener(dialog -> fitDarkStatusBar());
    }

    private void initBottomMenu() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
        params.bottomMargin = 0;
        mLlBottomMenu.setLayoutParams(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: " + mTopMenuContainer.getMeasuredHeight());
    }

    private void toggleNightMode() {
        if (isNightMode) {
            mTvNightMode.setText(StringUtils.getString(R.string.mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_morning);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            mTvNightMode.setText(StringUtils.getString(R.string.mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_night);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void setUpAdapter() {
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        mCategoryAdapter = new CategoryAdapter(null);
        mCategoryAdapter.setOnClickCallback((view, position) -> {
            mDlSlide.closeDrawer(GravityCompat.START);
            mPageLoader.skipToChapter(position);
        });
        mRvCategory.setAdapter(mCategoryAdapter);
    }

    // 注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (!isRegistered) {
                final ContentResolver cr = getContentResolver();
                cr.unregisterContentObserver(mBrightObserver);
                cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                isRegistered = true;
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (isRegistered) {
                getContentResolver().unregisterContentObserver(mBrightObserver);
                isRegistered = false;
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        fitDarkStatusBar();
        if (mTopMenuContainer.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mTopMenuContainer.getVisibility() == View.VISIBLE) {
            //关闭
            mTopMenuContainer.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            mTopMenuContainer.setVisibility(GONE);
            mLlBottomMenu.setVisibility(GONE);
            mTvPageTip.setVisibility(GONE);

            if (hideStatusBar) {
                fitDarkStatusBar();
            }
        } else {
            mTopMenuContainer.setVisibility(View.VISIBLE);
            mLlBottomMenu.setVisibility(View.VISIBLE);
            mTopMenuContainer.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            fitLightStatusBar();
        }
    }

    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    private void loadBook() {
        Disposable disposable = BookRepository.getInstance().getBookChaptersInRx(mBookId).compose(RxUtils::toSimpleSingle).subscribe((bookChapterBeen, throwable) -> {
            // 设置 CollBook
            mPageLoader.getCollBook().setBookChapters(bookChapterBeen);
            // 刷新章节列表
            mPageLoader.refreshChapterList();
            Log.e(TAG, throwable.getMessage());
        });
        addDisposable(disposable);
    }

    @Override
    public void onBackPressed() {
        if (mTopMenuContainer.getVisibility() == View.VISIBLE) {
            toggleMenu(true);
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
            mDlSlide.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mPageLoader.saveRecord();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);

        mPageLoader.closeBook();
        mPageLoader = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager.getInstance().isVolumeTurnPage();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToPrePage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToNextPage();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
