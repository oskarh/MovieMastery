package org.moviemastery.moviemastery.callback;

import org.moviemastery.moviemastery.repository.Game;

/**
 * Created by Oskar on 2014-11-29.
 */
public interface CreateGameListener {

    public void onGameReceived(Game game);
}
