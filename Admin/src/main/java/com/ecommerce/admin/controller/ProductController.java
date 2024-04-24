package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model) {
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());
        System.out.println("Product Size: "+products.size());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "products";
    };

    @PostMapping("/save-product")
    public String saveProduct (@ModelAttribute("productDto")ProductDto productDto,
                               @RequestParam("imageProduct")MultipartFile imageProduct,
                               RedirectAttributes attributes) {
        try {
            productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Added Product Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            attributes.addFlashAttribute("failed", "Something went wrong!!");
        }
        return "redirect:/products";
    }

    @GetMapping("/search-products")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        try {
            List<ProductDto> productDtos = productService.searchProducts(keyword);

            model.addAttribute("title", "Search Product");
            model.addAttribute("page", "Search Result");
            model.addAttribute("products", productDtos);
            model.addAttribute("size", productDtos.size());

            System.out.println("product size in ProductController Admin: " + productDtos.size());
        } catch (Exception ex) {
            System.out.println("Ngu" + ex);
            ex.printStackTrace();
        }
        return "products-result";
    }

    @GetMapping("/add-product")
    public String addProductPage(Model model) {
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findByActive();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "add-product";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        model.addAttribute("title", "Update products");
        System.out.println("product ID:" + productService.getReferenceById(id));

        List<Category> categories = categoryService.findByActive();
        ProductDto productDto = productService.getReferenceById(id);

        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update-product";
    };

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update Product Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Something went wrong!!!");
        }

        return "redirect:/products";
    };

    @RequestMapping(value = "/deleted-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            System.out.println(id);
            redirectAttributes.addFlashAttribute("success", "Deleted Product Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Something went wrong!!");
        }

        return "redirect:/products";
    }
}