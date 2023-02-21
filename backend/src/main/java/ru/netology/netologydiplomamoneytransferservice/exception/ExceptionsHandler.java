package ru.netology.netologydiplomamoneytransferservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.netologydiplomamoneytransferservice.domain.response.Error;

@RestControllerAdvice
public class ExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger("file-logger");

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

        logger.error(errorId + ": " + errorMsg);

        return new ResponseEntity<>(new Error(errorId, errorMsg), httpStatus);
    }
}
