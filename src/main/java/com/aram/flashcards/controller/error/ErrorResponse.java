package com.aram.flashcards.controller.error;

import lombok.Data;

@Data
public class ErrorResponse {

    private final String message;

    public static ErrorResponse withMessage(String message) {
        return new ErrorResponse(message);
    }

}