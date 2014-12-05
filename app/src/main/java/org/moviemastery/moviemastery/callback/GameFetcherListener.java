package org.moviemastery.moviemastery.callback;

import org.moviemastery.moviemastery.repository.Game;

import java.util.Collection;

/**
 * Created by Oskar on 2014-11-29.
 */
public interface GameFetcherListener {

    public void onGamesReceived(Collection<Game> games);
}
