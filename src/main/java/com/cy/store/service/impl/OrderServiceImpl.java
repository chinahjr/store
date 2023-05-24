package com.cy.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.dto.CartDto;
import com.cy.store.entity.Address;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;
import com.cy.store.entity.Product;
import com.cy.store.mapper.OrderMapper;
import com.cy.store.service.AddressService;
import com.cy.store.service.CartService;
import com.cy.store.service.OrderItemService;
import com.cy.store.service.OrderService;
import com.cy.store.service.ex.InsertException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public Order create(Integer aid, Integer uid, String username, Integer[] cids) {

        List<CartDto> list = cartService.getDTOByCid(uid, cids);
        Long totalPrice=0L;
        for(CartDto cartDto:list)
        {
            totalPrice+=cartDto.getRealPrice()* cartDto.getNum();
        }

        Address address = addressService.getByAid(aid, uid);
        Order order = new Order();
        order.setUid(uid);
        order.setRecvName(address.getName());
        order.setRecvPhone(address.getPhone());
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvArea(address.getAreaName());
        order.setRecvAddress(address.getAddress());

        order.setStatus(0);
        order.setTotalPrice(totalPrice);
        order.setOrderTime(new Date());

        order.setCreatedUser(username);
        order.setCreatedTime(new Date());
        order.setModifiedUser(username);
        order.setModifiedTime(new Date());

        int rows = orderMapper.insert(order);
        if(rows!=1)
        {
            throw new InsertException("插入数据异常");
        }

        for(CartDto cartDto:list)
        {
            OrderItem orderItem=new OrderItem();
            orderItem.setOid(order.getOid());
            orderItem.setPid(cartDto.getPid());
            orderItem.setTitle(cartDto.getTitle());
            orderItem.setImage(cartDto.getTitle());
            orderItem.setPrice(cartDto.getRealPrice());
            orderItem.setNum(cartDto.getNum());

            orderItem.setCreatedUser(username);
            orderItem.setCreatedTime(new Date());
            orderItem.setModifiedUser(username);
            orderItem.setModifiedTime(new Date());

            boolean save = orderItemService.save(orderItem);
            if(save==false)
            {
                throw new InsertException("插入数据异常");
            }
        }
        return order;
    }
}
