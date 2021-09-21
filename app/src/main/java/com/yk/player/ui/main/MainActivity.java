package com.yk.player.ui.main;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yk.player.R;
import com.yk.player.mvp.BaseMvpActivity;
import com.yk.player.utils.MyFragmentPagerAdapter;

import java.util.List;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MyFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initData() {
        setSupportActionBar(toolbar);

        presenter.loadVideoFragment();
    }

    private void bindEvent() {
    }

    @Override
    public void onLoadVideoFragment(List<String> titleList, List<Fragment> fragmentList) {
        if (fragmentPagerAdapter != null) {
            return;
        }

        if (titleList == null || titleList.isEmpty()) {
            return;
        }

        if (fragmentList == null || fragmentList.isEmpty()) {
            return;
        }

        fragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                titleList, fragmentList
        );

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }
}