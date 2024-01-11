package com.example.beimplementation.Exceptions;

import lombok.Getter;

@Getter
public class BadQueryException extends Exception {
    private final String resultCode;
    private final String resultMsg;

    public BadQueryException(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
