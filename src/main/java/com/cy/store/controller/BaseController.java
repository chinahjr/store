package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.utils.JsonResult;
import org.apache.ibatis.annotations.Insert;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/**
 * 控制层类的基类
 *
 **/
public class BaseController {
    /** 操作成功的状态码 */
    public static final int OK=200;

    //请求处理方法，这个方法返回值要传递给前端的数据
    //当项目中产生了异常，会被统一拦截到此方法
    @ExceptionHandler({ServiceException.class,FileUploadException.class})//用于统一处理抛出的异常
    public JsonResult<Void> handleException(Throwable e)
    {
        JsonResult<Void> result=new JsonResult<>(e);
        if(e instanceof UsernameDupicatedException){
            result.setState(4000);
            result.setMessage("用户名已经被占用");
        }
        else if(e instanceof UserNotFoundException){
            result.setState(4001);
            result.setMessage("用户数据不存在产生未知的异常");
        }
        else if(e instanceof PasswordNotMatchException){
            result.setState(4002);
            result.setMessage("用户名的密码错误的异常");
        }
        else if(e instanceof AddressCountLimitException){
            result.setState(4003);
            result.setMessage("用户收货地址超出上限");
        }
        else if(e instanceof AddressNotFoundException){
            result.setState(4004);
            result.setMessage("用户的收货地址数据不存在的异常");
        }
        else if(e instanceof AccessDeniedException){
            result.setState(4005);
            result.setMessage("收货地址数据非法访问的异常");
        }
        else if(e instanceof ProductNotFoundException){
            result.setState(4006);
            result.setMessage("商品数据不存在");
        }
        else if(e instanceof CartNotFoundException){
            result.setState(4007);
            result.setMessage("购物车数据不存在的异常");
        }
        else if(e instanceof InsertException){
            result.setState(5000);
            result.setMessage("插入数据时产生未知的异常");
        }
        else if(e instanceof UpdateException){
            result.setState(5001);
            result.setMessage("更新数据时产生未知的异常");
        }
        else if(e instanceof FileEmptyException){
            result.setState(6000);
            result.setMessage("文件为空时产生未知的异常");
        }
        else if(e instanceof FileSizeException){
            result.setState(6001);
            result.setMessage("超出文件大小时产生未知的异常");
        }
        else if(e instanceof FileTypeException){
            result.setState(6002);
            result.setMessage("文件类型错误时产生未知的异常");
        }
        else if(e instanceof FileStateException){
            result.setState(6003);
            result.setMessage("上传的文件状态异常时产生未知的异常");
        }
        else if(e instanceof FileUploadIOException){
            result.setState(6004);
            result.setMessage("上传文件时读写异常");
        }
        return result;
    }
    
    /**
     * 获取session对象中的uid
     * @return 当前登录用户的uid的值
 * @param httpSession
     **/
    protected final Integer getuidFromSession(HttpSession httpSession)
    {
        return Integer.valueOf(httpSession.getAttribute("uid").toString());
    }

    /**
     * 获取当前登录用户的username
     *
 * @param session
     **/
    protected final String getUsernameFromSession(HttpSession session)
    {
        return session.getAttribute("username").toString();
    }
}
