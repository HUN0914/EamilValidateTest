package com.example.emailserver.service;

import com.example.emailserver.controller.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

    private final MailService mailService;

    public String sendCode(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.email();
        return mailService.sendMail(email);
    }

}
