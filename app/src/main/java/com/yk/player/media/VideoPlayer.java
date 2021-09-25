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
        Log.d(TAG, "seekTo: " + time);
        mediaPlayer.seekTo(time);
    }

    public void pause() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.pause();
        if (onPlayListener != null) {
            onPlayListener.onPause();
        }
    }

    public void stop() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        if (onPlayListener != null) {
            onPlayListener.onStop();
        }
    }

    public int getCurPos() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == null) {
            return;
        }
        mp.start();
        if (onPlayListener != null) {
            onPlayListener.onStart(mp.getVideoWidth(), mp.getVideoHeight(), mp.getDuration());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
        if (onPlayListener != null) {
            onPlayListener.onComplete();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError: " + extra);
        if (onPlayListener != null) {
            onPlayListener.onError();
        }
        return false;
    }

    private OnPlayListener onPlayListener;

    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    public interface OnPlayListener {
        void onStart(int width, int height, int duration);

        void onPause();

        void onStop();

        void onComplete();

        void onError();
    }
}
