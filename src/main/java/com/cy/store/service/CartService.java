package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.dto.CartDto;
import com.cy.store.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {

    //将商品添加到购物车之中
    void addToCart(Integer uid,Integer pid,Integer amount,String username);


    /**
     * 查询两张表用于显示购物车页面
     *
     * @param uid
     **/
    List<CartDto> getDTOByUid(Integer uid);


    /**
     * 直接添加购物车
     *
     * @param cid
     * @param uid
     * @param username
     **/
    Integer addNum(Integer cid,Integer uid,String username);


    /**
     * 查询两张表用于显示购物车页面
     *
     * @param cids
     **/
    List<CartDto> getDTOByCid(Integer uid,Integer[] cids);
}
