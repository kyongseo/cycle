package com.example.shoppingmall.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findItemById(int id);

    List<Item> findItemsByUserIdOrderByCountDesc(int id);


}