package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    private String name;

    private String description;

    private double costPrice;

    private double salePrice;

    private int currentQuantity;

    private Category category;

    private String image;

    private boolean active;

    private boolean deleted;
}
