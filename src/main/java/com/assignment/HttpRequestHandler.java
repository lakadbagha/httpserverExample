package com.assignment;

import com.assignment.exception.AuthorizeException;
import com.assignment.exception.InvalidHttpRequestException;
import com.assignment.exception.ServerException;
import com.assignment.pojo.HighScore;
import com.assignment.pojo.UserScore;
import com.assignment.util.ScoreBoard;
import com.assignment.util.SessionManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Deepak
 *
 */
@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

    /*
     *  Key for the Parameters
     */
    public static final String PARAMETER_ATTRIBUTE = "parameters";
    public static final String REQUEST_PARAMETER = "request";
    public static final String LEVEL_ID_PARAMETER = "levelid";
    public static final String SESSION_KEY_PARAMETER = "sessionkey";
    public static final String SCORE_PARAMETER = "score";
    public static final String USER_ID_PARAMETER = "userid";

    /*
     *  Request for the different URI
     */
    public static final String LOGIN_REQUEST = "login";
    public static final String SCORE_REQUEST = "score";
    public static final String HIGH_SCORE_LIST_REQUEST = "highscorelist";

    /*
     *  Http Content type constants
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TEXT = "text/plain";

    private int HTTP_OK_STATUS = 200;

    private final Map<Integer, String> session = new HashMap<>();
    private final SessionManager sessionManager = new SessionManager(5);
    private ScoreBoard scoreBoard = new ScoreBoard();

    @Override
    public void handle(HttpExchange t) throws IOException {
        System.out.println("handling called" + t.getRequestMethod());
        //Create a response form the request
        String response = null;
        if ("GET".equals(t.getRequestMethod())) {
            try {
                response = handleGetRequest(t);
            } catch (InvalidHttpRequestException ex) {
                response = ex.getMessage();
            }
        } else if ("POST".equals(t.getRequestMethod())) {
            response = handlePostRequest(t);
        }
        //Set the response header status and length
        t.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
        t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
        try ( //Write the response string
                OutputStream os = t.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String generateSessionId(Integer userId) {

        String sessionId;
        if (session.get(userId) == null) {
            sessionId = sessionManager.addSession(userId);
            session.put(userId, sessionId);
        } else {
            sessionId = session.get(userId);
        }
        return sessionId;
    }

    private String handleGetRequest(HttpExchange t) throws InvalidHttpRequestException {
        Map<String, String> parameters = (Map<String, String>) t.getAttribute(PARAMETER_ATTRIBUTE);
        URI uri = t.getRequestURI();
        String path = uri.getPath();
        if (path.endsWith("login")) {
            System.out.println("path " + path);
            String userId = path.split("/")[1];
            System.out.println("userId " + userId);
            if (userId != null) {
                return generateSessionId(Integer.parseInt(userId));
            }
        } else {
            final int levelId = Integer.parseInt(parameters.get(LEVEL_ID_PARAMETER));
            HighScore highScore = scoreBoard.getHighScore(levelId);
            return highScore.toString();
        }
        throw new InvalidHttpRequestException("Invalid URL.");
    }

    private String handlePostRequest(HttpExchange t) {
        Map<String, String> parameters = (Map<String, String>) t.getAttribute(PARAMETER_ATTRIBUTE);
        final String sessionKey = parameters.get(SESSION_KEY_PARAMETER);
        final int levelId = Integer.parseInt(parameters.get(LEVEL_ID_PARAMETER));
        System.out.println("parameters.get(SCORE_PARAMETER) " + parameters.get(SCORE_PARAMETER));
        final int score = Integer.parseInt(parameters.get(SCORE_PARAMETER));
        if (sessionManager.isSessionValid(sessionKey) && SessionManager.userId.get(sessionKey) != null) {
            UserScore userScore = new UserScore(SessionManager.userId.get(sessionKey), score);
            scoreBoard.saveScore(userScore, levelId);
        }
        return null;
    }
}
