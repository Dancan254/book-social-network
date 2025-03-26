package com.mongs.book_social_network.exceptions;

public class OperationNotPermitted extends RuntimeException {
    public OperationNotPermitted(String s) {
        super(s);
    }
}
