package org.moviemastery.moviemastery.callback;

public interface LoginListener {

    public void onSuccess(String accessToken);

    public void onError(Exception exception);
}
