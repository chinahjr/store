package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {

    //新增收货地址
    void addNewAddress(Integer uid,String username,Address address);

    List<Address> getByUid(Integer uid);


    //修改某个用户的某条收货地址数据为默认地址
    void setDefault(Integer uid,Integer aid,String username);

    //删除用户选中的收货地址
    void delete(Integer aid,Integer uid,String username);

    //根据收货地址id获取收货地址的数据
    Address getByAid(Integer aid,Integer uid);
}
