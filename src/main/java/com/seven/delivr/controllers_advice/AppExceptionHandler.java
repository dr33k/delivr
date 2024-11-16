package com.seven.delivr.controllers_advice;

import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Responder;
import feign.FeignException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@ControllerAdvice
public class AppExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity <Response> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return Responder.badRequest(ex.getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity <Response> handleBadRequest(HttpClientErrorException.BadRequest ex) {
        return Responder.badRequest(ex.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity <Response> handleForbidden(AccessDeniedException ex) {
        return Responder.forbidden(ex.getMessage());
    }
    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity <Response> handleBadCredentials(BadCredentialsException ex) {
        return Responder.unauthorized(ex.getMessage());
    }
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    protected ResponseEntity <Response> handleUnsupportedContent(BadCredentialsException ex) {
        return Responder.unsupportedContentType(ex.getMessage());
    }
    @ExceptionHandler({MessagingException.class, IOException.class})
    protected void handleMessagingException(Exception ex) {
        ex.printStackTrace();
    }
    @ExceptionHandler( InternalAuthenticationServiceException.class)
    protected ResponseEntity <Response> handleFailedLogin(InternalAuthenticationServiceException ex){
        return Responder.notFound();
    }
    @ExceptionHandler(FeignException.class)
    protected ResponseEntity <Response> handleFeignException(FeignException ex){
        ex.printStackTrace();
        return Responder.badRequest(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity <Response> handleResponseStatusException(ResponseStatusException ex) {
        int status = ex.getStatusCode().value();
        switch(status) {
            case 400 -> {return Responder.badRequest(ex.getMessage());}
            case 401 -> {return Responder.unauthorized(ex.getMessage());}
            case 404 -> {return Responder.notFound();}
            case 403 -> {return Responder.forbidden(ex.getMessage());}
            case 409 -> {return Responder.conflict(ex.getMessage());}
            default ->  {return Responder.internalServerError(ex.getMessage());}
        }
    }
    @ExceptionHandler(Exception.class)
    protected ResponseEntity <Response> handleAnyException(Exception ex) {
        ex.printStackTrace();
        return Responder.badRequest(ex.getMessage());
    }
}
