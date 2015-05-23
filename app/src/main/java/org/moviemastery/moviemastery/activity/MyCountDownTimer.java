package org.moviemastery.moviemastery.activity;

import android.os.CountDownTimer;
import android.util.Log;

import org.moviemastery.moviemastery.callback.CountDownListener;

import hugo.weaving.DebugLog;

public class MyCountDownTimer {

    public static final int DEFAULT_TIME = 6000;
    public static final int DEFAULT_INTERVAL = 20;

    public CountDownListener listener;

    private long lifetimeMillis;

    private int interval;

    CountDownTimer timer;

    public MyCountDownTimer(CountDownListener listener) {
        this(listener, DEFAULT_TIME, DEFAULT_INTERVAL);
    }

    public MyCountDownTimer(CountDownListener listener, long millisInFuture, int countDownInterval) {
        timer = new Timer(millisInFuture, countDownInterval);
        this.listener = listener;
        lifetimeMillis = millisInFuture;
        interval = countDownInterval;
    }

    public void reset() {
        timer.cancel();
        timer = new Timer(lifetimeMillis, interval);
        timer.start();
    }

    public void cancel() {
        timer.cancel();
    }

    class Timer extends CountDownTimer {

        public Timer(long millisInFuture, int countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @DebugLog
        @Override
        public void onTick(long millisUntilFinished) {
            MyCountDownTimer.this.listener.onTick((int) (100 * millisUntilFinished / lifetimeMillis));
        }

        @Override
        public void onFinish() {
            Log.w("TAG", "Finished");
            MyCountDownTimer.this.listener.onFinished();
        }
    }
}