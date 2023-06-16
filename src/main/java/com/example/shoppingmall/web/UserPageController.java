package com.example.shoppingmall.web;

import com.example.shoppingmall.config.auth.PrincipalDetails;
import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.cart_item.Cart_item;
import com.example.shoppingmall.domain.history.History;
import com.example.shoppingmall.domain.item.Item;
import com.example.shoppingmall.domain.user.User;
import com.example.shoppingmall.service.CartFinderService;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.ItemService;
import com.example.shoppingmall.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserPageController {
    private final UserPageService userPageService;
    private final CartService cartService;
    private final ItemService itemService;
    private final CartFinderService cartFinderService;

    // 유저 페이지
    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() == id && principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 로그인 정보가 일치하고, 판매자일 경우
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            model.addAttribute("user",userPageService.findUser(id));
            return "/seller/sellerUserPage";
        }

        else if (principalDetails.getUser().getId() == id && principalDetails.getUser().getRole().equals("ROLE_USER")) {
            // 로그인 정보와 접속하는 유저 페이지의 id 값이 같으면 유저페이지 렌더링
            // 즉 본인은 본인 페이지만 볼 수 있음
            int cartCount = 0;
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            Cart userCart = cartFinderService.findCart(loginUser.getId());
            List<Cart_item> userItems = cartFinderService.findUserCart_items(userCart);
            cartCount = userItems.size();

            model.addAttribute("cartCount", cartCount);
            model.addAttribute("user", userPageService.findUser(id));
            return "/user/userPage";
        } else {
            return "redirect:/main";
        }
    }

    // 유저 페이지 수정 렌더링
    @GetMapping("/user/{id}/modify")
    public String userPageModify(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getId() == id) {
            model.addAttribute("user", userPageService.findUser(id));
            return "/user/userPageEdit";
        } else {
            return "redirect:/main";
        }
    }

    @PostMapping("/user/{id}/update")
    public String userPageModifyProcess(@PathVariable("id") Integer id, User user, MultipartFile file) throws Exception {
        userPageService.userPageModify(user, file);
        return "redirect:/user/{id}";
    }




    // 장바구니
    @GetMapping("/user/{id}/cart")
    public String userCartView(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() == id) {
            // 로그인 정보와 접속하는 유저 페이지의 id 값이 같으면 유저페이지 렌더링
            // 즉 본인은 본인 페이지만 볼 수 있음
            Cart userCart = cartFinderService.findCart(id); // 유저의 카트

            if (userCart == null) {
                // 만약 카트가 비어있다면?
                return "redirect:/main";
            } else {
                // 카트가 있는 경우
                List<Cart_item> userCart_items = cartFinderService.findUserCart_items(userCart); // 유저의 카트ID가 들어간 모든 Cart_item 반환

                // 카트에 담긴 아이템의 수
                int cartCount = 0;

                // 물품의 가격 총 합
                int totalPrice = 0;
                for (Cart_item item : userCart_items) {
                    cartCount += 1;
                    totalPrice += item.getCount() * item.getItem().getPrice();
                }

                model.addAttribute("cartCount", cartCount);
                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("cartItems", userCart_items);
                model.addAttribute("user", userPageService.findUser(id));

                return "/user/userCart";
            }

        } else {
            // 본인 페이지가 아닌 곳을 들어갈 경우
            return "redirect:/main";
        }
    }

    // 장바구니 추가
    @PostMapping("/user/{id}/cart/{itemId}")
    public String addCartItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, int quantity) {

        System.out.println("id == " + id + "  itemId == " + itemId + "  quantity == " + quantity);

        User loginUser = userPageService.findUser(id);
        Item item = itemService.itemView(itemId);

        cartService.addItem(loginUser, item, quantity);

        return "redirect:/item/{itemId}";
    }


    // 장바구니 삭제
    @GetMapping("/user/{id}/cart/{cart_itemId}/delete")
    public String deleteCartItem(@PathVariable("id") Integer id, @PathVariable("cart_itemId") Integer cart_itemId, Model model) {

        cartService.deleteCart_item(cart_itemId);

        Cart userCart = cartFinderService.findCart(id); // 유저의 카트
        // 카트가 있는 경우
        List<Cart_item> userCart_items = cartFinderService.findUserCart_items(userCart); // 유저의 카트ID가 들어간 모든 Cart_item 반환
        // 물품의 가격 총 합
        int totalPrice = 0;
        for (Cart_item item : userCart_items) {
            totalPrice += item.getCount() * item.getItem().getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", userCart_items);
        model.addAttribute("user", userPageService.findUser(id));

        return "/user/userCart";

    }



    // 장바구니 구매 진행
    @PostMapping("/user/{id}/cart/checkout")
    public String checkout(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        if(principalDetails.getUser().getId() == id) {
            // 로그인한 유저가 구매를 진행하는 경우

            Cart userCart = cartFinderService.findCart(id); // 유저의 카트
            List<Cart_item> userCart_items = cartFinderService.findUserCart_items(userCart); // 유저의 카트ID가 들어간 모든 Cart_item 반환

            // 총 결제 금액 구하기 & 품절 상품 및 재고가 적은 경우 처리
            int totalPrice = 0;
            for (Cart_item item : userCart_items) {
                if(item.getItem().getStock() == 0 || item.getCount() > item.getItem().getStock()) {
                    // 품절 상품이 있거나, 유저가 구매하려는 물건의 재고가 더 많은 경우
                    // 추후에 JS로 팝업 띄우기
                    return "redirect:/main";
                }
                totalPrice += item.getCount() * item.getItem().getPrice();
            }


            // 전체 결제 처리
            User loginUser = userPageService.findUser(id);
            int loginUserMoney = loginUser.getMoney();

            if(loginUserMoney < totalPrice) {

                // 결제 금액이 부족한 경우
                return "redirect:/main";
            } else {


                // 결제 처리 & 재고 처리
                // 각각의 아이템에 대해 하나씩 처리

                for(Cart_item item : userCart_items) {
                    User seller = item.getItem().getUser(); // 판매자

                    // 결제 처리
                    loginUser.setMoney(loginUser.getMoney() - (item.getCount() * item.getItem().getPrice()));
                    seller.setMoney(seller.getMoney() + (item.getCount() * item.getItem().getPrice()));

                    // 재고 처리
                    item.getItem().setStock(item.getItem().getStock() - item.getCount());

                    // item-count (판매자의 물품이 얼마나 팔렸는지 count) 처리
                    item.getItem().setCount(item.getItem().getCount() + item.getCount());


                    // 결제를 마친 경우 주문내역 담기 및 판매자 정보 담기 && 장바구니 품목 모두 제거
                    cartService.saveHistory(id, item);
                    cartService.deleteCart_item(item.getId());


                }
            }


            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("cartItems", userCart_items);
            model.addAttribute("user", userPageService.findUser(id));
            return "redirect:/user/{id}/cart";


        } else {

            // 다른 사용자가 구매를 시도하는 경우
            return "redirect:/main";
        }
    }


    // 주문내역 view 렌더링
    @GetMapping("/user/{id}/history")
    public String UserHistory(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if(principalDetails.getUser().getId() == id) {

            int cartCount = 0;
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            Cart userCart = cartFinderService.findCart(loginUser.getId());
            List<Cart_item> userItems = cartFinderService.findUserCart_items(userCart);
            cartCount = userItems.size();

            List<History> histories = cartService.getHistoriesForUser(loginUser);

            model.addAttribute("histories", histories);
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("user", loginUser);
            return "/user/userHistory";
        } else {
            return "redirect:/main";
        }
    }



    // 잔액충전 페이지 렌더링
    @GetMapping("/user/{id}/charge")
    public String ChargeMoney(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (id != principalDetails.getUser().getId()) {
            return "redirect:/main";
        }

        model.addAttribute("user", userPageService.findUser(id));
        return "/user/chargePage";
    }

    @GetMapping("/user/charge/process")
    public String ChargeMoneyProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, int amount) {

        System.out.println(amount);
        userPageService.chargeMoney(principalDetails.getUser(), amount);
        return "redirect:/main";
    }
}