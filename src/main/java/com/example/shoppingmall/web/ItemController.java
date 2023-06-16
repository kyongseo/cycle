package com.example.shoppingmall.web;

import com.example.shoppingmall.config.auth.PrincipalDetails;
import com.example.shoppingmall.domain.cart.Cart;
import com.example.shoppingmall.domain.cart_item.Cart_item;
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

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;
    private final CartFinderService cartFinderService;
    private final CartService cartService;
    private final UserPageService userPageService;

    // 메인 페이지 (로그인 안 한 유저)
    @GetMapping("/")
    public String mainPageNoneLogin(Model model) {
        // 로그인을 안 한 경우
        List<Item> items = itemService.allItemView();

        model.addAttribute("items", items);
        return "none/main";
    }


    // 메인 페이지 (로그인 유저)
    @GetMapping("/main")
    public String mainPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Item> items = itemService.allItemView();
        User loginUser = userPageService.findUser(principalDetails.getUser().getId());

        if(principalDetails.getUser().getRole().equals("ROLE_USER")) {
            // 일반 유저일 경우
            int cartCount = 0;
            Cart userCart = cartFinderService.findCart(loginUser.getId());
            List<Cart_item> userItems = cartFinderService.findUserCart_items(userCart);
            cartCount = userItems.size();
            model.addAttribute("cartCount", cartCount);
        }

        model.addAttribute("items", items);
        model.addAttribute("user", loginUser);

        return "mainLoginPage";
    }


    // 아이템 상세 페이지
    @GetMapping("/item/{id}")
    public String itemView(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("item", itemService.itemView(id));
            return "/seller/item";
        } else {
            // 일반 회원
            int cartCount = 0;
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            Cart userCart = cartFinderService.findCart(loginUser.getId());
            List<Cart_item> userItems = cartFinderService.findUserCart_items(userCart);
            cartCount = userItems.size();

            model.addAttribute("cartCount", cartCount);
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("item", itemService.itemView(id));
            return "/user/item";
        }

    }

    // 아이템 업로드 페이지
    @GetMapping("/item/upload")
    public String itemUpload(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            return "/seller/itemUpload";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }


    // 아이템 업로드 진행
    @PostMapping("/item/upload/process")
    public String itemUploadProcess(Item item, MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        System.out.println("filename == " + file);
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민
            item.setUser(principalDetails.getUser());
            itemService.saveItem(item, file);
            return "redirect:/main";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }

    }


    // 아이템 수정 페이지
    @GetMapping("/item/{id}/modify")
    public String itemModify(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민 혹은 판매자
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()) {
                model.addAttribute("item", itemService.itemView(id));
                return "/seller/itemModify";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }

    }


    // 아이템 수정 처리
    @PostMapping("/item/{id}/modify/process")
    public String itemModifyProcess(Item item, MultipartFile file, @PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()) {
                // 아이템 등록자와, 로그인 유저가 같으면 수정 진행
                itemService.itemModify(item, id, file);
                return "redirect:/main";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 아이템 삭제
    @GetMapping("/item/{id}/delete")
    public String itemDelete(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()) {
                itemService.itemDelete(id);
                return "redirect:/main";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }

    }


}