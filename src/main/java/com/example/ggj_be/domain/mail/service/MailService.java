package com.example.ggj_be.domain.mail.service;

import com.example.ggj_be.domain.member.dto.MemberRequest;
import jakarta.mail.MessagingException;

public interface MailService {


    void sendEmailMessage(String email) throws MessagingException;

    String verifyAuthCode(MemberRequest.VerifyCode request);


    void sendTemporaryPassword(String email) throws MessagingException;

}
