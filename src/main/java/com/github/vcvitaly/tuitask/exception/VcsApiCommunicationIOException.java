package com.github.vcvitaly.tuitask.exception;

/**
 * VcsApiCommunicationIOException.
 *
 * @author Vitalii Chura
 */
public class VcsApiCommunicationIOException extends RuntimeException {

    public VcsApiCommunicationIOException(Throwable cause) {
        super("An exception happened during communication with GItHub API", cause);
    }
}
