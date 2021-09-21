package com.yk.player.ui.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yk.player.media.VideoPlayer;

public class VideoView extends TextureView implements TextureView.SurfaceTextureListener, VideoPlayer.OnPlayListener {
    private static final String TAG = "VideoView";

    private final VideoPlayer videoPlayer = new VideoPlayer();

    private String path;
    private boolean isLoop;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        videoPlayer.setOnPlayListener(this);
    }

    public void start(String path, boolean isLoop) {
        this.path = path;
        this.isLoop = isLoop;
        if (isAvailable()) {
            videoPlayer.start(path, new Surface(getSurfaceTexture()), isLoop);
        } else {
            setSurfaceTextureListener(this);
        }
    }

    public void continuePlay() {
        videoPlayer.continuePlay();
    }

    public void seekTo(int time) {
        videoPlayer.seekTo(time);
    }

    public void pause() {
        videoPlayer.pause();
    }

    public void stop() {
        videoPlayer.stop();
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        videoPlayer.start(path, new Surface(surface), isLoop);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    public void onStart(int width, int height) {
        setAspectRatio(width, height);
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
}
