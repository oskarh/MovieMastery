package org.moviemastery.moviemastery.service;

import org.moviemastery.moviemastery.UnsafeHttpsClient;
import org.moviemastery.moviemastery.client.TokenInterceptorBuilder;

import hugo.weaving.DebugLog;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

/**
 * Created by Oskar on 2014-11-24.
 */
public class ServiceBuilder {

    public static final String URL = "https://192.168.0.65:8443";

    private static String accessToken;

    public static GameServiceApi getGameServiceApi() {
        return buildService(accessToken).create(GameServiceApi.class);
    }

    public static QuizUserApi getQuizUserApi() {
        return buildService(accessToken).create(QuizUserApi.class);
    }

    public static QuestionSetApi getQuestionSetApi() {
        return buildService(accessToken).create(QuestionSetApi.class);
    }

    public static QuestionSetTemplateServiceApi getQuestionSetTemplateApi() {
        return buildService(accessToken).create(QuestionSetTemplateServiceApi.class);
    }

    @DebugLog
    private static RestAdapter buildService(String accessToken) {
        return new TokenInterceptorBuilder()
                .setEndpoint(URL)
                .setAccessToken(accessToken)
                .setClient(new ApacheClient(UnsafeHttpsClient.createHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static void setAccessToken(String accessToken) {
        ServiceBuilder.accessToken = accessToken;
    }
}
