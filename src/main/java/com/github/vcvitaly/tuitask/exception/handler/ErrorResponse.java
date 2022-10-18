package com.github.vcvitaly.tuitask.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ErrorResponse.
 *
 * @author Vitalii Chura
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
}
