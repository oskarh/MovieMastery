package org.moviemastery.moviemastery.repository;

import com.google.common.base.Objects;

import java.util.List;

import hugo.weaving.DebugLog;

public class QuestionSet {

    public static final int NO_ANSWER = -3;
    public static final int TIMED_OUT = -2;
    public static final int SKIPPED_QUESTION = -1;
	
	private long id;
	
	private List<String> alternatives;
	
	private int userAnswer;
	
	private int rating;

    private int correctAnswerIndex;

    private String explanation;

    private Game game;

    private long questionSetTemplateId;

    public QuestionSet() {
        userAnswer = NO_ANSWER;
    }

    public QuestionSet(List<String> alternatives, int correctAnswerIndex, String explanation) {
        this();
        this.alternatives = alternatives;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
    }

    public QuestionSet(List<String> alternatives, int correctAnswerIndex, String explanation, int rating) {
        this(alternatives, correctAnswerIndex, explanation);
        this.rating = rating;
    }

    public QuestionSet(List<String> alternatives, int correctAnswerIndex, String explanation, int rating, int userAnswer) {
        this(alternatives, correctAnswerIndex, explanation, rating);
        this.userAnswer = userAnswer;
    }

    public void decreaseRating() {
        rating--;
    }

    public void increaseRating() {
        rating++;
    }

    @DebugLog
	public List<String> getAlternatives() {
		return alternatives;
	}

    @DebugLog
	public void setAlternatives(List<String> alternatives) {
		this.alternatives = alternatives;
	}

	public int getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(int userAnswer) {
		this.userAnswer = userAnswer;
	}

    @DebugLog
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getQuestionSetTemplateId() {
        return questionSetTemplateId;
    }

    public void setQuestionSetTemplateId(long questionTemplateId) {
        this.questionSetTemplateId = questionTemplateId;
    }

    @Override
	public int hashCode() {
		return Objects.hashCode(alternatives, userAnswer, rating, correctAnswerIndex, explanation, id);
	}
	
	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuestionSet) {
			QuestionSet other = (QuestionSet) obj;
			return Objects.equal(alternatives, other.alternatives)
					&& Objects.equal(userAnswer, other.userAnswer)
					&& Objects.equal(rating, other.rating)
                    && Objects.equal(correctAnswerIndex, other.correctAnswerIndex)
                    && Objects.equal(explanation, other.explanation)
                    && Objects.equal(id, other.id);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
	       .add("alternatives", alternatives)
	       .add("userAnswer", userAnswer)
	       .add("rating", rating)
           .add("correctAnswerIndex", correctAnswerIndex)
           .add("explanation", explanation)
           .add("id", id)
	       .toString();
	}
}
