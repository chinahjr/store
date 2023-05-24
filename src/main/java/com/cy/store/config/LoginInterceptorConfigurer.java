package com.cy.store.config;

import com.cy.store.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理器拦截器的注册
 *
 **/
@Configuration
public class
LoginInterceptorConfigurer implements WebMvcConfigurer {


    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //创建自定义的拦截器对象
        HandlerInterceptor interceptor=new LoginInterceptor();

        //配置白名单，存放在一个List集合之中
        List<String> arrayList=new ArrayList<>();
        arrayList.add("/static/index.html");
        arrayList.add("/bootstrap3/**");
        arrayList.add("/css/**");
        arrayList.add("/images/**");
        arrayList.add("/js/**");
        arrayList.add("/web/register.html");
        arrayList.add("/web/login.html");
        arrayList.add("/web/index.html");
        arrayList.add("/web/product.html");
        arrayList.add("/users/reg");
        arrayList.add("/users/login");
        arrayList.add("/districts/**");
        //arrayList.

        //完成拦截器的配置
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")//表示要拦截的url是什么
                .excludePathPatterns(arrayList);//表示不需要拦截的（白名单）
    }
}
