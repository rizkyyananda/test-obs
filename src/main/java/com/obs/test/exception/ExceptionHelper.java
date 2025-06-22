package com.obs.test.exception;

import com.obs.test.dto.ResponseObject;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHelper {
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleException(Exception ex) {
        ResponseObject<Object> responseObject = new ResponseObject<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = { EntityNotFoundException.class })
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex) {
        ResponseObject<Object> responseObject = new ResponseObject<>(
                HttpStatus.NOT_FOUND,
                null,
                ex.getMessage()
        );
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        ResponseObject<Object> responseObject = new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ResponseObject<Object> responseObject = new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                String.join(", ", errors), // gabungkan error agar lebih rapi
                null
        );

        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ResponseObject<Object> responseObject = new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request: " + ex.getLocalizedMessage(),
                null
        );

        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());

        ResponseObject<List<String>> responseObject = new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

}
