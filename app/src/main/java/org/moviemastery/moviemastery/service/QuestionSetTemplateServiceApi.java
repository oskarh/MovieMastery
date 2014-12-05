package org.moviemastery.moviemastery.service;

import org.moviemastery.moviemastery.repository.QuestionSet;

import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Oskar on 2014-11-30.
 */
public interface QuestionSetTemplateServiceApi {

    public static final String QUESTION_SET_TEMPLATE_SERVICE_PATH = "/template";

    @POST(QUESTION_SET_TEMPLATE_SERVICE_PATH + "/{id}/like")
    public QuestionSet likeQuestionSetTemplate(@Path("id") long id);

    @POST(QUESTION_SET_TEMPLATE_SERVICE_PATH + "/{id}/dislike")
    public QuestionSet dislikeQuestionSetTemplate(@Path("id") long id);
}