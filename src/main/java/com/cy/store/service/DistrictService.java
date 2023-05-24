package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.entity.District;

import java.util.List;

public interface DistrictService extends IService<District> {

    /**
     * 根据父编号找到结果集
     *
     * @param parent
     **/
    List<District> getByParent(String parent);

    /**
     * 根据code的值获取名称
     *
     * @param code
     **/
    String getNameByCode(String code);
}
