package com.example.ggj_be.global.jwt;


import com.example.ggj_be.global.exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.example.ggj_be.global.util.ExceptionHandlerUtil.exceptionHandler;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            log.info("===================== ExceptionFilter - Exception Control");
            exceptionHandler(response, e.getErrorStatus(), HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}