package com.cy.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.AddressService;
import com.cy.store.service.DistrictService;
import com.cy.store.service.ex.AddressCountLimitException;
import com.cy.store.service.ex.AddressNotFoundException;
import com.cy.store.service.ex.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cy.store.service.ex.AccessDeniedException;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private DistrictService districtService;

    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        LambdaQueryWrapper<Address> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUid,uid);
        Integer count = addressMapper.selectCount(queryWrapper);
        if(count>20)
        {
            throw new AddressCountLimitException("插入地址总数超出限制");
        }

        //对address对象中的数据进行补全,也就是多选框中的选择的数据，进行补全
        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String  cityName= districtService.getNameByCode(address.getCityCode());
        String  areaName= districtService.getNameByCode(address.getAreaCode());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);


        address.setUid(uid);
        Integer isDefault=count==0?1:0;
        address.setIsDefault(isDefault);

        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());
        addressMapper.insert(address);
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        LambdaQueryWrapper<Address> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUid,uid);
        List<Address> list=addressMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public void setDefault(Integer uid, Integer aid, String username) {
        LambdaQueryWrapper<Address> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getAid,aid);
        Address result = addressMapper.selectOne(queryWrapper);
        if(result==null)
        {
            throw new AddressNotFoundException("收货地址不存在");
        }

        //检测当前获取到的收货地址数据的归属
        if(!result.getUid().equals(uid)){
            throw new AccessDeniedException("非法数据访问");
        }

        List<Address> list = this.getByUid(uid);
        if(list==null)
        {
            throw new UpdateException("更新数据发生异常");
        }
        for(Address address1:list){
            address1.setIsDefault(1);
            addressMapper.updateById(address1);
        }

        Address address = addressMapper.selectById(aid);
        address.setIsDefault(0);
        address.setModifiedTime(new Date());
        address.setModifiedUser(username);
        addressMapper.updateById(address);
    }

    @Override
    public void delete(Integer aid, Integer uid, String username) {
        LambdaQueryWrapper<Address> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getAid,aid);
        Address result = addressMapper.selectOne(queryWrapper);
        if(result==null)
        {
            throw new AddressNotFoundException("收货地址数据不存在");
        }
        //检测当前获取到的收货地址数据的归属
        if(!result.getUid().equals(uid)){
            throw new AccessDeniedException("非法数据访问");
        }

        addressMapper.delete(queryWrapper);

        LambdaQueryWrapper<Address> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Address::getUid,uid);
        lambdaQueryWrapper.orderByDesc(Address::getModifiedTime).last("limit 0,1");
        Address address = addressMapper.selectOne(lambdaQueryWrapper);
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());
        address.setIsDefault(0);
        int i = addressMapper.updateById(address);
        if(i!=1)
        {
            throw new UpdateException("数据更新异常");
        }
    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address address = addressMapper.selectById(aid);
        if(address==null)
        {
            throw new AddressNotFoundException("收货地址数据不存在");
        }
        if(!address.getUid().equals(uid))
        {
            throw new AccessDeniedException("非法数据方法");
        }

        address.setProvinceCode(null);
        address.setCityCode(null);
        address.setAreaCode(null);
        address.setCreatedUser(null);
        address.setModifiedUser(null);
        address.setModifiedTime(null);
        address.setCreatedTime(null);
        return address;
    }
}
