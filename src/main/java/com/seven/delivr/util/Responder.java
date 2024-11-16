package com.seven.delivr.util;

import com.seven.delivr.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

public final class Responder {
    public static ResponseEntity<Response> ok(Object records) {
        return ResponseEntity.ok(Response.builder()
                .data(records)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .title("SUCCESS")
                .message("Operation Successful")
                .build());
    }

    public static ResponseEntity<Response> ok(Object records, String token) {
        return ResponseEntity.ok(Response.builder()
                .token(token)
                .data(records)
                .isError(false)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .title("SUCCESS")
                .message("Login Successful")
                .build());
    }

    public static ResponseEntity<Response> badRequest(String message) {
        return ResponseEntity.status(400).body(Response.builder()
                .message(message)
                .isError(true)
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> unauthorized(String message) {
        return ResponseEntity.status(401).body(Response.builder()
                        .title("ERROR")
                .message(message)
                .isError(true)
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<Response> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity<Response> forbidden(String message) {
        return ResponseEntity.status(403).body(Response.builder()
                .title("NOT ALLOWED")
                .message(message)
                .isError(true)
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> conflict(String message) {
        return ResponseEntity.status(409).body(Response.builder()
                .title("CONFLICT")
                .message(message)
                .isError(true)
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> unsupportedContentType(String message) {
        return ResponseEntity.status(405).body(Response.builder()
                .title("ERROR")
                .message(message)
                .isError(true)
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> internalServerError(String message) {
        return ResponseEntity.internalServerError().body(Response.builder()
                .title("ERROR")
                .message(message)
                .isError(true)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public static ResponseEntity<Response> created(Object records, String location) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();
        return ResponseEntity.status(201).location(uri).body(
                Response.builder()
                        .data(records)
                        .isError(false)
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .title("SUCCESS")
                        .message("Created Successfully")
                        .build());
    }

    public static ResponseEntity<Response> created(Object records, String location, String token) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();
        return ResponseEntity.status(201).location(uri).body(
                Response.builder()
                        .token(token)
                        .data(records)
                        .isError(false)
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .title("SUCCESS")
                        .message("Registered Successfully")
                        .build());
    }

    public static ResponseEntity<Response> accepted(String message) {
        return ResponseEntity.status(202).body(
                Response.builder()
                        .title("DONE")
                        .message(message)
                        .isError(false)
                        .status(HttpStatus.ACCEPTED)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
//    public static ResponseEntity<Response> created(Object records, String location, String token) {
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(location).buildAndExpand().toUri();
//        return ResponseEntity.status(201).location(uri).body(
//                Response.builder()
//                .data(records)
//                .isError(false)
//                .status(HttpStatus.CREATED)
//                .timestamp(LocalDateTime.now()).token(token)
//                .build());
//    }
}
