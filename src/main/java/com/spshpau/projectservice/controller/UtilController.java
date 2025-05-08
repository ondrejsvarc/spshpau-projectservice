package com.spshpau.projectservice.controller;


import org.springframework.http.ResponseEntity;

public interface UtilController {

    /**
     * A simple ping endpoint to check service availability.
     *
     * @return A ResponseEntity containing the string "Pong!" and HTTP status.
     */
    ResponseEntity<String> ping();

    /**
     * An endpoint to verify if the provided authentication token is valid.
     *
     * @return A ResponseEntity containing a success message if authentication is valid, and HTTP status.
     */
    ResponseEntity<String> auth();
}
