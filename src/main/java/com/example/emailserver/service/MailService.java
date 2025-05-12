package com.example.emailserver.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {
    private static String number;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static void createNumber(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) { // 총 8자리 인증 번호 생성
            int idx = random.nextInt(3); // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다

            // 0,1,2 값을 switchcase를 통해 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // 0일 때, a~z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 1일 때, A~Z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2일 때, 0~9 까지 랜덤 생성 후 key에 추가
                    key.append(random.nextInt(9));
                    break;
            }
        }
        number = key.toString();
    }

    public MimeMessage createMessage(String email){
        createNumber();
        log.info("Number : {}",number);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true); // Helper 사용
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("[오늘의 하루] 이메일 인증 번호 발송");
            String body = "<html><body style='background-color: #000000; margin: 0 auto; max-width: 600px; word-break: break-word; padding: 50px 30px; color: #ffffff; font-family: sans-serif;'>";

            body += "<h1 style='padding-top: 40px; font-size: 30px; margin-bottom: 30px;'>이메일 주소 인증</h1>";

            body += "<p style='font-size: 18px; line-height: 32px; margin-bottom: 24px;'>안녕하세요? 오늘의 하루 관리자 입니다.</p>";
            body += "<p style='font-size: 18px; line-height: 32px; margin-bottom: 24px;'>오늘의 하루 서비스 사용을 위해 고객님께서 입력하신 이메일 주소의 인증이 필요합니다.</p>";
            body += "<p style='font-size: 18px; line-height: 32px; margin-bottom: 24px;'>하단의 인증 번호로 이메일 인증을 완료하시면, 정상적으로 오늘의 하루 서비스를 이용하실 수 있습니다.</p>";
            body += "<p style='font-size: 18px; line-height: 32px; margin-bottom: 24px;'>항상 최선의 노력을 다하는 오늘의 하루가 되겠습니다.</p>";
            body += "<p style='font-size: 18px; line-height: 32px;'>감사합니다.</p>";

            body += "<div style='margin-top: 40px; padding: 20px 0; color: #000000; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px; font-weight: bold;'>" + number + "</div>";

            body += "</body></html>";   messageHelper.setText(body, true);
        //    ClassPathResource image = new ClassPathResource("img/thedayoftoday.png");
         //   messageHelper.addInline("image", image);
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return mimeMessage;
    }

    public String sendMail(String email) {
        MimeMessage mimeMessage = createMessage(email);
        log.info("[Mail 전송 시작]");
        javaMailSender.send(mimeMessage);
        log.info("[Mail 전송 완료]");
        return number;
    }
}
