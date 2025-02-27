package com.example.ggj_be.domain.mail.service;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.member.service.MemberCommandService;
import com.example.ggj_be.domain.member.service.MemberQueryService;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import com.example.ggj_be.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Async
    public void sendEmailMessage(String email) throws MessagingException {

        String authCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[끄적끄적] 이메일 인증 - 인증 코드 전송");
        message.setText(setContext(authCode), "UTF-8", "html");
        javaMailSender.send(message);

        redisUtil.setEmailCode(email, authCode);
    }

    @Override
    @Async
    public void sendTemporaryPassword(String email) throws MessagingException {
        String tempPassword = createCode();

        Optional<Member> member = Optional.ofNullable(memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorStatus._MEMBER_NOT_FOUND)));

        memberCommandService.changePassword(member.get(), tempPassword);


        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[끄적끄적] 임시 비밀번호 안내");

        String emailContent = "<h3>임시 비밀번호 발급</h3>" +
                "<p>아래의 임시 비밀번호로 로그인 후 비밀번호를 변경해주세요.</p>" +
                "<strong>" + tempPassword + "</strong>" +
                "<p>문의사항이 있으시면 아래 메일로 연락주세요.</p>" +
                "<p>david5451231@gmail.com</p>";

        message.setContent(emailContent, "text/html; charset=utf-8");
        javaMailSender.send(message);
    }

    @Override
    public String verifyAuthCode(MemberRequest.VerifyCode request) {

        String authCode = redisUtil.getEmailCode(request.getAccountId());

        return Optional.ofNullable(authCode)
                .filter(code -> code.equals(request.getAuthCode()))
                .map(code -> "이메일 인증에 성공하였습니다.")
                .orElseThrow(() -> new ApiException(ErrorStatus._MAIL_WRONG_CODE));
    }

    private String createCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int authCode = random.nextInt(1000000);
            log.info("===================== authCode: " + authCode);
            return String.format("%06d", authCode);
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ErrorStatus._MAIL_CREATE_CODE_ERROR);
        }
    }

    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mailCode", context);
    }


}
