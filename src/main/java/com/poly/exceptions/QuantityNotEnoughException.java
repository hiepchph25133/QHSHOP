package com.poly.exceptions;

public class QuantityNotEnoughException extends  RuntimeException{
    public QuantityNotEnoughException(String message) {
        super(message);
    }
}
