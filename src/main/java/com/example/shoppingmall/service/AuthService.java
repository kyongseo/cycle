package com.example.shoppingmall.service;


import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.cart.CartRepository;
import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;

    @Transactional // Write(Insert, Update, Delete)
    public User signup(User user, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        user.setFilename(fileName);
        user.setFilepath("/files/" + fileName);


        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setGrade("BRONZE"); // 기본 Grade 설정

        User userEntity = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return userEntity;

    }
}