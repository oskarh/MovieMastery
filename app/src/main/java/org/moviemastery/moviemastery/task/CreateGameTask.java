package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import org.moviemastery.moviemastery.callback.CreateGameListener;
import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.service.GameServiceApi;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-29.
 */
public class CreateGameTask extends AsyncTask<Void, Void, Game> {

    private GameServiceApi gameApi;
    private CreateGameListener listener;

    @DebugLog
    public CreateGameTask(GameServiceApi gameApi, CreateGameListener listener) {
        this.gameApi = gameApi;
        this.listener = listener;
    }

    @DebugLog
    @Override
    protected Game doInBackground(Void... voids) {
        return gameApi.createGame();
    }

    @DebugLog
    @Override
    protected void onPostExecute(Game game) {
        listener.onGameReceived(game);
    }
}
