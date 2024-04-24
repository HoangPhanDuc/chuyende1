package com.ecommerce.library.service;

import com.ecommerce.library.dto.CartDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username);

    ShoppingCartDto getReferenceById(Long id);

    ShoppingCartDto addItemSession(ProductDto productDto, ShoppingCartDto cartDto, int quantity);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);

    ShoppingCart updateCart(ProductDto productDto, int quantity, String username);

    void deletedCartById(Long id);


}
