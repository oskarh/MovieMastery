package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import org.moviemastery.moviemastery.callback.GameFetcherListener;
import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.service.QuizUserApi;

import java.util.Collection;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-29.
 */
public class GameFetcherTask extends AsyncTask<Void, Void, Collection<Game>> {

    private QuizUserApi userApi;
    private GameFetcherListener listener;

    @DebugLog
    public GameFetcherTask(QuizUserApi userApi, GameFetcherListener listener) {
        this.userApi = userApi;
        this.listener = listener;
    }

    @Override
    protected Collection<Game> doInBackground(Void... voids) {
        return userApi.getGameList();
    }

    @DebugLog
    @Override
    protected void onPostExecute(Collection<Game> games) {
        listener.onGamesReceived(games);
    }
}
