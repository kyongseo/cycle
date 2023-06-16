package com.example.shoppingmall.web;

import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.service.AuthService;
import com.example.shoppingmall.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signin")
    public String SigninForm() {
        return "signin";
    }

    @GetMapping("/signup")
    public String SignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(SignupDto signupDto, MultipartFile file) throws Exception {

        // User에 signupDto 넣음
        User user = signupDto.toEntity();

        User userEntity = authService.signup(user, file);
        System.out.println(userEntity);

        return "signin"; //
    }
}