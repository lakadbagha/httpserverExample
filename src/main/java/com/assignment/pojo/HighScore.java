/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.pojo;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author deepak
 */
public class HighScore {
    /**
     * Static variable with the value for the max size for the HighScore list.
     */
    private static final int MAX_SCORES = 15;

    /**
     * ConcurrentSkipListSet with all the UserScores for a Level
     */
    private ConcurrentSkipListSet<UserScore> highScores;

    /**
     * Creates a new instance of HighScore
     */
    public HighScore() {
        this.highScores = new ConcurrentSkipListSet<>();
    }

    /**
     * Get the value of highScores
     *
     * @return the value of highScores
     */
    public ConcurrentSkipListSet<UserScore> getHighScores() {
        return highScores;
    }

    /**
     * Set the value of highScores
     *
     * @param highScores new value of highScores
     */
    public void setHighScores(ConcurrentSkipListSet<UserScore> highScores) {
        this.highScores = highScores;
    }

    /**
     * Method to add an new UserScore to the HighScores
     *
     * @param userScore the new UserScore to add
     */
    public void add(UserScore userScore) {
        UserScore oldUserScore = findExistingUserScore(userScore);
        if (oldUserScore != null) {
            if (oldUserScore.getScore() >= userScore.getScore())
                return;
            highScores.remove(oldUserScore);
        }
        highScores.add(userScore);
        if (highScores.size() > MAX_SCORES) {
            highScores.pollLast();
        }
    }

    /**
     * Method to find an UserScore with the same UserId from the selected UserScore
     *
     * @param userScore UserScore to compare if exist another with the same UserId
     * @return the UserScore in the level with se same userId, return null if isn't exist any.
     */
    public UserScore findExistingUserScore(UserScore userScore) {
        for (UserScore u : highScores) {
            if (u.getUserId() == userScore.getUserId()) {
                return u;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return highScores.toString().replace("[", "").replace("]", "").replace(", ", ",");
    }
}
