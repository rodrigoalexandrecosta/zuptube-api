package br.com.zup.bootcamp.zuptubeapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<Object> handleGeneralException(Exception e) {
        log.error("Uncaught exception, message={}", e.getMessage(), e);
        return status(INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    ResponseEntity<Object> handleDuplicateKeyException(Exception e) {
        return status(CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<Object> handleInvalidRequest(Exception e) {
        return status(BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return status(METHOD_NOT_ALLOWED).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException e) {
        final List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return status(BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn(e.getMessage());
        return status(UNSUPPORTED_MEDIA_TYPE).build();
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return status(NOT_FOUND).build();
    }

}
