package com.cy.store.controller;


import com.cy.store.controller.ex.*;
import com.cy.store.entity.TUser;
import com.cy.store.service.TUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDupicatedException;
import com.cy.store.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class TUserController extends BaseController{

    @Autowired
    private TUserService tUserService;

    @RequestMapping("reg")
    public JsonResult<Void> reg(TUser tUser)
    {
        //创建响应结果对象
        tUserService.reg(tUser);
        return new JsonResult<>(OK);
    }


    @RequestMapping("login")
    public JsonResult<TUser> login(HttpSession httpSession,String username, String password) {
        TUser data = tUserService.login(username, password);
        //向session对象中完成数据的绑定(session全局的)
        /**
         * @param request.getSession()可以帮你得到HttpSession类型的对象
         * @param session.setAttribute("key",value)；是session设置值的方法
         * 原理同java中的HashMap的键值对，意思也就是key现在为“user”；存放的值为userName，userName应该为一个String类型的变量吧？看你自己的定义。
         * @param 可以使用session.getAttribute("key");来取值，意味着你能得到userName的值
         **/
        httpSession.setAttribute("uid",data.getUid());
        httpSession.setAttribute("username",data.getUsername());
        return new JsonResult<TUser>(OK, data);
    }


    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,String newPassword,HttpSession session)
    {
        Integer uid=getuidFromSession(session);
        String username=getUsernameFromSession(session);

        tUserService.changePassword(uid,username,oldPassword,newPassword);
        return new JsonResult<Void>(OK);
    }

    @RequestMapping("get_by_uid")
    public JsonResult<TUser> getByUid(HttpSession session){
        TUser user = tUserService.getByUid(getuidFromSession(session));
        return new JsonResult<TUser>(OK,user);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(TUser tUser,HttpSession session)
    {
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        tUserService.changeInfo(uid,username,tUser);
        return new JsonResult<>(OK);
    }

    //设置文件上传的最大值
    public static final int AVATAR_MAX_SIZE=10*1024*1024;

    //限制上传文件的类型
    public static final List<String> AVATAR_TYPE=new ArrayList<>();

    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }
    /**
     * MultipartFile接口是SpringMVC提供的一个接口，这个接口为我们包装了获取文件类型的数据
     * 只需在处理请求的方法参数列表上声明一个参数类型为MultipartFile，
     * 然后SpringBoot自动将传递给服务的文件数据赋值给这个参数
     * @param session
     * @param file
     * @RequestParam("file") 表示请求中的参数（也就是前端页面中提交的参数），将请求中的参数注入请求处理方法的某个参数上，如果
     * 名称不一致则可以使用@RequestParam注解进行标记和映射
     **/
    @Value("${reggie.path}")
    private String basePath;
    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session, @RequestParam("file") MultipartFile file)
    {
        //判断文件是否为null
        if(file.isEmpty())
        {
            throw new FileEmptyException("文件为空");
        }
        if(file.getSize()>AVATAR_MAX_SIZE){
            throw new FileSizeException("文件超出限制");
        }
        //判断文件类型是否为我们规定的后缀类型
        String contentType = file.getContentType();
        if(!AVATAR_TYPE.contains(contentType)){
            throw new FileTypeException("文件类型不支持");
        }

        //File对象指向这个路径，File是否存在
        File dir=new File(basePath);
        if(!dir.exists())
        {
            dir.mkdirs();//创建当前目录
        }

        //获取这个文件名称,UUID工具来生成一个新的字符串
        String originalFilename=file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//截取的.之后的后缀

        String fileName=UUID.randomUUID().toString().toUpperCase()+suffix;

        //由于此时的file生成的是临时文件，则此时需要通过transferTo进行文件的转存
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer uid=getuidFromSession(session);
        String username=getUsernameFromSession(session);
        //返回头像的路径给前端页面，用于头像的显示
        String avatar="/upload/"+fileName;
        tUserService.changeAvatar(uid,avatar,username);
        return new JsonResult<>(OK,avatar);
    }
}
