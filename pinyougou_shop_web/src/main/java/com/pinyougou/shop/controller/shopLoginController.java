package com.pinyougou.shop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class shopLoginController {



    //获取商家登录用户名
    @RequestMapping("/getUserInfo")
    public Map getSellerInfo(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Map map=new HashMap();
        map.put("loginName", name);
        return map ;
    }

}
