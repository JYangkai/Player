package com.yk.player.media;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class VideoPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "VideoPlayer";

    private MediaPlayer mediaPlayer;

    public void start(String path, Object surface, boolean isLoop) {
        stop();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setLooping(isLoop);
            if (surface instanceof SurfaceHolder) {
                mediaPlayer.setDisplay((SurfaceHolder) surface);
            } else if (surface instanceof Surface) {
                mediaPlayer.setSurface((Surface) surface);
            }
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "start: ", e);
        }
    }

    public void continuePlay() {
        if (mediaPlayer == null || mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.start();
    }

    public void seekTo(int time) {
        if (mediaPlayer == null) {
            return;
        }
        long duration = mediaPlayer.getDuration();
        if (time >= duration) {
            return;
        }
        mediaPlayer.seekTo(time);
    }

    public void pause() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == null) {
            return;
        }
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError: " + extra);
        return false;
    }
}
