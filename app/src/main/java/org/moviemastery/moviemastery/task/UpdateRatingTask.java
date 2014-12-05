package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import org.moviemastery.moviemastery.service.QuestionSetTemplateServiceApi;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-30.
 */
public class UpdateRatingTask extends AsyncTask<Void, Void, Void> {

    private QuestionSetTemplateServiceApi templateApi;

    private long id;

    private boolean isLiked;

    @DebugLog
    public UpdateRatingTask(QuestionSetTemplateServiceApi templateApi, long id, boolean isLiked) {
        this.templateApi = templateApi;
        this.id = id;
        this.isLiked = isLiked;
    }

    @DebugLog
    @Override
    protected Void doInBackground(Void... voids) {
        if (isLiked) {
            templateApi.likeQuestionSetTemplate(id);
        } else {
            templateApi.dislikeQuestionSetTemplate(id);
        }
        return null;
    }
}
