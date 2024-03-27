package com.example.shoppingmall.config;

import com.example.shoppingmall.config.auth.CustomAuthFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화
@Configuration // IoC 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthFailureHandler customAuthFailureHandler;

    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//      super.configure(http); // 이 코드 삭제하면 기존 시큐리티가 가진 모든 기능 비활성화
        http
                .csrf().disable(); // csrf 토큰 비활성화 코드
        http
                .authorizeRequests()
                .antMatchers("/main", "/item/**").authenticated() // 이 주소로 시작되면 인증이 필요
                .anyRequest().permitAll() // 그게 아닌 모든 주소는 인증 필요 없음
                .and()
                .formLogin()
                    .loginPage("/signin") // 인증필요한 주소로 접속하면 이 주소로 이동시킴(GetMapping)
                    .loginProcessingUrl("/signin") // 스프링 시큐리티가 로그인 자동 진행(PostMapping)
                    .failureHandler(customAuthFailureHandler) //// 로그인 실패 핸들러
                    .defaultSuccessUrl("/main"); // 로그인이 정상적이면 "/main" 로 이동
    }
}