package org.moviemastery.moviemastery.callback;

/**
 * Created by Oskar on 2014-12-18.
 */
public interface CountDownListener {

    public void onTick(int percentLeft);

    public void onFinished();
}
