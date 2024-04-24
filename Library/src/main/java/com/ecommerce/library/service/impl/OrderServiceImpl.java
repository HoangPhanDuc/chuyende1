package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final ShoppingCartService shoppingCartService;

    @Override
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setQuantity(shoppingCart.getTotalItems());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(Cart cart : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setUnitPrice(cart.getUnitPrice());
            orderDetail.setProduct(cart.getProduct());

            orderDetailRepository.save(orderDetail);

            orderDetailList.add(orderDetail);
        }

        order.setOrderDetailList(orderDetailList);
        shoppingCartService.deletedCartById(shoppingCart.getId());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order acceptOrder(Long id, Order order) {
        order = orderRepository.getReferenceById(id);
        order.setAccept(true);
        order.setOrderStatus("Processing");
        System.out.println(order.isAccept());
        System.out.println(id);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long id, Order order) {
        order = orderRepository.getReferenceById(id);
        order.setAccept(false);
        order.setOrderStatus("Canceled");
        return orderRepository.save(order);
    }

}