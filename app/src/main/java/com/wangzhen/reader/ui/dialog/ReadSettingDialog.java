package com.wangzhen.reader.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.DialogReadSettingBinding;
import com.wangzhen.reader.model.local.ReadSettingManager;
import com.wangzhen.reader.ui.activity.MoreSettingActivity;
import com.wangzhen.reader.ui.activity.ReadActivity;
import com.wangzhen.reader.ui.adapter.PageStyleAdapter;
import com.wangzhen.reader.utils.BrightnessUtils;
import com.wangzhen.reader.utils.Constant;
import com.wangzhen.reader.utils.ScreenUtils;
import com.wangzhen.reader.widget.page.PageLoader;
import com.wangzhen.reader.widget.page.PageMode;
import com.wangzhen.reader.widget.page.PageStyle;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wangzhen on 17-5-18.
 */

public class ReadSettingDialog extends Dialog {
    private DialogReadSettingBinding binding;

    ImageView mIvBrightnessMinus;
    SeekBar mSbBrightness;
    ImageView mIvBrightnessPlus;
    CheckBox mCbBrightnessAuto;
    TextView mTvFontMinus;
    TextView mTvFont;
    TextView mTvFontPlus;
    CheckBox mCbFontDefault;
    RadioGroup mRgPageMode;

    RadioButton mRbSimulation;
    RadioButton mRbCover;
    RadioButton mRbSlide;
    RadioButton mRbScroll;
    RadioButton mRbNone;
    RecyclerView mRvBg;
    TextView mTvMore;
    /************************************/
    private PageStyleAdapter mPageStyleAdapter;
    private final PageLoader mPageLoader;
    private final Activity mActivity;

    private PageMode mPageMode;
    private PageStyle mPageStyle;

    private int mBrightness;
    private int mTextSize;

    private boolean isBrightnessAuto;
    private boolean isTextDefault;


