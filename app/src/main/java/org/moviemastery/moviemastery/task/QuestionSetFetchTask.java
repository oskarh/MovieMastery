package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import org.moviemastery.moviemastery.repository.QuestionSet;
import org.moviemastery.moviemastery.callback.QuestionFetcherListener;
import org.moviemastery.moviemastery.service.QuestionSetApi;

import java.util.Collection;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-27.
 */
public class QuestionSetFetchTask extends AsyncTask<Void, Void, Collection<QuestionSet>> {

    private QuestionSetApi api;
    private QuestionFetcherListener listener;

    public QuestionSetFetchTask(QuestionSetApi api, QuestionFetcherListener listener) {
        this.api = api;
        this.listener = listener;
    }

    @DebugLog
    @Override
    protected Collection<QuestionSet> doInBackground(Void... voids) {
        return api.getQuestionSetList();
    }

    @DebugLog
    @Override
    protected void onPostExecute(Collection<QuestionSet> questionSets) {
        listener.onQuestionsReceived(questionSets);
    }
}
