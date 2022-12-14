package com.github.vcvitaly.tuitask.exception.handler;

import com.github.vcvitaly.tuitask.exception.GitHubResourceNotFoundException;
import com.github.vcvitaly.tuitask.exception.VcsApiCommunicationIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ExceptionHandler.
 *
 * @author Vitalii Chura
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(VcsApiCommunicationIOException.class)
    public ResponseEntity<ErrorResponse> handleException(VcsApiCommunicationIOException e) {
        log.error("An exception occurred: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(GitHubResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(GitHubResourceNotFoundException e) {
        log.error("An exception occurred: ", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMediaTypeNotAcceptableException e) {
        log.error("An exception occurred: ", e);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                        String.format("%s, the only supported representation is application/json", e.getMessage())));
    }
}
