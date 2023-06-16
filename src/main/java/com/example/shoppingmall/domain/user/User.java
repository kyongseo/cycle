package com.example.shoppingmall.domain.user;

import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.history.History;
import com.example.shoppingmall.domain.item.Item;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity // DB에 테이블 자동 생성
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true) // username 중목 안됨
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String grade; // 회원등급
    private String role; // 권한

    private String filename; // 파일 이름
    private String filepath; // 파일 경로

    private int money;

    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<>(); // item의 판매자와 연결

    @OneToMany(mappedBy = "user")
    private List<History> userHistory = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<History> sellerHistory = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.createDate = LocalDate.now();
    }
}