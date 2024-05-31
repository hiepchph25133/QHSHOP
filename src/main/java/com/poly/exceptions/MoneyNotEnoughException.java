package com.poly.exceptions;

public class MoneyNotEnoughException extends  RuntimeException{
    public MoneyNotEnoughException(String message) {
        super(message);
    }
}
