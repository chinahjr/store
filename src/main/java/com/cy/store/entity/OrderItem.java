package com.cy.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/** 订单中的商品数据 */
@Data
@TableName("t_order_item")
public class OrderItem extends BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer oid;
    private Integer pid;
    private String title;
    private String image;
    private Long price;
    private Integer num;
}
