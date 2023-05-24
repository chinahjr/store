package com.cy.store.controller;

import com.cy.store.dto.CartDto;
import com.cy.store.service.CartService;
import com.cy.store.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("carts")
@RestController
public class CartController extends BaseController{

    @Autowired
    private CartService cartService;

    @RequestMapping("add_to_cart")
    public JsonResult<Void> addToCart(Integer pid, Integer amount, HttpSession session)
    {
        cartService.addToCart(getuidFromSession(session),pid,amount,getUsernameFromSession(session));
        return new JsonResult<>(OK);
    }

    @RequestMapping({"","/"})
    public JsonResult<List<CartDto>> getDTOByUid(HttpSession session)
    {
        List<CartDto> data=cartService.getDTOByUid(getuidFromSession(session));
        return new JsonResult<>(OK,data);
    }

    @RequestMapping("{cid}/num/add")
    public JsonResult<Integer> addNum(@PathVariable("cid") Integer cid, HttpSession session)
    {
        Integer data = cartService.addNum(cid, getuidFromSession(session), getUsernameFromSession(session));
        return new JsonResult<>(OK,data);
    }

    @RequestMapping("list")
    public JsonResult<List<CartDto>> getDTOByCid(Integer[] cids,HttpSession session)
    {
        List<CartDto> data = cartService.getDTOByCid(getuidFromSession(session), cids);
        return new JsonResult<>(OK,data);
    }
}
