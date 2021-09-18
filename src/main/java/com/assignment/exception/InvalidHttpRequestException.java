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
public class InvalidHttpRequestException extends Exception{
     private static final long serialVersionUID = 4354356123447123492L;

    /**
     * Constructs a {@code NotValidHttpException} with no detail message.
     */
    public InvalidHttpRequestException() {
        super();
    }

    /**
     * Constructs a {@code NotValidHttpException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public InvalidHttpRequestException(String msg) {
        super(msg);
    }
}
