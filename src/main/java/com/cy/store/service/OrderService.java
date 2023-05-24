package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.entity.Address;
import com.cy.store.entity.Order;

public interface OrderService extends IService<Order> {

    Order create(Integer aid,Integer uid,String username,Integer[] cids);
}
