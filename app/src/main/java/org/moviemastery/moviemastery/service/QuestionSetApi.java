package org.moviemastery.moviemastery.service;

import org.moviemastery.moviemastery.repository.QuestionSet;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by Oskar on 2014-11-21.
 */
public interface QuestionSetApi {

    public static final String QUESTION_SET_SERVICE_PATH = "/questionset";

    @GET(QUESTION_SET_SERVICE_PATH)
    public List<QuestionSet> getQuestionSetList();
}
