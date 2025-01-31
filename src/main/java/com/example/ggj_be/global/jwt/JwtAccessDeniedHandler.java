package com.example.ggj_be.global.jwt;

import com.example.ggj_be.global.response.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import static com.example.ggj_be.global.util.ExceptionHandlerUtil.exceptionHandler;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        exceptionHandler(response, ErrorStatus._FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
    }
}