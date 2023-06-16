package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.cart.CartRepository;
import com.example.shoppingmall.domain.cart_item.Cart_item;
import com.example.shoppingmall.domain.cart_item.Cart_itemRepository;
import com.example.shoppingmall.domain.history.History;
import com.example.shoppingmall.domain.history.HistoryRepository;
import com.example.shoppingmall.domain.item.Item;
import com.example.shoppingmall.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final Cart_itemRepository cart_itemRepository;
    private final CartRepository cartRepository;
    private final CartFinderService cartFinderService;
    private final HistoryRepository historyRepository;
    private final UserPageService userPageService;

    // 유저 장바구니에 담긴 물품의 개수가 0개이고, 새로 추가할 때
    // 카트 하나를 생성함
    // 장바구니에 물건 추가 메소드

    @Transactional
    public void addItem(User user, Item item, int quantity) {

        // 만약 품절된 물건이라면?
        if(item.getStock() == 0) {
            return;
        }

        // 재고가 3개 남았는데 4개를 담는다면?
        if(item.getStock() < quantity) {
            return;
        }


        if(cartRepository.findByUserId(user.getId()) == null) {
            // 유저ID에 대한 장바구니가 없을 때 == 즉 장바구니에 물건을 처음 담는 경우
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            // 여기까지 하면, cart(카트고유ID,유저) 가 담긴다.

            Cart_item cart_item = new Cart_item();
            cart_item.setCart(cart);
            cart_item.setItem(item);
            cart_item.setCount(quantity);
            cart_itemRepository.save(cart_item);
        } else {
            // 유저 ID에 대한 장바구니가 있을 때

            Cart userCart = cartRepository.findByUserId(user.getId());
            List<Cart_item> allUserCart_item = cartFinderService.findUserCart_items(userCart);
            for(Cart_item userItem : allUserCart_item) {
                if(userItem.getItem().getId() == item.getId()) {
                    // 만약 같은 물건을 담는다면? (중복)
                    return;
                }
            }
            // 중복인 물건이 없다면
            Cart_item cart_item = new Cart_item();
            cart_item.setCart(userCart);
            cart_item.setItem(item);
            cart_item.setCount(quantity);
            cart_itemRepository.save(cart_item);
        }
    }


    // 장바구니에 담긴 아이템 삭제 (== cart_item 삭제)
    public void deleteCart_item(int id) {
        cart_itemRepository.deleteById(id);
    }


    // 주문내역에 구매 내역 남기는 로직
    @Transactional
    public void saveHistory(int id, Cart_item item) {
        // 유저 id와 cartItem을 매개변수로 받음

        User user = userPageService.findUser(id);
        History history = new History();

        history.setUser(user);
        history.setItemName(item.getItem().getName());
        history.setItemPrice(item.getItem().getPrice());
        history.setItemCount(item.getCount());
        history.setSeller(item.getItem().getUser());
        historyRepository.save(history);
    }

    // 구매자 판매내역 반환
    public List<History> getHistoriesForUser(User user) {
        if(historyRepository.findHistoriesByUser(user) == null) {
            // 아무것도 없을때 빈 걸 생성해서 보냄
            History history = new History();
        }
        return historyRepository.findHistoriesByUser(user);
    }

    // 판매자 판매내역 반환
    public List<History> getHistoriesForSeller(User user) {
        if(historyRepository.findHistoryBySeller(user) == null) {
            History history = new History();
        }

        return historyRepository.findHistoryBySeller(user);
    }


    public History getHistory(int id) {
        return historyRepository.findHistoryById(id);
    }

}