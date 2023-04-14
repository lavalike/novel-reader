package com.wangzhen.reader.ui.activity;

import static com.wangzhen.reader.ui.fragment.BaseFileFragment.OnFileCheckedCallback;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wangzhen.commons.toolbar.impl.Toolbar;
import com.wangzhen.reader.R;
import com.wangzhen.reader.base.toolbar.AppCommonToolbar;
import com.wangzhen.reader.databinding.ActivityFileSystemBinding;
import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.base.BookRepository;
import com.wangzhen.reader.ui.base.BaseActivity;
import com.wangzhen.reader.ui.fragment.BaseFileFragment;
import com.wangzhen.reader.ui.fragment.FileCategoryFragment;
import com.wangzhen.reader.ui.fragment.LocalBookFragment;
import com.wangzhen.reader.utils.AppConfig;
import com.wangzhen.reader.utils.MD5Utils;
import com.wangzhen.reader.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FileSystemActivity
 * Created by wangzhen on 2023/4/11
 */
public class FileSystemActivity extends BaseActivity {
    private ActivityFileSystemBinding binding;
    private CheckBox mCbSelectAll;
    private Button mBtnDelete;
    private Button mBtnAddBook;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    private LocalBookFragment mLocalFragment;
    private FileCategoryFragment mCategoryFragment;
    private BaseFileFragment mCurFragment;

    private final OnFileCheckedCallback mListener = new OnFileCheckedCallback() {
        @Override
        public void onItemCheckedChange(boolean isChecked) {
            changeMenuStatus();
        }

        @Override
        public void onCategoryChanged() {
            //状态归零
            mCurFragment.setCheckedAll(false);
            //改变菜单
            changeMenuStatus();
            //改变是否能够全选
            changeCheckedAllStatus();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileSystemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        initEvents();
        mCurFragment = mLocalFragment;
    }

    private void initViews() {
        tabLayout = binding.tabIndicator;
        viewPager = binding.tabVp;
        mCbSelectAll = binding.fileSystemCbSelectedAll;
        mBtnDelete = binding.fileSystemBtnDelete;
        mBtnAddBook = binding.fileSystemBtnAddBook;
    }

    @Nullable
    @Override
    public Toolbar createToolbar() {
        return new AppCommonToolbar(this, "本机导入");
    }

    private void setUpTabLayout() {
        mFragmentList = createTabFragments();
        mTitleList = createTabTitles();

        TabFragmentPageAdapter adapter = new TabFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected List<Fragment> createTabFragments() {
        mLocalFragment = new LocalBookFragment();
        mCategoryFragment = new FileCategoryFragment();
        return Arrays.asList(mLocalFragment, mCategoryFragment);
    }

    protected List<String> createTabTitles() {
        return Arrays.asList("智能导入", "手机目录");
    }

    private void initEvents() {
        setUpTabLayout();

        mCbSelectAll.setOnClickListener((view) -> {
            //设置全选状态
            boolean isChecked = mCbSelectAll.isChecked();
            mCurFragment.setCheckedAll(isChecked);
            //改变菜单状态
            changeMenuStatus();
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mCurFragment = mLocalFragment;
                } else {
                    mCurFragment = mCategoryFragment;
                }
                //改变菜单状态
                changeMenuStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBtnAddBook.setOnClickListener((v) -> {
            //获取选中的文件
            List<File> files = mCurFragment.getCheckedFiles();
            //转换成CollBook,并存储
            List<CollBookBean> collBooks = convertCollBook(files);
            BookRepository.getInstance().saveCollBooks(collBooks);
            //设置HashMap为false
            mCurFragment.setCheckedAll(false);
            //改变菜单状态
            changeMenuStatus();
            //改变是否可以全选
            changeCheckedAllStatus();
            //提示加入书架成功
            Toast.makeText(this, getResources().getString(R.string.file_add_succeed, collBooks.size()), Toast.LENGTH_SHORT).show();
        });

        mBtnDelete.setOnClickListener((v) -> {
            //弹出，确定删除文件吗。
            new AlertDialog.Builder(this).setTitle("删除文件").setMessage("确定删除文件吗?").setPositiveButton(getResources().getString(R.string.common_sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //删除选中的文件
                    mCurFragment.deleteCheckedFiles();
                    //提示删除文件成功
                    Toast.makeText(FileSystemActivity.this, "删除文件成功", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton(getResources().getString(R.string.common_cancel), null).show();
        });

        mLocalFragment.setOnFileCheckedCallback(mListener);
        mCategoryFragment.setOnFileCheckedCallback(mListener);
    }

    /**
     * 将文件转换成CollBook
     *
     * @param files:需要加载的文件列表
     * @return list of CollBookBean
     */
    private List<CollBookBean> convertCollBook(List<File> files) {
        List<CollBookBean> collBooks = new ArrayList<>(files.size());
        for (File file : files) {
            //判断文件是否存在
            if (!file.exists()) continue;

            CollBookBean collBook = new CollBookBean();
            collBook.set_id(MD5Utils.strToMd5By16(file.getAbsolutePath()));
            collBook.setTitle(file.getName().replace(".txt", ""));
            collBook.setAuthor("");
            collBook.setShortIntro("无");
            collBook.setCover(file.getAbsolutePath());
            collBook.setLocal(true);
            collBook.setLastChapter("开始阅读");
            collBook.setUpdated(StringUtils.dateConvert(file.lastModified(), AppConfig.Format.FORMAT_BOOK_DATE));
            collBook.setLastRead(StringUtils.dateConvert(System.currentTimeMillis(), AppConfig.Format.FORMAT_BOOK_DATE));
            collBooks.add(collBook);
        }
        return collBooks;
    }

    /**
     * 改变底部选择栏的状态
     */
    private void changeMenuStatus() {

        //点击、删除状态的设置
        if (mCurFragment.getCheckedCount() == 0) {
            mBtnAddBook.setText(getString(R.string.file_add_shelf));
            //设置某些按钮的是否可点击
            setMenuClickable(false);

            if (mCbSelectAll.isChecked()) {
                mCurFragment.setChecked(false);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }

        } else {
            mBtnAddBook.setText(getString(R.string.file_add_shelves, mCurFragment.getCheckedCount()));
            setMenuClickable(true);

            //全选状态的设置

            //如果选中的全部的数据，则判断为全选
            if (mCurFragment.getCheckedCount() == mCurFragment.getCheckableCount()) {
                //设置为全选
                mCurFragment.setChecked(true);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }
            //如果曾今是全选则替换
            else if (mCurFragment.isCheckedAll()) {
                mCurFragment.setChecked(false);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }
        }

        //重置全选的文字
        if (mCurFragment.isCheckedAll()) {
            mCbSelectAll.setText("取消");
        } else {
            mCbSelectAll.setText("全选");
        }

    }

    private void setMenuClickable(boolean isClickable) {

        //设置是否可删除
        mBtnDelete.setEnabled(isClickable);
        mBtnDelete.setClickable(isClickable);

        //设置是否可添加书籍
        mBtnAddBook.setEnabled(isClickable);
        mBtnAddBook.setClickable(isClickable);
    }

    /**
     * 改变全选按钮的状态
     */
    private void changeCheckedAllStatus() {
        //获取可选择的文件数量
        int count = mCurFragment.getCheckableCount();

        //设置是否能够全选
        if (count > 0) {
            mCbSelectAll.setClickable(true);
            mCbSelectAll.setEnabled(true);
        } else {
            mCbSelectAll.setClickable(false);
            mCbSelectAll.setEnabled(false);
        }
    }

    class TabFragmentPageAdapter extends FragmentPagerAdapter {

        public TabFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
