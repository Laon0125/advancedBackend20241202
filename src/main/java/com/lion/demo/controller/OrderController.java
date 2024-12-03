package com.lion.demo.controller;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.OrderService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired private BookService bookService;
    @Autowired private CartService cartService;
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    @GetMapping("/createOrder")
    public String createOrder(HttpSession session) {
        String uid = (String) session.getAttribute("sessUid");
        List<Cart> cartList = cartService.getCartItemsByUser(uid);
        Order order = orderService.createOrder(uid, cartList);
        return "redirect:/mall/list";
    }
}