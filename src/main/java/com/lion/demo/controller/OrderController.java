package com.lion.demo.controller;

import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.entity.OrderItem;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.OrderService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/createOrder")
    public String createOrder(HttpSession session) {
        String uid = (String) session.getAttribute("sessUid");
        List<Cart> cartList = cartService.getCartItemsByUser(uid);
        Order order = orderService.createOrder(uid, cartList);
        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        String uid = (String) session.getAttribute("sessUid");
        List<Order> orderList = orderService.getOrdersByUser(uid);
        List<String> orderTitleList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItem> orderItems = order.getOrderItems();
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1) {
                title += " 외" + (size - 1) + " 건";
            }
            orderTitleList.add(title);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        return "order/list";
    }

    @GetMapping("/listAll")
    public String ListAll(Model model) {
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        List<Order> orderList = orderService.getOrderByDateRange(startTime, endTime);

        int totalRevenue = 0, totalBooks = 0;
        List<String> orderTitleList = new ArrayList<>();
        for (Order order : orderList) {
            totalRevenue += order.getTotalAmount();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                totalBooks += orderItem.getQuantity();
            }
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1) {
                title += " 외" + (size - 1) + " 건";
            }
            orderTitleList.add(title);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        model.addAttribute("totalRevenue", orderList);
        model.addAttribute("totalBooks", orderTitleList);
        return "order/listAll";
    }
}