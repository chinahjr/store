package com.cy.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.dto.CartDto;
import com.cy.store.entity.Cart;
import com.cy.store.entity.Product;
import com.cy.store.mapper.CartMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.CartService;
import com.cy.store.service.ex.AccessDeniedException;
import com.cy.store.service.ex.CartNotFoundException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UpdateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addToCart(Integer uid, Integer pid, Integer amount, String username) {
        //查询当前要添加的购物车是否在表中已经存在
        LambdaQueryWrapper<Cart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUid,uid).eq(Cart::getPid,pid);
        Cart result = cartMapper.selectOne(queryWrapper);
        Date date=new Date();
        if(result==null)
        {
            //表示这个商品没有被添加到购物车之中
            //创建一个cart对象
            Cart cart=new Cart();
            cart.setUid(uid);
            cart.setPid(pid);
            cart.setNum(amount);

            Product product = productMapper.selectById(pid);
            cart.setPrice(product.getPrice());

            //补全日志
            cart.setCreatedUser(username);
            cart.setCreatedTime(date);
            cart.setModifiedTime(date);
            cart.setModifiedUser(username);

            int rows = cartMapper.insert(cart);
            if(rows!=1)
            {
                throw new InsertException("插入数据时产生未知异常");
            }

        }
        else {
            //表示当前商品已经存在于购物车之中，则更新number
            int num = result.getNum() + amount;
            LambdaQueryWrapper<Cart> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Cart::getCid,result.getCid());
            Cart cart=new Cart();
            cart.setNum(num);
            int rows = cartMapper.update(cart, lambdaQueryWrapper);
            if(rows!=1)
            {
                throw new UpdateException("更新数据时产生未知异常");
            }

        }
    }

    @Override
    public List<CartDto> getDTOByUid(Integer uid) {
        LambdaQueryWrapper<Cart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUid,uid).orderByDesc(Cart::getModifiedTime);
        List<Cart> cartList = cartMapper.selectList(queryWrapper);

        List<CartDto> cartDtos = cartList.stream().map((item) -> {
            CartDto cartDto = new CartDto();

            BeanUtils.copyProperties(item, cartDto);
            Product product = productMapper.selectById(item.getPid());
            cartDto.setRealPrice(product.getPrice());
            cartDto.setImage(product.getImage());
            cartDto.setTitle(product.getTitle());
            return cartDto;
        }).collect(Collectors.toList());

        return cartDtos;
    }

    @Override
    public Integer addNum(Integer cid, Integer uid, String username) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getCid,cid);
        Cart result = cartMapper.selectOne(lambdaQueryWrapper);
        if(result==null)
        {
            throw new CartNotFoundException("数据不存在");
        }
        if(!result.getUid().equals(uid))
        {
            throw new AccessDeniedException("数据非法访问");
        }

        Integer num=result.getNum()+1;
        Cart cart=new Cart();
        LambdaQueryWrapper<Cart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getCid,cid);

        cart.setNum(num);
        cart.setModifiedTime(new Date());
        cart.setModifiedUser(username);
        cartMapper.update(cart,queryWrapper);
        return num;
    }

    @Override
    public List<CartDto> getDTOByCid(Integer uid,Integer[] cids) {
        LambdaQueryWrapper<Cart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Cart::getCid,cids).orderByDesc(Cart::getModifiedTime);
        List<Cart> carts = cartMapper.selectList(queryWrapper);
        for(Cart cart:carts)
        {
            if(!cart.getUid().equals(uid))
            {
                carts.remove(cart);
            }
        }

        List<CartDto> cartDtos = carts.stream().map((item) -> {
            CartDto cartDto = new CartDto();

            BeanUtils.copyProperties(item, cartDto);
            Product product = productMapper.selectById(item.getPid());
            cartDto.setRealPrice(product.getPrice());
            cartDto.setImage(product.getImage());
            cartDto.setTitle(product.getTitle());
            return cartDto;
        }).collect(Collectors.toList());
        return cartDtos;
    }
}
