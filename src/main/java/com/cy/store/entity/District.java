package com.cy.store.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_dict_district")
public class District{

    private Integer id;
    private String parent;
    private String code;
    private String name;
}
