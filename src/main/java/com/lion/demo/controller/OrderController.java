package com.lion.demo.controller;

import com.lion.demo.entity.BookStat;
import com.lion.demo.entity.Cart;
import com.lion.demo.entity.Order;
import com.lion.demo.entity.OrderItem;
import com.lion.demo.service.BookService;
import com.lion.demo.service.CartService;
import com.lion.demo.service.OrderService;
import com.lion.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;

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
        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        String uid = (String) session.getAttribute("sessUid");
        List<Order> orderList = orderService.getOrdersByUser(uid);
        List<String> orderTitleList = new ArrayList<>();
        for (Order order: orderList) {
            List<OrderItem> orderItems = order.getOrderItems();
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1)
                title += " 외 " + (size - 1) + " 건";
            orderTitleList.add(title);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        return "order/list";
    }

    @GetMapping("/listAll")
    public String listAll(Model model) {
        // 2024년 12월
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999);
        List<Order> orderList = orderService.getOrderByDateRange(startTime, endTime);
        int totalRevenue = 0, totalBooks = 0;
        String bookName = null;
        Integer sold, revenue = 0;
        List<String> orderTitleList = new ArrayList<>();
        Map<String, Integer> mostSoldMap= new HashMap<>();
        Map<String, Integer> mostRevenue = new HashMap<>();
        for (Order order: orderList) {
            totalRevenue += order.getTotalAmount();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem: orderItems) {
                totalBooks += orderItem.getQuantity();
                bookName = orderItem.getBook().getTitle();
                sold = orderItem.getQuantity();
                revenue = orderItem.getSubPrice();
                if (mostSoldMap.containsKey(bookName) && mostRevenue.containsKey(bookName)) {
                    sold += mostSoldMap.get(bookName);
                    mostSoldMap.put(bookName, sold);
                    revenue += mostRevenue.get(bookName);
                    mostRevenue.put(bookName, revenue);
                }
                else {
                    mostSoldMap.put(bookName, sold);
                    mostRevenue.put(bookName, revenue);
                }

            }
            String title = orderItems.get(0).getBook().getTitle();
            int size = orderItems.size();
            if (size > 1)
                title += " 외 " + (size - 1) + " 건";
            orderTitleList.add(title);
        }

        String mostSoldBook = mostSoldMap.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                        .orElse(null);
        String mostRevBook = mostRevenue.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        model.addAttribute("orderList", orderList);
        model.addAttribute("orderTitleList", orderTitleList);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("mostSoldBookName", mostSoldBook);
        model.addAttribute("MostRevBookName", mostRevBook);
        System.out.println(mostRevBook);
        System.out.println(mostSoldMap);
        System.out.println(mostRevBook);
        System.out.println("===================================");
        return "order/listAll";
    }

    @GetMapping("/bookStat")
    public String bookStat(Model model) {
        // 2024년 12월
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999);
        List<Order> orderList = orderService.getOrderByDateRange(startTime, endTime);
        Map<Long, BookStat> map = new HashMap<>();

        for (Order order: orderList) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item: orderItems) {
                long bid = item.getBook().getBid();
                if (map.containsKey(bid)) {
                    BookStat bookStat = map.get(bid);
                    bookStat.setQuantity(bookStat.getQuantity() + item.getQuantity());
                    map.replace(bid, bookStat);
                } else {
                    BookStat bookStat = BookStat.builder()
                            .bid(bid)
                            .title(item.getBook().getTitle())
                            .company(item.getBook().getCompany())
                            .unitPrice(item.getBook().getPrice())
                            .quantity(item.getQuantity())
                            .build();
                    map.put(bid, bookStat);
                }
            }
        }

        List<BookStat> bookStatList = new ArrayList<>();
        for (Map.Entry<Long, BookStat> entry: map.entrySet()) {
            BookStat bookStat = entry.getValue();
            bookStat.setTotalPrice(bookStat.getUnitPrice() * bookStat.getQuantity());
            bookStatList.add(bookStat);
        }
        model.addAttribute("bookStatList", bookStatList);
        return "order/bookStat";
    }
}