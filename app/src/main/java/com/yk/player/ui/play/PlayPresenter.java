package com.yk.player.ui.play;

import com.yk.player.mvp.BaseMvpPresenter;

import java.util.Timer;
import java.util.TimerTask;

public class PlayPresenter extends BaseMvpPresenter<IPlayView> {

    private Timer timer;

    public void startPosTimerTask() {
        stopPosTimerTask();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getView() != null) {
                    getView().onTimerTask();
                }
            }
        }, 0, 1000);
    }

    public void stopPosTimerTask() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer = null;
    }

}
