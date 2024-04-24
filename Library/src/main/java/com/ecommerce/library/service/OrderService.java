package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAllOrders();

    Order acceptOrder(Long id, Order order);

    Order cancelOrder(Long id, Order order);
}

