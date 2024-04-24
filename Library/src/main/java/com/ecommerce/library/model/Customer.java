package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name ="customers", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    private String username;

    @Column(name = "email")
    private String email;

    private String password;

    private String address;

    private String phoneNumber;

    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_roles",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Customer() {
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer {" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", address'" + address + '\'' +
                ", phoneNumber'" + phoneNumber + '\'' +
                ", email'" + email + '\'' +
                ", roles=" + roles +
                ", cart=" + cart.getId() +
                ", cart=" + orders.size() +
                '}';
    }
}