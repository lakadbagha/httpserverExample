/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.filter;

import com.assignment.HttpRequestHandler;
import com.assignment.exception.ServerException;
import com.assignment.exception.InvalidHttpRequestException;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author deepak
 */
public class MyHttpFilter extends Filter {
      /*
     *  URL Patterns for all the valid requests for each service
     */
    private static final String LOGIN_PATTERN = "/(\\d*)/login";
    private static final String SCORE_PATTERN = "/(\\d*)/score\\?sessionkey=(.*)";
    private static final String HIGH_SCORE_LIST_PATTERN = "/(\\d*)/highscorelist";

    /**
     * Method where all the filter are applied
     *
     * @param httpExchange
     * @param chain
     * @throws IOException
     */
    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        try {
            Map<String, String> parameters;
            //validate the URL from the request
            String uri = httpExchange.getRequestURI().toString();
            System.out.println("uri "+uri);
            if (uri.matches(LOGIN_PATTERN)) {
                parameters = parseLoginParameters(httpExchange);
            } else if (uri.matches(SCORE_PATTERN)) {
                parameters = parseScoreParameters(httpExchange);
            } else if (uri.matches(HIGH_SCORE_LIST_PATTERN)) {
                parameters = parseHighScoreListParameters(httpExchange);
            } else {
                //is an invalid url
                throw new InvalidHttpRequestException("Invalid URL.");
            }
            //Pass the parameter to the BackEndHttpHandler
            httpExchange.setAttribute(HttpRequestHandler.PARAMETER_ATTRIBUTE, parameters);
            chain.doFilter(httpExchange);
        } catch (InvalidHttpRequestException ex) {
            exceptionHandledResponse(ex.getMessage(), httpExchange, HttpURLConnection.HTTP_NOT_FOUND);
        } catch (Exception ex) {
            exceptionHandledResponse(ex.getMessage(), httpExchange, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }

    /**
     * Method where parse the Parameters from the Login Request
     *
     * @param httpExchange
     * @return
     * @throws InvalidHttpRequestException
     */
    private static Map<String, String> parseLoginParameters(HttpExchange httpExchange)
            throws InvalidHttpRequestException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String userId = uri.split("/")[1];
        parameters.put(HttpRequestHandler.REQUEST_PARAMETER, HttpRequestHandler.LOGIN_REQUEST);
        parameters.put(HttpRequestHandler.USER_ID_PARAMETER, userId);
        return parameters;
    }

    /**
     * Method where parse the Parameters from the Score Request
     *
     * @param httpExchange
     * @return
     * @throws InvalidHttpRequestException
     */
    private static Map<String, String> parseScoreParameters(HttpExchange httpExchange)
            throws InvalidHttpRequestException, ServerException {
        validHttpMethod(httpExchange, "POST");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String[] uriStrings = uri.split("/");
        String levelId = uriStrings[1];
        String[] paramsStrings = uriStrings[2].split(HttpRequestHandler.SESSION_KEY_PARAMETER + "=");
        String sessionKey = paramsStrings[1];
        String score;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                score = bufferedReader.readLine();
            } finally {
                bufferedReader.close();
                inputStreamReader.close();
            }
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
        parameters.put(HttpRequestHandler.REQUEST_PARAMETER, HttpRequestHandler.SCORE_REQUEST);
        parameters.put(HttpRequestHandler.LEVEL_ID_PARAMETER, levelId);
        parameters.put(HttpRequestHandler.SESSION_KEY_PARAMETER, sessionKey);
        parameters.put(HttpRequestHandler.SCORE_PARAMETER, score);
        return parameters;
    }

    /**
     * Method where parse the Parameters from the HighScoreList Request
     *
     * @param httpExchange
     * @return
     * @throws InvalidHttpRequestException
     */
    private static Map<String, String> parseHighScoreListParameters(HttpExchange httpExchange)
            throws InvalidHttpRequestException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String levelId = uri.split("/")[1];
        parameters.put(HttpRequestHandler.REQUEST_PARAMETER, HttpRequestHandler.HIGH_SCORE_LIST_REQUEST);
        parameters.put(HttpRequestHandler.LEVEL_ID_PARAMETER, levelId);
        return parameters;
    }

    /**
     * Method to validate if the request use the correct HttpMethod (GET or POST),
     * is not throws an Exception
     *
     * @param httpExchange
     * @param method
     * @throws InvalidHttpRequestException
     */
    private static void validHttpMethod(HttpExchange httpExchange, String method)
            throws InvalidHttpRequestException {
        if (!method.equalsIgnoreCase(httpExchange.getRequestMethod())) {
            throw new InvalidHttpRequestException("Method '" + httpExchange.getRequestMethod() + "' is not supported.");
        }
    }

    /**
     * Method used to handle an Exception into an Http Response
     *
     * @param message
     * @param httpExchange
     * @param statusCode
     * @throws IOException
     */
    private void exceptionHandledResponse(String message, HttpExchange httpExchange, int statusCode)
            throws IOException {
        if (message == null || message.isEmpty())
            message = ServerException.GENERIC_ERROR_MESSAGE;
        httpExchange.sendResponseHeaders(statusCode, message.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    @Override
    public String description() {
        return "Manage the Http Requests.";
    }
}
