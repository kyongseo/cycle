package com.example.shoppingmall.domain.history;

import com.example.shoppingmall.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findHistoriesByUser(User user);
    History findHistoryByUser(User user);

    List<History> findHistoryBySeller(User user); // seller

    History findHistoryById(int id);
}