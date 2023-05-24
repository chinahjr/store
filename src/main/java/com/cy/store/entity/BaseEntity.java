package com.cy.store.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/*实体类的基类，用于保存公共字段,
当其他实体类继承基类时，就会拥有基类当中的公共字段
* 序列化
* */
@Data
public class BaseEntity implements Serializable {
    //创建人
    private String createdUser;
    //创建时间
    private Date createdTime;
    //最后修改人
    private String modifiedUser;
    //最后修改时间
    private Date modifiedTime;

    public BaseEntity() {
    }

    public BaseEntity(String createUser, Date createTime, String modifiedUser, Date modifiedTime) {
        this.createdUser = createUser;
        this.createdTime = createTime;
        this.modifiedUser = modifiedUser;
        this.modifiedTime = modifiedTime;
    }

}
