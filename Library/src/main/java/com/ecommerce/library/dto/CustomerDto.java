package com.ecommerce.library.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String username;

    private String email;

    private String password;

    @Size(max = 10, message = "Phone number must have been 10 numbers")
    private String phone;

    private String confirmPassword;

    private String address;
}
