package com.buspass.bus_pass_tracker.exception;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundResponseUsesConsistentJsonFields() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(new ResourceNotFoundException("Student not found"));

        assertStandardResponse(response, HttpStatus.NOT_FOUND, "Student not found");
    }

    @Test
    void businessRuleResponseUsesConsistentJsonFields() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleBusinessRule(new BusinessRuleException("Pass already active"));

        assertStandardResponse(response, HttpStatus.BAD_REQUEST, "Pass already active");
    }

    @Test
    void validationResponseIncludesFieldMessagesAndStandardFields() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError(
                "request", "boardingPoint", null, false, null, null,
                "Boarding point is required"));
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(exception);

        assertStandardResponse(response, HttpStatus.BAD_REQUEST, "Request validation failed");
        assertEquals(Map.of("boardingPoint", "Boarding point is required"),
                response.getBody().get("messages"));
    }

    @Test
    void malformedRequestBodyReturnsBadRequest() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "Unreadable body", new MockHttpInputMessage(new byte[0]));

        ResponseEntity<Map<String, Object>> response = handler.handleUnreadableRequest(exception);

        assertStandardResponse(response, HttpStatus.BAD_REQUEST,
                "Malformed or unreadable request body");
    }

    @Test
    void invalidParameterTypeReturnsBadRequest() {
        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException("invalid", Long.class, "id", null, null);

        ResponseEntity<Map<String, Object>> response = handler.handleTypeMismatch(exception);

        assertStandardResponse(response, HttpStatus.BAD_REQUEST, "Invalid value for parameter 'id'");
    }

    @Test
    void internalErrorDoesNotExposeExceptionDetails() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleOther(new IllegalStateException("database credentials leaked"));

        assertStandardResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
    }

    private void assertStandardResponse(ResponseEntity<Map<String, Object>> response,
                                        HttpStatus status, String message) {
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("timestamp"));
        assertEquals(status.value(), response.getBody().get("status"));
        assertEquals(status.getReasonPhrase(), response.getBody().get("error"));
        assertEquals(message, response.getBody().get("message"));
    }
}