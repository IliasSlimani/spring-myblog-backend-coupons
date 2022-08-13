package com.example.coupons.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jdk.nashorn.internal.parser.Token;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value= ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorMsg resourceNotFoundException(ResourceNotFound resourceNotFound) {
        return new ErrorMsg(resourceNotFound.getMessage(), HttpStatus.NOT_FOUND.toString(), new Date());

    }

    @ExceptionHandler(value= InternalError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorMsg internalError(InternalError internalError) {
            return new ErrorMsg(internalError.getMsg(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), new Date());
    }

    @ExceptionHandler(value = DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg dbException(DataAccessException e) {
        return new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), new Date());
    }

    @ExceptionHandler(value = NonTransientDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg dbException(NonTransientDataAccessException e) {
        return new ErrorMsg("Fields shouldn't be null", HttpStatus.BAD_REQUEST.toString(), new Date());
    }

    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg dbException(InvalidDataAccessResourceUsageException e) {
        return new ErrorMsg("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR.toString(), new Date());
    }

    @ExceptionHandler(value = DuplicateResource.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg duplicateRsr(DuplicateResource duplicateResource) {
        return new ErrorMsg(duplicateResource.getMsg(), HttpStatus.BAD_REQUEST.toString(), new Date());
    }

    @ExceptionHandler(value = EmptyRequestParam.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg EmptyRequiredParam(EmptyRequestParam emptyRequestParam) {
        return new ErrorMsg(emptyRequestParam.getMsg(), HttpStatus.BAD_REQUEST.toString(), new Date());
    }

    @ExceptionHandler(value = TokenExpired.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMsg TokenExpired(TokenExpired tokenExpired) {
        return new ErrorMsg(tokenExpired.getMsg(), HttpStatus.UNAUTHORIZED.toString(), new Date());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorMsg MethodNotAllowed(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        return new ErrorMsg(httpRequestMethodNotSupportedException.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.toString(), new Date());
    }

    @ExceptionHandler(value = JWTVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMsg JWTVerificationExceptionHandler(JWTVerificationException jwtVerificationException) {
        return new ErrorMsg(jwtVerificationException.getMessage(), HttpStatus.FORBIDDEN.toString(), new Date());
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMsg TokenExpiredExceptionHandler(TokenExpiredException tokenExpiredException) {
        return new ErrorMsg(tokenExpiredException.getMessage(), HttpStatus.FORBIDDEN.toString(), new Date());
    }
    @ExceptionHandler(value = MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMsg MissingRequestCookieExceptionHandler(MissingRequestCookieException missingRequestCookieException) {
        return new ErrorMsg(missingRequestCookieException.getMessage(), HttpStatus.BAD_REQUEST.toString(), new Date());
    }

}
