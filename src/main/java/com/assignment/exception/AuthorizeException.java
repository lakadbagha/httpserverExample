/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.exception;

/**
 *
 * @author deepak
 */
public class AuthorizeException extends Exception {

    private static final long serialVersionUID = 5163433612347123492L;


    public static final String AUTHORIZATION_ERROR = "Authorization Error";

    /**
     * Constructs a {@code AuthenticationException} with no detail message.
     */
    public AuthorizeException() {
        super(AUTHORIZATION_ERROR);
    }

    /**
     * Constructs a {@code AuthorizeException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AuthorizeException(String msg) {
        super(msg);
    }
}
