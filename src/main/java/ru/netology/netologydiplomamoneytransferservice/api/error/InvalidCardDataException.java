package ru.netology.netologydiplomamoneytransferservice.api.error;

public class InvalidCardDataException extends RuntimeException {

    public InvalidCardDataException(String message) {
        super(message);
    }
}
