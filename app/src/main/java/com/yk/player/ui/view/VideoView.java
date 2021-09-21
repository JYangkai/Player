package com.yk.player.ui.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yk.player.media.VideoPlayer;

public class VideoView extends TextureView implements TextureView.SurfaceTextureListener {
    private final VideoPlayer videoPlayer = new VideoPlayer();

    private String path;
    private boolean isLoop;

    public VideoView(@NonNull Context context) {
        super(context);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
