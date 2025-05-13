package com.example.emailserver.controller;

import com.example.emailserver.service.RedisService;
import com.example.emailserver.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final SignService signService;
    private final RedisService redisService;

    @PostMapping(value = "/request-sign-up")
    public ResponseEntity<?> sendCodeWithEmail(@RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(signService.sendCode(signUpRequestDto));
    }

    @PostMapping("/checking")
    public ResponseEntity<?> checkCode(@RequestBody EmailCondeValidationDto emailCondeValidationDto){
        String code = redisService.getCode(emailCondeValidationDto.email());
        if(!code.equals(emailCondeValidationDto.code())){
            throw new IllegalArgumentException("두 코드가 다릅니다.");
        }
        return ResponseEntity.ok().build();

    }

}
