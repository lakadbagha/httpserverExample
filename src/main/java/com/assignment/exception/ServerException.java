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
public class ServerException extends Exception {

    private static final long serialVersionUID = 2345365634347123492L;

    public static final String GENERIC_ERROR_MESSAGE = "Something wrong happened.";

    /**
     * Constructs a {@code BackEndException} with no detail message.
     */
    public ServerException() {
        super();
    }

    /**
     * Constructs a {@code BackEndException} with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ServerException(String msg) {
        super(msg);
    }

    /**
     * Creates a new instance of ParkingException from an existed Exception
     *
     * @param ex
     * @param code
     * @param description
     */
    public ServerException(Exception ex) {
        super(ex);
    }

}
