package ru.netology.netologydiplomamoneytransferservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.api.error.Error;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({InvalidConfirmationDataException.class, InvalidCardDataException.class})
    public ResponseEntity<Error> handleInvalidDataException(Exception ex) {
        return sendError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleUnregisteredException(Exception ex) {
        return sendError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Error> sendError(Exception ex, HttpStatus httpStatus) {

        int errorId = Error.idCounter.incrementAndGet();
        String errorMsg = ex.getMessage();

        return new ResponseEntity<>(new Error(errorId, errorMsg), httpStatus);
    }
}
