package com.example.emailserver.service;

import com.example.emailserver.controller.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

    private final MainSendService mailService;
    private final RedisService redisService;

    public String sendCode(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.email();
        String code = mailService.sendMail(email);
        redisService.setCode(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String redisCode = redisService.getCode(email);
        if (redisCode.equals(code)) {
            return true;
        }
        return false;
    }
}
