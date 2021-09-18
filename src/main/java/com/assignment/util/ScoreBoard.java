/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.util;

import com.assignment.pojo.HighScore;
import com.assignment.pojo.UserScore;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author deepak
 */
public class ScoreBoard {
     /**
     * ConcurrentMap<levelId, HighScore> with all the HighScore for all the Levels
     */
    private ConcurrentMap<Integer, HighScore> highScoresMap;
    
    /**
     * Creates a new instance of ScoreManager
     */
    public ScoreBoard() {
        highScoresMap = new ConcurrentHashMap<>();
    }

    /**
     * Method where save a new UserScore in a Level
     * @param userScore new UserScore to save
     * @param levelId level to add the UserScore
     */
    public synchronized void saveScore(final UserScore userScore, final Integer levelId) {
        HighScore highScore = highScoresMap.get(levelId);
        if (highScore == null) {
            highScore = new HighScore();
            highScoresMap.putIfAbsent(levelId, highScore);
        }
        highScore.add(userScore);
    }

    /**
     * Method to get HighScore for a specific level
     * @param levelId level to get the HighScore
     * @return the HighScore for the selected level
     */
    public HighScore getHighScore(final int levelId) {
        if (!highScoresMap.containsKey(levelId)) {
            return new HighScore();
        }
        return highScoresMap.get(levelId);
    }
}
