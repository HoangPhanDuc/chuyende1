package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if(principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();

            if(cart == null) {
                System.out.println("Cart Null");
                model.addAttribute("check", true);
            } else {
                System.out.println("Cart Size " + cart.getCartItems().size());
                System.out.println("Cart Info: " + cart);
                System.out.println("Cart Items: " + cart.getCartItems());
                model.addAttribute("grandTotal", cart.getTotalPrice());
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");
            return "cart";
        }
    }

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST, params = "action=add")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {
        ProductDto productDto = productService.getReferenceById(id);
        if(principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity, username);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());

            model.addAttribute("shoppingCart", shoppingCart);

            System.out.println("Cart Product: " + shoppingCart.getCartItems());
        }
        return "redirect:" + request.getHeader("Referer");
    };

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.POST, params = "action=buy")
    public String buyNow(@RequestParam("id") long productId,
                         @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                         Model model,
                         Principal principal) {
        ProductDto productDto = productService.getReferenceById(productId);
        String username = principal.getName();
        ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity, username);

        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/cart";
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal,
                             HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }

        ProductDto productDto = productService.getReferenceById(id);
        String username = principal.getName();

        ShoppingCart shoppingCart = cartService.updateCart(productDto, quantity, username);
        model.addAttribute("shoppingCart", shoppingCart);
        System.out.println("Shopping Cart in CartController: " + shoppingCart);
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        return "redirect:/cart";
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItem(@RequestParam("id") long productId,
            Principal principal,
            Model model) {
        try {
            ProductDto productDto = productService.getReferenceById(productId);
            String username = principal.getName();
            System.out.println(username + " Deleted Item");
            ShoppingCart shoppingCart = cartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);

            return "redirect:/cart";
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/cart";
    }
}
