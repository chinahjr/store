package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {

    List<Product> findHotList();

    Product findById(Integer id);
}
