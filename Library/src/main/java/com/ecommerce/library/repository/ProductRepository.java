package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.is_deleted = false and p.is_active = true")
    List<Product> getAllProduct();

    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_active = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);


    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, " +
        "p.cost_price, p.category_id, p.sale_price, p.image, p.is_active, " +
        "p.is_deleted from products p where p.is_deleted = false " +
        "and p.is_active = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProducts(String keyword);
}
