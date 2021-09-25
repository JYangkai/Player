package com.yk.player.ui.play;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yk.player.R;
import com.yk.player.data.bean.Video;
import com.yk.player.media.VideoPlayer;
import com.yk.player.mvp.BaseMvpActivity;
import com.yk.player.ui.list.ListFragment;
import com.yk.player.ui.view.VideoController;
import com.yk.player.ui.view.VideoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseMvpActivity<IPlayView, PlayPresenter> implements IPlayView {
    private static final String TAG = "PlayActivity";

    private static final String EXTRA_VIDEO_LIST = "extra_video_list";
    private static final String EXTRA_CUR_POS = "extra_cur_pos";

    private FrameLayout flVideoView;
    private VideoView videoView;
    private VideoController videoController;
    private FrameLayout flVideoList;

    private final List<Video> videoList = new ArrayList<>();
    private int curPos = -1;

    public static void startPlayActivity(Context context, List<Video> list, int curPos) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(EXTRA_VIDEO_LIST, (Serializable) list);
        intent.putExtra(EXTRA_CUR_POS, curPos);
        context.startActivity(intent);
    }

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
        flVideoView = findViewById(R.id.flVideoView);
        videoView = findViewById(R.id.videoView);
        videoController = findViewById(R.id.videoController);
        flVideoList = findViewById(R.id.flVideoList);
    }

    private void initData() {
        Object o = getIntent().getSerializableExtra(EXTRA_VIDEO_LIST);
        if (o != null) {
            videoList.addAll((List<Video>) o);
        }

        curPos = getIntent().getIntExtra(EXTRA_CUR_POS, curPos);

        videoController.setTitle(videoList.get(curPos).getName());

        loadFragment();
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flVideoList, ListFragment.newInstance(videoList))
                .commit();
    }

    private void bindEvent() {
        flVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    if (isViewShow(flVideoList)) {
                        hideVideoList();
                    }
                }

                if (isViewShow(videoController)) {
                    hideVideoController();
                } else {
                    showVideoController();
                }
            }
        });

        videoView.setOnPlayListener(new VideoPlayer.OnPlayListener() {
            @Override
            public void onStart(int width, int height, int duration) {
                videoView.setAspectRatio(width, height);
                videoController.setMax(duration);

                if (cachePos != null) {
                    videoView.seekTo(cachePos);
                }
                cachePos = null;
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

        videoController.setOnClickControllerListener(new VideoController.OnClickControllerListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickMore(View view) {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    // 竖屏
                    return;
                }

                // 横屏
                if (isViewShow(flVideoList)) {
                    hideVideoList();
                } else {
                    showVideoList();
                }
            }

            @Override
            public void onClickPause() {
                pausePlay();
            }

            @Override
            public void onClickPlay() {
                continuePlay();
            }

            @Override
            public void onSeekBar(int progress) {
                videoView.seekTo(progress);
            }

            @Override
            public void onFullScreen() {
                fullScreen();
            }

            @Override
            public void onFullScreenExit() {
                fullScreenExit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlay();
        presenter.startPosTimerTask();
        isPause = false;
    }

    private boolean isPause = false;

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
    }

    private void continuePlay() {
        videoView.continuePlay();
    }

    private void pausePlay() {
        videoView.pause();
    }

    private void showVideoController() {
        videoController.setVisibility(View.VISIBLE);
    }

    private void hideVideoController() {
        videoController.setVisibility(View.GONE);
    }

    private void showVideoList() {
        flVideoList.setVisibility(View.VISIBLE);
    }

    private void hideVideoList() {
        flVideoList.setVisibility(View.GONE);
    }

    private boolean isViewShow(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private boolean fullScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // 横屏
            return false;
        }
        // 竖屏
        int width = videoView.getMediaPlay().getVideoWidth();
        int height = videoView.getMediaPlay().getVideoHeight();

        if (width > height) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setViewSize(flVideoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            hideVideoList();
        }
        return true;
    }

    private boolean fullScreenExit() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // 横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        // 竖屏
        int width = videoView.getMediaPlay().getVideoWidth();
        int height = videoView.getMediaPlay().getVideoHeight();

        if (width < height) {
            setViewSize(flVideoView, ViewGroup.LayoutParams.MATCH_PARENT, dip2px(this, 200));
            showVideoList();
            return true;
        }
        return false;
    }

    private boolean isFullScreen() {
        ViewGroup.LayoutParams layoutParams = flVideoView.getLayoutParams();
        return layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void setViewSize(View view, int width, int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void onTimerTask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoController.setProgress(videoView.getCurPos());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen() && fullScreenExit()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 缓存切换横竖屏时的播放时间
     */
    private Integer cachePos;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int curPos = videoView.getCurPos();
        outState.putInt("cur_pos", curPos);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int curPos = savedInstanceState.getInt("cur_pos");
        cachePos = curPos;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
