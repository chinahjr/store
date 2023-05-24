package com.cy.store.dto;

import com.cy.store.entity.Cart;
import com.cy.store.entity.Product;
import lombok.Data;

@Data
public class CartDto extends Cart {

    private Long realPrice;

    private String title;

    private String image;
}
