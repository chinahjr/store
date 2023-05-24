package com.cy.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.entity.Product;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.ProductService;
import com.cy.store.service.ex.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findHotList() {
        LambdaQueryWrapper<Product> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus,1);
        queryWrapper.orderByDesc(Product::getPriority);
        queryWrapper.last("limit 0,4");

        List<Product> products = productMapper.selectList(queryWrapper);
        return products;
    }

    @Override
    public Product findById(Integer id) {
        Product product = productMapper.selectById(id);
        if(product==null)
        {
            throw new ProductNotFoundException("尝试访问的商品数据不存在");
        }


        return product;
    }
}
