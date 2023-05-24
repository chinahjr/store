package com.cy.store.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_cart")
public class Cart extends BaseEntity{
    private Integer cid;
    private Integer uid;
    private Integer pid;
    private Long price;
    private Integer num;

}
