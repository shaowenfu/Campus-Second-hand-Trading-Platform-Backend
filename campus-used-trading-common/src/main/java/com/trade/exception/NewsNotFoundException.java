package com.trade.exception;

public class NewsNotFoundException extends BaseException {
    public NewsNotFoundException() {}

    public NewsNotFoundException(String message) {
        super(message);
    }
}
