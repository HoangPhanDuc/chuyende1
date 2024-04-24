package com.ecommerce.library.repository;

import com.ecommerce.library.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Modifying
    @Query(value = "delete from ShoppingCart s where s.id = ?1")
    void deleteShoppingCartById(Long id);

}

