package com.yk.player.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.yk.player.R;
import com.yk.player.utils.TimeUtils;

public class VideoController extends FrameLayout {
    private AppCompatImageView ivBack;
    private AppCompatTextView tvTitle;
    private AppCompatImageView ivMore;

    private AppCompatImageView ivPause;
    private AppCompatSeekBar seekBar;
    private AppCompatTextView tvDuration;
    private AppCompatImageView ivFullScreen;

    private boolean isPause = false;
    private boolean isFullScreen = false;

    public VideoController(@NonNull Context context) {
        this(context, null);
    }

    public VideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_video_controller, this);
        findView();
        bindEvent();
    }

    private void findView() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        ivMore = findViewById(R.id.ivMore);

        ivPause = findViewById(R.id.ivPause);
        seekBar = findViewById(R.id.seekBar);
        tvDuration = findViewById(R.id.tvDuration);
        ivFullScreen = findViewById(R.id.ivFullScreen);
    }

    private void bindEvent() {
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickControllerListener != null) {
                    onClickControllerListener.onClickBack();
                }
            }
        });

        ivMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickControllerListener != null) {
                    onClickControllerListener.onClickMore(v);
                }
            }
        });

        ivPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickControllerListener != null) {
                    if (isPause) {
                        // ?????????????????????????????????????????????
                        onClickControllerListener.onClickPlay();
                    } else {
                        // ?????????????????????????????????????????????
                        onClickControllerListener.onClickPause();
                    }
                    setPause(!isPause);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onClickControllerListener != null) {
                    onClickControllerListener.onSeekBar(seekBar.getProgress());
                }
            }
        });

        ivFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickControllerListener != null) {
                    if (isFullScreen) {
                        // ???????????????????????????????????????????????????
                        onClickControllerListener.onFullScreenExit();
                    } else {
                        // ??????????????????????????????????????????????????????
                        onClickControllerListener.onFullScreen();
                    }
                    setFullScreen(!isFullScreen);
                }
            }
        });
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
        if (isPause) {
            ivPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            ivPause.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    }

    public void setMax(int max) {
        seekBar.setMax(max);

        String duration = TimeUtils.getDuration(0) + "/" + TimeUtils.getDuration(max);
        tvDuration.setText(duration);
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);

        String durationStr = tvDuration.getText().toString();
        if (TextUtils.isEmpty(durationStr)) {
            return;
        }
        String duration = TimeUtils.getDuration(progress) + durationStr.substring(durationStr.indexOf('/'));
        tvDuration.setText(duration);
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
        if (isFullScreen) {
            ivFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24);
        } else {
            ivFullScreen.setImageResource(R.drawable.ic_baseline_fullscreen_24);
        }
    }

    private OnClickControllerListener onClickControllerListener;

    public void setOnClickControllerListener(OnClickControllerListener onClickControllerListener) {
        this.onClickControllerListener = onClickControllerListener;
    }

    public interface OnClickControllerListener {
        /**
         * ????????????
         */
        void onClickBack();

        /**
         * ????????????
         */
        void onClickMore(View view);

        /**
         * ????????????
         */
        void onClickPause();

        /**
         * ????????????
         */
        void onClickPlay();

        /**
         * ???????????????
         */
        void onSeekBar(int progress);

        /**
         * ????????????
         */
        void onFullScreen();

        /**
         * ??????????????????
         */
        void onFullScreenExit();
    }
}
