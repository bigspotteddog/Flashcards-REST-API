package com.aram.flashcards.controller.error;

import com.aram.flashcards.controller.ResponseHandler;
import com.aram.flashcards.service.exception.ConflictException;
import com.aram.flashcards.service.exception.BadRequestException;
import com.aram.flashcards.service.exception.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ApiExceptionHandler implements ResponseHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    ErrorResponse handle(NotFoundException exception) {
        return responseFrom(exception);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    ErrorResponse handle(BadRequestException exception) {
        return responseFrom(exception);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    @ResponseBody
    ErrorResponse handle(ConflictException exception) {
        return responseFrom(exception);
    }

    private ErrorResponse responseFrom(RuntimeException exception) {
        return ErrorResponse.withMessage(exception.getMessage());
    }

}
