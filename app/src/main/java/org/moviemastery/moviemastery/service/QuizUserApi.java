package org.moviemastery.moviemastery.service;

import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.repository.QuizUser;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Oskar on 2014-11-21.
 */
public interface QuizUserApi {

    public static final String TOKEN_PATH = "/oauth/token";

    public static final String USER_SERVICE_PATH = "/user";

    @GET(USER_SERVICE_PATH + "/{id}")
    public QuizUser getQuizUserId(@Path("id") int id);

    @POST(USER_SERVICE_PATH + "/game")
    public QuizUser addGame(@Body Game game);

    @GET(USER_SERVICE_PATH)
    public Collection<Game> getGameList();
}
