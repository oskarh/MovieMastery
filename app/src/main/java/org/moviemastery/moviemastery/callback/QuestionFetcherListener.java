package org.moviemastery.moviemastery.callback;

import org.moviemastery.moviemastery.repository.QuestionSet;

import java.util.Collection;

/**
 * Created by Oskar on 2014-11-27.
 */
public interface QuestionFetcherListener {

    public void onQuestionsReceived(Collection<QuestionSet> questions);
}
