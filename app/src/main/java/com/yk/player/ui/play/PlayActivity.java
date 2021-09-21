package com.yk.player.ui.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.yk.player.R;
import com.yk.player.data.bean.Video;
import com.yk.player.mvp.BaseMvpActivity;
import com.yk.player.ui.list.ListFragment;
import com.yk.player.ui.view.VideoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseMvpActivity<IPlayView, PlayPresenter> implements IPlayView {
    private static final String EXTRA_VIDEO_LIST = "extra_video_list";
    private static final String EXTRA_CUR_POS = "extra_cur_pos";

    public static void startPlayActivity(Context context, List<Video> list, int curPos) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(EXTRA_VIDEO_LIST, (Serializable) list);
        intent.putExtra(EXTRA_CUR_POS, curPos);
        context.startActivity(intent);
    }

    private VideoView videoView;

    private List<Video> videoList = new ArrayList<>();
    private int curPos = -1;

    private boolean isPause = false;

    @Override
    public PlayPresenter createPresenter() {
        return new PlayPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        videoView = findViewById(R.id.videoView);
    }

    private void initData() {
        Object o = getIntent().getSerializableExtra(EXTRA_VIDEO_LIST);
        if (o != null) {
            videoList.addAll((List<Video>) o);
        }

        curPos = getIntent().getIntExtra(EXTRA_CUR_POS, curPos);
    }

    private void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlay();
        loadFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        videoView.pause();
    }

    private void startPlay() {
        if (videoList.isEmpty() || curPos == -1) {
            return;
        }

        if (isPause) {
            videoView.continuePlay();
        } else {
            videoView.start(videoList.get(curPos).getPath(), true);
        }
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flVideoList, ListFragment.newInstance(videoList))
                .commit();
    }
}