    public ReadSettingDialog(@NonNull Activity activity, PageLoader mPageLoader) {
        super(activity, R.style.ReadSettingDialog);
        mActivity = activity;
        this.mPageLoader = mPageLoader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogReadSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpWindow();
        initData();
        initWidget();
        initEvents();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    private void initData() {
        ReadSettingManager settings = ReadSettingManager.getInstance();

        isBrightnessAuto = settings.isBrightnessAuto();
        mBrightness = settings.getBrightness();
        mTextSize = settings.getTextSize();
        isTextDefault = settings.isDefaultTextSize();
        mPageMode = settings.getPageMode();
        mPageStyle = settings.getPageStyle();
    }

    private void initWidget() {
        mIvBrightnessMinus = binding.readSettingIvBrightnessMinus;
        mSbBrightness = binding.readSettingSbBrightness;
        mIvBrightnessPlus = binding.readSettingIvBrightnessPlus;
        mCbBrightnessAuto = binding.readSettingCbBrightnessAuto;
        mTvFontMinus = binding.readSettingTvFontMinus;
        mTvFont = binding.readSettingTvFont;
        mTvFontPlus = binding.readSettingTvFontPlus;
        mCbFontDefault = binding.readSettingCbFontDefault;
        mRgPageMode = binding.readSettingRgPageMode;
        mRbSimulation = binding.readSettingRbSimulation;
        mRbCover = binding.readSettingRbCover;
        mRbSlide = binding.readSettingRbSlide;
        mRbScroll = binding.readSettingRbScroll;
        mRbNone = binding.readSettingRbNone;
        mRvBg = binding.readSettingRvBg;
        mTvMore = binding.readSettingTvMore;

        mSbBrightness.setProgress(mBrightness);
        mTvFont.setText(String.valueOf(mTextSize));
        mCbBrightnessAuto.setChecked(isBrightnessAuto);
        mCbFontDefault.setChecked(isTextDefault);
        initPageMode();
        setUpAdapter();
    }

    private void setUpAdapter() {
        List<Drawable> drawables = Arrays.asList(getDrawable(R.color.read_bg_1), getDrawable(R.color.read_bg_2), getDrawable(R.color.read_bg_3), getDrawable(R.color.read_bg_4), getDrawable(R.color.read_bg_5));

        mPageStyleAdapter = new PageStyleAdapter();
        mRvBg.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mRvBg.setAdapter(mPageStyleAdapter);
        mPageStyleAdapter.refreshItems(drawables);

        mPageStyleAdapter.setPageStyleChecked(mPageStyle);

    }

    private void initPageMode() {
        switch (mPageMode) {
            case SIMULATION:
                mRbSimulation.setChecked(true);
                break;
            case COVER:
                mRbCover.setChecked(true);
                break;
            case SLIDE:
                mRbSlide.setChecked(true);
                break;
            case NONE:
                mRbNone.setChecked(true);
                break;
            case SCROLL:
                mRbScroll.setChecked(true);
                break;
        }
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    private void initEvents() {
        //亮度调节
        mIvBrightnessMinus.setOnClickListener((v) -> {
            if (mCbBrightnessAuto.isChecked()) {
                mCbBrightnessAuto.setChecked(false);
            }
            int progress = mSbBrightness.getProgress() - 1;
            if (progress < 0) return;
            mSbBrightness.setProgress(progress);
            BrightnessUtils.setBrightness(mActivity, progress);
        });
        mIvBrightnessPlus.setOnClickListener((v) -> {
            if (mCbBrightnessAuto.isChecked()) {
                mCbBrightnessAuto.setChecked(false);
            }
            int progress = mSbBrightness.getProgress() + 1;
            if (progress > mSbBrightness.getMax()) return;
            mSbBrightness.setProgress(progress);
            BrightnessUtils.setBrightness(mActivity, progress);
            //设置进度
            ReadSettingManager.getInstance().setBrightness(progress);
        });

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(mActivity, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //获取屏幕的亮度
                BrightnessUtils.setBrightness(mActivity, BrightnessUtils.getScreenBrightness(mActivity));
            } else {
                //获取进度条的亮度
                BrightnessUtils.setBrightness(mActivity, mSbBrightness.getProgress());
            }
            ReadSettingManager.getInstance().setAutoBrightness(isChecked);
        });

        //字体大小调节
        mTvFontMinus.setOnClickListener((v) -> {
            if (mCbFontDefault.isChecked()) {
                mCbFontDefault.setChecked(false);
            }
            int fontSize = Integer.parseInt(mTvFont.getText().toString()) - 1;
            if (fontSize < 0) return;
            mTvFont.setText(String.valueOf(fontSize));
            mPageLoader.setTextSize(fontSize);
        });

        mTvFontPlus.setOnClickListener((v) -> {
            if (mCbFontDefault.isChecked()) {
                mCbFontDefault.setChecked(false);
            }
            int fontSize = Integer.parseInt(mTvFont.getText().toString()) + 1;
            mTvFont.setText(String.valueOf(fontSize));
            mPageLoader.setTextSize(fontSize);
        });

        mCbFontDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int fontSize = ScreenUtils.dpToPx(Constant.Text.DEFAULT_TEXT_SIZE);
                mTvFont.setText(String.valueOf(fontSize));
                mPageLoader.setTextSize(fontSize);
            }
        });

        //Page Mode 切换
        mRgPageMode.setOnCheckedChangeListener((group, checkedId) -> {
            PageMode pageMode;
            switch (checkedId) {
                case R.id.read_setting_rb_simulation:
                    pageMode = PageMode.SIMULATION;
                    break;
                case R.id.read_setting_rb_cover:
                    pageMode = PageMode.COVER;
                    break;
                case R.id.read_setting_rb_slide:
                    pageMode = PageMode.SLIDE;
                    break;
                case R.id.read_setting_rb_scroll:
                    pageMode = PageMode.SCROLL;
                    break;
                case R.id.read_setting_rb_none:
                    pageMode = PageMode.NONE;
                    break;
                default:
                    pageMode = PageMode.SIMULATION;
                    break;
            }
            mPageLoader.setPageMode(pageMode);
        });

        //背景的点击事件
        mPageStyleAdapter.setOnItemClickListener((view, pos) -> mPageLoader.setPageStyle(PageStyle.values()[pos]));

        //更多设置
        mTvMore.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), MoreSettingActivity.class);
            mActivity.startActivityForResult(intent, ReadActivity.REQUEST_MORE_SETTING);
            //关闭当前设置
            dismiss();
        });
    }

    public boolean isBrightFollowSystem() {
        if (mCbBrightnessAuto == null) {
            return false;
        }
        return mCbBrightnessAuto.isChecked();
    }
}
