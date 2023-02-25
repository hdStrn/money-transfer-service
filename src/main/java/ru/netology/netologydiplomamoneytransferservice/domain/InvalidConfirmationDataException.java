package ru.netology.netologydiplomamoneytransferservice.domain;

public class InvalidConfirmationDataException extends RuntimeException {

    public InvalidConfirmationDataException(String message) {
        super(message);
    }
}
