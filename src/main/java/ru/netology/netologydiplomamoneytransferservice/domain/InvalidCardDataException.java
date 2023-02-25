package ru.netology.netologydiplomamoneytransferservice.domain;

public class InvalidCardDataException extends RuntimeException {

    public InvalidCardDataException(String message) {
        super(message);
    }
}
