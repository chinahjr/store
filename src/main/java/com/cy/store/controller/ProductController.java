package com.cy.store.controller;

import com.cy.store.entity.Product;
import com.cy.store.service.ProductService;
import com.cy.store.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("products")
public class ProductController extends BaseController{

    @Autowired
    private ProductService productService;

    @RequestMapping("hot_list")
    public JsonResult<List<Product>> getHotList()
    {
        List<Product> data=productService.findHotList();
        return new JsonResult<List<Product>>(OK,data);
    }

    @RequestMapping("{id}/details")
    public JsonResult<Product> getById(@PathVariable("id") Integer id)
    {
        Product data = productService.getById(id);
        return new JsonResult<>(OK,data);
    }
}
