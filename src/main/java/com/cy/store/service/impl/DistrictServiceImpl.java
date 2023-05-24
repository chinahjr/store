package com.cy.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.entity.District;
import com.cy.store.mapper.DistrictMapper;
import com.cy.store.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl extends ServiceImpl<DistrictMapper, District> implements DistrictService {

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<District> getByParent(String parent) {

        LambdaQueryWrapper<District> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(District::getParent,parent);
        List<District> districts = districtMapper.selectList(queryWrapper);
        for (District district:districts)
        {
            district.setId(null);
            district.setParent(null);
        }
        return districts;
    }

    @Override
    public String getNameByCode(String code) {
        LambdaQueryWrapper<District> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(District::getCode,code);
        District district = districtMapper.selectOne(queryWrapper);
        String name = district.getName();
        return name;
    }
}
