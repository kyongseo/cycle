package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserPageService {

    private final UserRepository userRepository;

    public User findUser(Integer id) {
        return userRepository.findById(id).get();
    }

    public void userPageModify(User user, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);


        User before = userRepository.findById(user.getId()); // 기존 유저
        before.setFilename(fileName);
        before.setFilepath("/files/" + fileName);
        before.setEmail(user.getEmail());
        before.setAddress(user.getAddress());
        before.setPhone(user.getPhone());
        userRepository.save(before);

    }

    public void chargeMoney(User user, int money) {
        user.setMoney(user.getMoney() + money);
        userRepository.save(user);
    }
}