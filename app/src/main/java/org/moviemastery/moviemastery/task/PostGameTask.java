package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.service.QuizUserApi;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-29.
 */
public class PostGameTask extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "PostGameTask";

    private QuizUserApi userApi;

    private Game game;

    public PostGameTask(QuizUserApi userApi, Game game) {
        this.userApi = userApi;
        this.game = game;
    }

    @DebugLog
    @Override
    protected Void doInBackground(Void... voids) {
        userApi.addGame(game);
        return null;
    }
}
