package org.moviemastery.moviemastery.repository;

import com.google.common.base.Objects;

import java.util.List;

public class QuizUser {
	
	private long id;
	
	private String username;
	
	private String password;

    private List<Game> games;
	
	public QuizUser() {
	}
	
	public QuizUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(username, password, id);
	}
	
	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuizUser) {
			QuizUser other = (QuizUser) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(username, other.username)
					&& Objects.equal(password, other.password)
                    && Objects.equal(id, other.id);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
	       .add("username", username)
	       .add("password", password)
           .add("games", games)
           .add("id", id)
	       .toString();
	}
}
