package com.cy.store.controller;

import com.cy.store.entity.District;
import com.cy.store.service.DistrictService;
import com.cy.store.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.cy.store.controller.BaseController.OK;

@RequestMapping("districts")
@RestController
public class DistrictController {

    @Autowired
    private DistrictService districtService;


    //districts开头的请求都被拦截到getByParent方法
    @RequestMapping({"/",""})
    public JsonResult<List<District>> getByParent(String parent)
    {
        List<District> data=districtService.getByParent(parent);
        return new JsonResult<>(OK,data);
    }

}
