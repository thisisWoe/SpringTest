package com.ecommercetest.test.controllers.response;

import lombok.Getter;

@Getter
public class CustomExceptionHandler implements ApiResponse {

    private final String statusCodeExplanation;
    private final String message;

    public CustomExceptionHandler(String statusCodeExplanation, String message) {
        this.statusCodeExplanation = statusCodeExplanation;
        this.message = message;
    }

    public static String switchStatusCode(int statusCode) {
        return switch (statusCode) {
            case 201 -> "The resource was successfully created on the server.";
            case 204 -> "The request was successful, but there is no content to return.";
            case 400 -> "The request could not be understood by the server due to malformed syntax or invalid data.";
            case 401 -> "Authentication is required. Please log in and try again.";
            case 403 -> "You do not have permission to access this resource or perform this action.";
            case 404 -> "The requested resource could not be found on the server.";
            case 409 -> "There is a conflict with the current state of the server. For example, a duplicate entry.";
            case 422 ->
                    "The request data is valid, but the server could not process it due to a business logic violation.";
            case 500 -> "An unexpected error occurred on the server. Please try again later or contact support.";
            default -> "The request was processed successfully.";
        };
    }
}
