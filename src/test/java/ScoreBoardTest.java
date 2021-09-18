
import com.assignment.pojo.HighScore;
import com.assignment.pojo.UserScore;
import com.assignment.util.ScoreBoard;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deepak
 */
public class ScoreBoardTest {
    ScoreBoard scoreBoard;

    @Before
    public void setUp() throws Exception {
        scoreBoard = new ScoreBoard();
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testSaveScore() throws Exception {
        int levelId = 1;
        int userId = 23;
        int score = 320;
        UserScore userScore = new UserScore(userId, score);
        scoreBoard.saveScore(userScore, levelId);
        HighScore highScore = scoreBoard.getHighScore(levelId);
        Assert.assertEquals("HighScore Invalid", "23=320", highScore.toString());
    }

    @Test
    public void testLevelIdWithoutScores() throws Exception {
        HighScore highScore = scoreBoard.getHighScore(555);
        Assert.assertEquals("HighScore Invalid", "", highScore.toString());
    }


    @Test
    public void testUniqueScorePerUser() throws Exception {
        int levelId = 2;
        int userId = 42;
        int score = 500;
        HighScore highScore;
        UserScore userScore1 = new UserScore(userId, score);
        scoreBoard.saveScore(userScore1, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=500", highScore.toString());
        UserScore userScore2 = new UserScore(userId, score + 50);
        scoreBoard.saveScore(userScore2, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=550", highScore.toString());
        UserScore userScore3 = new UserScore(userId, score - 50);
        scoreBoard.saveScore(userScore3, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=550", highScore.toString());
        UserScore userScore4 = new UserScore(userId, score + 100);
        scoreBoard.saveScore(userScore4, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=600", highScore.toString());
    }


    @Test
    public void testSeveralScore() throws Exception {
        int levelId = 3;
        HighScore highScore;
        UserScore userScore1 = new UserScore(42, 500);
        scoreBoard.saveScore(userScore1, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=500", highScore.toString());
        UserScore userScore2 = new UserScore(72, 450);
        scoreBoard.saveScore(userScore2, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=500,72=450", highScore.toString());
        UserScore userScore3 = new UserScore(42, 600);
        scoreBoard.saveScore(userScore3, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "42=600,72=450", highScore.toString());
        UserScore userScore4 = new UserScore(72, 700);
        scoreBoard.saveScore(userScore4, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "72=700,42=600", highScore.toString());
        UserScore userScore5 = new UserScore(10, 650);
        scoreBoard.saveScore(userScore5, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        System.out.println("levelScore = " + highScore);
        Assert.assertEquals("HighScore Invalid", "72=700,10=650,42=600", highScore.toString());
    }


    @Test
    public void testMaxLimitScore() throws Exception {
        int levelId = 4;
        for (int i = 1; i < 16; i++) {
            UserScore userScore1 = new UserScore(i, i);
            scoreBoard.saveScore(userScore1, levelId);
        }
        HighScore highScore = scoreBoard.getHighScore(levelId);
        Assert.assertEquals("HighScore Invalid",
                "15=15,14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2,1=1", highScore.toString());
        int userId = 42;
        UserScore userScore = new UserScore(userId, userId);
        scoreBoard.saveScore(userScore, levelId);
        highScore = scoreBoard.getHighScore(levelId);
        Assert.assertEquals("HighScore Invalid",
                "42=42,15=15,14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2", highScore.toString());
    }


    @Test
    public void testGetLevelScore() throws Exception {

    }
}
