package com.example.shoppingmall.web;

import com.example.shoppingmall.config.auth.PrincipalDetails;
import com.example.shoppingmall.domain.history.History;
import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class SaleHistoryController {

    private final UserPageService userPageService;
    private final CartService cartService;

    // 판매내역 페이지
    @GetMapping("/seller/{sellerId}/history/{historyId}")
    public String salePageView(@PathVariable("sellerId") Integer sellerId, @PathVariable("historyId") Integer historyId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(principalDetails.getUser().getId() != sellerId) {
            return "redirect:/main";
        }

        History history = cartService.getHistory(historyId);
        User user = history.getUser(); //
        User seller = history.getSeller();

        model.addAttribute("user", user);
        model.addAttribute("seller", seller);
        model.addAttribute("history", history);

        System.out.println(history);

        return "/seller/salePage";
    }
}