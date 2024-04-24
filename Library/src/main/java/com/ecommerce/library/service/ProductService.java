package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    List<ProductDto> products();

    List<ProductDto> allProduct();

    Product save(MultipartFile imageProduct, ProductDto productDto);

    Product update(MultipartFile imageProduct, ProductDto productDto);

    ProductDto getReferenceById(Long id);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    List<ProductDto> findAllByCategory(String category);

    List<ProductDto> listViewProducts();

    List<ProductDto> searchProducts(String keyword);

}
