package com.spshpau.projectservice.controller;


import org.springframework.http.ResponseEntity;

public interface UtilController {
    ResponseEntity<String> ping();
    ResponseEntity<String> auth();
}
