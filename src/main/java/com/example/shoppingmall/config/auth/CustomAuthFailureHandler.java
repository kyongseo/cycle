package com.example.shoppingmall.config.auth;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
/**
 * 로그인 실패시 에러 메시지를 보여주기 위한 클래스
 */
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 로그인 실패 시 처리할 코드
        String errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
    //    request.getSession().setAttribute("errorMessage", errorMessage); // 세션에 실패 메시지 저장
        request.setAttribute("errorMessage", errorMessage);

        super.setDefaultFailureUrl("/signin?error=true"); // 실패 시 리디렉션할 URL 설정
        super.onAuthenticationFailure(request, response, exception); // 상위 클래스의 로그인 실패 처리 메서드 호출

        // 실패 후에 /signin?error=true 경로로 리다이렉트
    //    getRedirectStrategy().sendRedirect(request, response, "/signin?error=true");
    }

    /* 한글 자체는 url에 맞도록 자동으로 인코딩해주지 않기 때문에, 직접 UTF-8 인코딩 처리 */
    //    errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

    //     setDefaultFailureUrl("/auth/login?error=true&exception="+errorMessage);
}
