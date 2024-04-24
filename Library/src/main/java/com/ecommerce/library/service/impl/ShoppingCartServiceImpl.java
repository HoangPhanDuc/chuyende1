package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CartDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartRepository cartRepository;
    private final CustomerService customerService;

    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        if(shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }

        Set<Cart> cartList = shoppingCart.getCartItems();

        Cart cartItem = find(cartList, productDto.getId());
        Product product = transfer(productDto);

        double unitPrice = productDto.getCostPrice();
        double salePrice = productDto.getSalePrice();

        int itemQuantity = 0;

        if(cartList == null) {
            cartList = new HashSet<>();
            if(cartItem == null) {
                cartItem = new Cart();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setSalePrice(salePrice);
                cartList.add(cartItem);
                cartRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() +quantity;
                cartItem.setQuantity(itemQuantity);
                cartRepository.save(cartItem);
            }
        } else {
            if(cartItem == null) {
                cartItem = new Cart();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setSalePrice(salePrice);
                cartList.add(cartItem);
                cartRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() +quantity;
                cartItem.setQuantity(itemQuantity);
                cartRepository.save(cartItem);
            }
        }
        shoppingCart.setCartItems(cartList);

        double totalPrice = totalPrice(shoppingCart.getCartItems());
        int totalItem = totalItem(shoppingCart.getCartItems());

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setCustomer(customer);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getReferenceById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getReferenceById(id);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setCustomer(shoppingCart.getCustomer());
        System.out.println("Customer Info in ShoppingCartImpl: " + shoppingCart.getCustomer());
        shoppingCartDto.setTotalPrice(shoppingCart.getTotalPrice());
        shoppingCartDto.setTotalItems(shoppingCart.getTotalItems());

        Set<CartDto> cartDtoSet = new HashSet<>();
        for(Cart cart : shoppingCart.getCartItems()) {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setSalePrice(cart.getSalePrice());

            ShoppingCartDto shoppingCartDto1 = new ShoppingCartDto();
            shoppingCartDto1.setId(cart.getCart().getId());
            shoppingCartDto1.setCustomer(cart.getCart().getCustomer());
            shoppingCartDto1.setTotalPrice(cart.getCart().getTotalPrice());
            shoppingCartDto1.setTotalItems(cart.getCart().getTotalItems());

            ProductDto productDto = new ProductDto();
            productDto.setId(cart.getProduct().getId());
            productDto.setName(cart.getProduct().getName());
            productDto.setDescription(cart.getProduct().getDescription());
            productDto.setCostPrice(cart.getProduct().getCostPrice());
            productDto.setSalePrice(cart.getProduct().getSalePrice());
            productDto.setCurrentQuantity(cart.getProduct().getCurrentQuantity());
            productDto.setCategory(cart.getProduct().getCategory());
            productDto.setImage(cart.getProduct().getImage());
            productDto.setActive(cart.getProduct().is_active());
            productDto.setDeleted(cart.getProduct().is_deleted());

            cartDto.setCart(shoppingCartDto);
            cartDto.setProduct(productDto);
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setUnitPrice(cart.getUnitPrice());
            cartDto.setSalePrice(cart.getSalePrice());

            cartDtoSet.add(cartDto);
        }

        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto addItemSession(ProductDto productDto, ShoppingCartDto cartDto, int quantity) {
        CartDto cartItem = findDto(cartDto, productDto.getId());
        if(cartDto == null) {
            cartDto = new ShoppingCartDto();
        }
        Set<CartDto> cartList = cartDto.getCartItems();

        double unitPrice = productDto.getCostPrice();
        double salePrice = productDto.getSalePrice();

        int itemQuantity = 0;

        if(cartList == null) {
            cartList = new HashSet<>();
            if(cartItem == null) {
                cartItem = new CartDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setSalePrice(salePrice);
                cartList.add(cartItem);
                System.out.println("Add Item To Cart" + cartList);
            } else {
                itemQuantity = cartItem.getQuantity() +quantity;
                cartItem.setQuantity(itemQuantity);
            }
        } else {
            if(cartItem == null) {
                cartItem = new CartDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setSalePrice(salePrice);
                cartList.add(cartItem);
                System.out.println("Add Item To Cart" + cartList);
            } else {
                itemQuantity = cartItem.getQuantity() +quantity;
                cartItem.setQuantity(itemQuantity);
            }
        }
        System.out.println("here");
        cartDto.setCartItems(cartList);
        double totalPrice = totalPriceDto(cartList);
        int totalItem = totalItemDto(cartList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println("Total Item in ShoppingCartImpl: " + cartDto.getTotalItems());
        System.out.println("Total Price in ShoppingCartImpl: " + cartDto.getTotalPrice());
        System.out.println("success");
        return cartDto;
    }

    @Override
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<Cart> cartItemList = shoppingCart.getCartItems();
        Cart item = find(cartItemList, productDto.getId());
        cartItemList.remove(item);
        cartRepository.delete(item);

        System.out.println("The other Item in Cart: " + shoppingCart.getCartItems());
        System.out.println("Find Item have been Deleted in Cart: " + item);

        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);

        System.out.println("The total Items in Cart: " + shoppingCart);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public void deletedCartById(Long id) {
        shoppingCartRepository.deleteShoppingCartById(id);
    }

    @Override
    @Transactional
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<Cart> cartItemList = shoppingCart.getCartItems();
        Cart item = find(cartItemList, productDto.getId());
        int itemQuantity = quantity;

        item.setQuantity(itemQuantity);
        cartRepository.save(item);
        shoppingCart.setCartItems(cartItemList);
        int totalItem = totalItem(cartItemList);
        double totalPrice = totalPrice(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return shoppingCartRepository.save(shoppingCart);
    }


    private Cart find(Set<Cart> cartItems, long productId) {
        if(cartItems == null) {
            return null;
        }
        Cart cartItem = null;
        for(Cart cart : cartItems) {
            if(cart.getProduct().getId() == productId) {
                cartItem = cart;
            }
        }
        return cartItem;
    }
    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_active(productDto.isActive());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());

        return product;
    }

    private double totalPrice(Set<Cart> cartItem) {
        double total = 0.0;
        for(Cart item : cartItem) {
            total += item.getUnitPrice() * item.getQuantity();
        }
        return total;
    }

    private int totalItem(Set<Cart> cartItem) {
        int total = 0;
        for(Cart item : cartItem) {
            total += item.getQuantity();
        }
        return total;
    };

    private CartDto findDto(ShoppingCartDto shoppingCartDto, long productId) {
        if(shoppingCartDto == null) {
            return null;
        }
        CartDto cartItem = null;
        for(CartDto item : shoppingCartDto.getCartItems()) {
            if(item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    };

    private double totalPriceDto(Set<CartDto> cartItemList) {
        double totalPrice = 0;
        for (CartDto item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private int totalItemDto(Set<CartDto> cartItemList) {
        int totalItem = 0;
        for (CartDto item : cartItemList) {
            totalItem = item.getQuantity();
        }
        return totalItem;
    }
}