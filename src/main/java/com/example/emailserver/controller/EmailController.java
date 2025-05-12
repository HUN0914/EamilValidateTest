package com.example.emailserver.controller;

import com.example.emailserver.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final SignService signService;

    @PostMapping(value = "/request-sign-up")
    public ResponseEntity<?> requestSignUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(signService.sendCode(signUpRequestDto));
    }
}
