package com.wangzhen.reader.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * BaseTabActivity
 * Created by wangzhen on 2023/4/11
 */
public abstract class BaseTabActivity extends NewBaseActivity {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    protected abstract List<Fragment> createTabFragments();

    protected abstract List<String> createTabTitles();

    protected void setUpTabLayout() {
        mFragmentList = createTabFragments();
        mTitleList = createTabTitles();

        TabFragmentPageAdapter adapter = new TabFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
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
