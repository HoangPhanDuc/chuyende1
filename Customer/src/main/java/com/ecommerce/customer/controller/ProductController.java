package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        try {
            List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
            List<ProductDto> productDtos = productService.searchProducts(keyword);
            List<ProductDto> listView = productService.listViewProducts();

            model.addAttribute("productViews", listView);
            model.addAttribute("categories", categoryDtos);
            model.addAttribute("title", "Search Product");
            model.addAttribute("page", "Search Result");
            model.addAttribute("products", productDtos);
        } catch (Exception ex) {
            System.out.println("Ngu" + ex);
            ex.printStackTrace();
        }
        return "products";
    }

}
