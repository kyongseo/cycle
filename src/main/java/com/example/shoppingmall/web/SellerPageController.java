package com.example.shoppingmall.web;

import com.example.shoppingmall.config.auth.PrincipalDetails;
import com.example.shoppingmall.domain.history.History;
import com.example.shoppingmall.domain.item.Item;
import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.ItemService;
import com.example.shoppingmall.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class SellerPageController {
    private final UserPageService userPageService;
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/seller/{id}")
    public String sellerPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() != id) {
            // 판매자 본인이 아니면 메인으로 보내버림
            return "redirect:/main";
        } else {
            // 판매자 본인이 접속한 경우
            User seller = userPageService.findUser(id);
            List<Item> allItem = itemService.allItemView();
            List<Item> userItem = new ArrayList<>();
            // 총 판매대금
            int totalPrice = 0;

            for (Item item : allItem) {
                if (item.getUser().getId() == id) {
                    userItem.add(item);
                    totalPrice += item.getPrice() * item.getCount();
                }
            }

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("seller", seller);
            model.addAttribute("userItem", userItem);
            return "seller/sellerPage";
        }
    }


    // 판매 통계 페이지
    @GetMapping("/seller/{id}/history")
    public String sellerHistory(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getId() == id) {
            User user = userPageService.findUser(id);
            List<History> histories = cartService.getHistoriesForSeller(user);
            List<Item> items = itemService.itemsBySeller(id); // 판매자가 올린 아이템을 판매량 기준으로 내림차순


            model.addAttribute("items", items);
            model.addAttribute("histories", histories);
            model.addAttribute("user", user);
            return "/seller/sellerHistory";
        } else {
            return "redirect:/main";
        }
    }
}
