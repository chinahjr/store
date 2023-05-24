package com.cy.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.store.entity.TUser;

import javax.servlet.http.HttpServletRequest;

public interface TUserService extends IService<TUser> {

    /**
     * 用户注册
     *
     **/
    void reg(TUser tUser);
    
    /**
     * 用户登录功能
     *
 * @param username
 * @param password
     * @return 当前匹配的用户数据，如果没有则返回null
     **/
    TUser login(String username, String password);

    /**
     * 用户修改密码
     *
 * @param uid
 * @param username
 * @param oldpassword
 * @param newpassword
     **/
    void changePassword(Integer uid,String username,String oldpassword,String newpassword);
    

    /**
     * 根据用户id查询用户数据
     *
 * @param uid
     **/
    TUser getByUid(Integer uid);
    
    /**
     * 更新用户的数据的操作
     *
 * @param uid
 * @param username
 * @param tUser
     **/
    void changeInfo(Integer uid,String username,TUser tUser);


    /**
     * 用户头像的更改
     *
 * @param uid
 * @param avatar
 * @param username
     **/
    void changeAvatar(Integer uid,String avatar,String username);
}
