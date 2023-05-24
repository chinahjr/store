package com.cy.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.store.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {

    /**
     * 根据uid查询当前用户最后一次被修改的收货地址数据
     *
     * @param uid
     **/
    Address findlastModified(Integer uid);
}
