package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.cart.CartRepository;
import com.example.shoppingmall.domain.cart_item.Cart_item;
import com.example.shoppingmall.domain.cart_item.Cart_itemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartFinderService {

    private final CartRepository cartRepository;
    private final Cart_itemRepository cart_itemRepository;

    public Cart findCart(int userId) {
        return cartRepository.findByUserId(userId);
    }

    // 유저의 Cart id가 들어있는 모든 Cart_item 을 반환해준다
    public List<Cart_item> findUserCart_items(Cart userCart) {
        //
        int userCartId = userCart.getId(); // 카트 고유 번호
        List<Cart_item> allUserCart_items = new ArrayList<>(); // 유저가 담은 아이템들

        List<Cart_item> allCart_items = cart_itemRepository.findAll(); // 전체 Cart_item

        for(Cart_item cart_item : allCart_items) {
            if(cart_item.getCart().getId() == userCartId) {
                allUserCart_items.add(cart_item);
            }
        }

        return allUserCart_items;
    }


    public List<Cart_item> findCart_itemByItemId(int id) {
        List<Cart_item> cart_items = cart_itemRepository.findCart_itemByItemId(id);
        return cart_items;
    }
}