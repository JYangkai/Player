package com.yk.player.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.player.R;
import com.yk.player.data.adapter.VideoAdapter;
import com.yk.player.data.bean.Video;
import com.yk.player.mvp.BaseMvpFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends BaseMvpFragment<IListView, ListPresenter> implements IListView {
    public static final String EXTRA_LIST = "extra_list";

    @Override
    public ListPresenter createPresenter() {
        return new ListPresenter();
    }

    public static ListFragment newInstance(List<Video> list) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LIST, (Serializable) list);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView rvVideo;
    private VideoAdapter videoAdapter;
    private final List<Video> videoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        initData();
        bindEvent();
    }

    private void findView(View view) {
        rvVideo = view.findViewById(R.id.rvVideo);
    }

    private void initData() {
        initExtra();
        initRvVideo();
    }

    private void initExtra() {
        if (getArguments() == null) {
            return;
        }

        Object o = getArguments().getSerializable(EXTRA_LIST);
        if (o == null) {
            return;
        }

        List<Video> list = (List<Video>) o;
        videoList.addAll(list);
    }

    private void initRvVideo() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        videoAdapter = new VideoAdapter(videoList);
        rvVideo.setLayoutManager(linearLayoutManager);
        rvVideo.setAdapter(videoAdapter);
    }

    private void bindEvent() {

    }
}
