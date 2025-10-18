package com.iit.dsr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody String req){
        return ResponseEntity.ok("Success");
    }
}
