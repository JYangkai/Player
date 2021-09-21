package com.yk.player.ui.main;

import androidx.fragment.app.Fragment;

import com.yk.player.mvp.BaseMvpView;

import java.util.List;

public interface IMainView extends BaseMvpView {
    void onLoadVideoFragment(List<String> titleList, List<Fragment> fragmentList);
}
