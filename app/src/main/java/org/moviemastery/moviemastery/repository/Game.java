package org.moviemastery.moviemastery.repository;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static final int GAME_OVER_THRESHOLD = 3;

    private Long id;

    private List<QuestionSet> questions;

    private QuizUser quizUser;

    private int score;

    private int incorrectAnswers;

    private boolean inProgress;

    public Game() {
    }

    public Game(List<QuestionSet> questions) {
        this.questions = questions;
    }

    public Game(List<QuestionSet> questions, int score, int incorrectAnswers, boolean inProgress) {
        this(questions);
        this.score = score;
        this.incorrectAnswers = incorrectAnswers;
        this.inProgress = inProgress;
    }

    public List<QuestionSet> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionSet> questions) {
        this.questions = questions;
    }

    public void addQuestion(QuestionSet question) {
        if(questions == null) {
            questions = new ArrayList<QuestionSet>();
        }
        questions.add(question);
    }

    public int increaseScore() {
        return ++score;
    }

    public int increaseIncorrectAnswer() {
        return ++incorrectAnswers;
    }

    public boolean isGameOver() {
        return incorrectAnswers >= GAME_OVER_THRESHOLD;
    }

    public int getCurrentQuestionNumber() {
        return score + incorrectAnswers + 1;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizUser getQuizUser() {
        return quizUser;
    }

    public void setQuizUser(QuizUser quizUser) {
        this.quizUser = quizUser;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(score, incorrectAnswers, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Game) {
            Game other = (Game) obj;
            return Objects.equal(score, other.score)
                    && Objects.equal(incorrectAnswers, other.incorrectAnswers)
                    && id == id;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("score", score)
                .add("incorrectAnswers", incorrectAnswers)
                .add("inProgress", inProgress)
                .add("questions", questions)
                .add("id", id)
                .toString();
    }
}
