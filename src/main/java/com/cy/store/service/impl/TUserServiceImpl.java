package com.cy.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.store.entity.TUser;
import com.cy.store.mapper.TUserMapper;
import com.cy.store.service.TUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.PasswordNotMatchException;
import com.cy.store.service.ex.UserNotFoundException;
import com.cy.store.service.ex.UsernameDupicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService{

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public void reg(TUser tUser) {

        String username = tUser.getUsername();
        //1、判断用户是否被注册
        LambdaQueryWrapper<TUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TUser::getUsername,username);
        TUser user = tUserMapper.selectOne(queryWrapper);

        //2、判断结果是否为null，如果不为null则抛出用户名被占用的异常
        if(user!=null)
        {
            //抛出异常
            throw new UsernameDupicatedException("用户名被占用");
        }

        //3、密码加密实现
        String oldPassword=tUser.getPassword();
        //获取盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        //将密码与盐值作为一个整体进行加密
        tUser.setSalt(salt);
        String password = null;
        for(int i=0;i<3;i++)
        {
            password = DigestUtils.md5DigestAsHex((salt + oldPassword + salt).getBytes()).toUpperCase();
        }
        tUser .setPassword(password);

        //3、补全数据：is_delete设置成0
        tUser.setIsDelete(0);

        //4、补全数据，4个日志字段信息
        tUser.setCreatedUser(tUser.getUsername());
        tUser.setModifiedUser(tUser.getUsername());
        Date date=new Date();
        tUser.setCreatedTime(date);
        tUser.setModifiedTime(date);


        //5、执行注册业务功能实现
        int row = tUserMapper.insert(tUser);

        //6、可以抛出业务注册异常
        if(row!=1)
        {
            throw new InsertException("在用户注册中产生未知异常");
        }
    }


    @Override
    public TUser login(String username, String password) {
        //根据用户名称查询用户的数据是否存在，如果不在就抛出异常
        LambdaQueryWrapper<TUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TUser::getUsername,username);
        TUser user = tUserMapper.selectOne(queryWrapper);

        if(user==null)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        String salt=user.getSalt();
        String newMd5password=null;
        for(int i=0;i<3;i++)
        {
            newMd5password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }

        if(!newMd5password.equals(user.getPassword()))
        {
            throw new PasswordNotMatchException("用户密码错误");
        }

        if(user.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        //登录成功，将员工id存入Session并返回登录成功结果

        TUser user1=new TUser();
        user1.setUid(user.getUid());
        user1.setUsername(user.getUsername());
        user1.setAvatar(user.getAvatar());
        return user1;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldpassword, String newpassword) {

        TUser tUser = tUserMapper.selectById(uid);
        if(tUser==null || tUser.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }
        if(tUser==null || tUser.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        //原始密码与数据库中密码进行比较
        String Md5oldpassword=null;
        for(int i=0;i<3;i++)
        {
            Md5oldpassword = DigestUtils.md5DigestAsHex((tUser.getSalt() + oldpassword + tUser.getSalt()).getBytes()).toUpperCase();
        }
        if(!Md5oldpassword.equals(tUser.getPassword()))
        {
            throw new PasswordNotMatchException("密码错误");
        }

        //将新的密码进行加密,再进行更新
        String Md5newpassword=null;
        for(int i=0;i<3;i++)
        {
            Md5newpassword = DigestUtils.md5DigestAsHex((tUser.getSalt() + newpassword + tUser.getSalt()).getBytes()).toUpperCase();
        }
        tUser.setPassword(Md5newpassword);
        tUser.setModifiedUser(username);
        tUser.setModifiedTime(new Date());

        //执行查询
        tUserMapper.updateById(tUser);
    }

    @Override
    public TUser getByUid(Integer uid) {
        TUser result = tUserMapper.selectById(uid);
        if(result==null || result.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        TUser user=new TUser();
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender((result.getGender()));
        return user;
    }


    @Override
    public void changeInfo(Integer uid, String username, TUser tUser) {
        LambdaQueryWrapper<TUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TUser::getUid,uid);
        TUser result = tUserMapper.selectOne(queryWrapper);
        if(result==null || result.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        tUser.setUid(uid);
        tUser.setModifiedUser(username);
        tUser.setModifiedTime(new Date());

        tUserMapper.updateById(tUser);
    }

    @Override
    public void changeAvatar(Integer uid, String avatar, String username) {
        //查询当前的用户数据是否存在
        TUser result = tUserMapper.selectById(uid);
        if(result==null || result.getIsDelete()==1)
        {
            throw new UserNotFoundException("用户数据不存在");
        }

        result.setAvatar(avatar);
        result.setModifiedUser(username);
        result.setModifiedTime(new Date());
        tUserMapper.updateById(result);
    }
}
