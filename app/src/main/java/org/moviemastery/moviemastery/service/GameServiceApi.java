package org.moviemastery.moviemastery.service;

import org.moviemastery.moviemastery.repository.Game;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Oskar on 2014-11-21.
 */
public interface GameServiceApi {

    public static final String GAME_SERVICE_PATH = "/game";

    @GET(GAME_SERVICE_PATH + "/{id}")
    public Game getGameById(@Path("id") long id);

    @POST(GAME_SERVICE_PATH + "/sr/{gameId}/{questionSetId}")
    public Void addQuestionSet(@Path("gameId") long gameId, @Path("questionSetId") long questionSetId);

    @POST(GAME_SERVICE_PATH)
    public Game addGame(@Body Game game);

    @GET(GAME_SERVICE_PATH + "/create")
    public Game createGame();
}
