package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Order> orderList = orderService.findAllOrders();
            model.addAttribute("orders", orderList);
            return "orders";
        }
    }


    @RequestMapping(value = "/accept-order/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(@PathVariable Long id, Order order, RedirectAttributes attributes, Principal principal) {
        if (principal != null) {
            orderService.acceptOrder(id, order);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/orders";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/cancel-order/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(@PathVariable Long id, Order order, Principal principal, RedirectAttributes attributes) {
        if (principal != null) {
            orderService.cancelOrder(id, order);
            attributes.addFlashAttribute("success", "Order Canceled");
            return "redirect:/orders";
        } else {
            return "redirect:/login";
        }
    }


}
