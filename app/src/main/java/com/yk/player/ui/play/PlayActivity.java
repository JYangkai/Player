package com.yk.player.ui.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.yk.player.R;
import com.yk.player.data.bean.Video;
import com.yk.player.media.VideoPlayer;
import com.yk.player.mvp.BaseMvpActivity;
import com.yk.player.ui.list.ListFragment;
import com.yk.player.ui.view.VideoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseMvpActivity<IPlayView, PlayPresenter> implements IPlayView {
    private static final String TAG = "PlayActivity";

    private static final String EXTRA_VIDEO_LIST = "extra_video_list";
    private static final String EXTRA_CUR_POS = "extra_cur_pos";

    public static void startPlayActivity(Context context, List<Video> list, int curPos) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(EXTRA_VIDEO_LIST, (Serializable) list);
        intent.putExtra(EXTRA_CUR_POS, curPos);
        context.startActivity(intent);
    }

    private VideoView videoView;
    private AppCompatImageView ivPause;
    private AppCompatImageView ivList;
    private FrameLayout flVideoList;
    private AppCompatSeekBar sbVideo;

    private final List<Video> videoList = new ArrayList<>();
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
        ivPause = findViewById(R.id.ivPause);
        ivList = findViewById(R.id.ivList);
        flVideoList = findViewById(R.id.flVideoList);
        sbVideo = findViewById(R.id.sbVideo);
    }

    private void initData() {
        Object o = getIntent().getSerializableExtra(EXTRA_VIDEO_LIST);
        if (o != null) {
            videoList.addAll((List<Video>) o);
        }

        curPos = getIntent().getIntExtra(EXTRA_CUR_POS, curPos);
    }

    private void bindEvent() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowController()) {
                    hideController();
                } else {
                    showController();
                }
            }
        });

        videoView.setOnPlayListener(new VideoPlayer.OnPlayListener() {
            @Override
            public void onStart(int width, int height, int duration) {
                videoView.setAspectRatio(width, height);
                sbVideo.setMax(duration);
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError() {

            }
        });

        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    pausePlay();
                } else {
                    continuePlay();
                }
            }
        });

        ivList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flVideoList.getVisibility() == View.VISIBLE) {
                    flVideoList.setVisibility(View.GONE);
                } else {
                    flVideoList.setVisibility(View.VISIBLE);
                }
            }
        });

        sbVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(sbVideo.getProgress());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlay();
        loadFragment();
        presenter.startPosTimerTask();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        pausePlay();
        presenter.stopPosTimerTask();
    }

    private void startPlay() {
        if (videoList.isEmpty() || curPos == -1) {
            return;
        }

        if (isPause) {
            continuePlay();
        } else {
            videoView.start(videoList.get(curPos).getPath(), true);
        }

        ivPause.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    private void continuePlay() {
        videoView.continuePlay();
        ivPause.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    private void pausePlay() {
        videoView.pause();
        ivPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flVideoList, ListFragment.newInstance(videoList))
                .commit();
    }

    private void showController() {
        ivPause.setVisibility(View.VISIBLE);
        ivList.setVisibility(View.VISIBLE);
        sbVideo.setVisibility(View.VISIBLE);
    }

    private void hideController() {
        ivPause.setVisibility(View.GONE);
        ivList.setVisibility(View.GONE);
        sbVideo.setVisibility(View.GONE);
    }

    private boolean isShowController() {
        return ivPause.getVisibility() == View.VISIBLE && ivList.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onTimerTask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sbVideo.setProgress(videoView.getCurPos());
            }
        });
    }
}
