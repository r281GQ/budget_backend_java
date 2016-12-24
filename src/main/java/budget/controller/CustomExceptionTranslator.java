package budget.controller;

import budget.accessories.DetailedRestErrorMessage;
import budget.controller.exceptions.FailedLogInException;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.controller.exceptions.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by veghe on 21/11/2016.
 */
@ControllerAdvice
public class CustomExceptionTranslator {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public DetailedRestErrorMessage resourceNotFoundExceptionResolver(ResourceNotFoundException e) {
        return new DetailedRestErrorMessage(HttpStatus.NOT_FOUND, "No resource in the database", e.getResource().toString());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public DetailedRestErrorMessage unsupportedMediaTypeResolver(HttpMediaTypeNotSupportedException e) {
        return new DetailedRestErrorMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "API only consumes JSON content", e.getContentType().toString());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidDataProvidedException.class)
    public DetailedRestErrorMessage invalidDataProvidedExceptionResolver(InvalidDataProvidedException e) {
        return new DetailedRestErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, "Some of the data provided was incorrect or/and was not in line with database content", e.getDetails());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public DetailedRestErrorMessage httpRequestMethodNotSupportedExceptionResolver(HttpRequestMethodNotSupportedException e) {
        return new DetailedRestErrorMessage(HttpStatus.METHOD_NOT_ALLOWED, "Method is not allowed on that url", e.getSupportedHttpMethods().toString());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DetailedRestErrorMessage MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new DetailedRestErrorMessage(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(), "");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnAuthorizedException.class)
    public DetailedRestErrorMessage MethodArgumentNotValidException(UnAuthorizedException e) {
        return new DetailedRestErrorMessage(HttpStatus.FORBIDDEN, "You do not have permission for that resource", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FailedLogInException.class)
    public DetailedRestErrorMessage failedLogIn(FailedLogInException e) {
        return new DetailedRestErrorMessage(HttpStatus.UNAUTHORIZED, "Login failed, check your email.", e.getMessage());
    }
}
