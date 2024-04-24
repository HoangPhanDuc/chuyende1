package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final CustomerService customerService;

    private final BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home";
    }

    @PostMapping("/do-login-customer")
    public String progressLogin(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                                BindingResult result, Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            if(result.hasErrors()) {
                System.out.println("ngu");
            }
            if(customerDto.getPassword() == null) {
                System.out.println("password null");
            }
            else if(customerDto.getUsername() == null) {
                System.out.println("Email null: " + customerDto.getEmail() + " " +
                        customerDto.getPassword());
                return "login";
            } else {
                System.out.println("Good: " + customerDto.getUsername() + " " +customerDto.getPassword());
                redirectAttributes.addFlashAttribute("success", "Login SuccessFully");
                System.out.println("Login Successfully");
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Something went wrong!!");
            System.out.println("Error Server");
            System.out.println("Something went wrong!!");
        }
        return "home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                                  BindingResult result, Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            if(result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

            Customer customer = customerService.findByUsername(customerDto.getUsername());
            if(customer != null) {
                model.addAttribute("errorUsername", "Username already exists");
                model.addAttribute("error", "Email already exists");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            if(customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                redirectAttributes.addFlashAttribute("success", "Register Successfully");
                System.out.println("Customer Info: " + customerDto);
            } else {
                model.addAttribute("password", "Confirm password does not match");
                System.out.println("Confirm password does not match");
                model.addAttribute("customerDto", customerDto);
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Something went wrong!!");
            System.out.println("Something went wrong!!");
            model.addAttribute("customerDto", customerDto);
            ex.printStackTrace();
        }
        return "register";
    }
}
