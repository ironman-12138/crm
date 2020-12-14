package com.xtn.controller;

import com.xtn.domain.User;
import com.xtn.service.UserService;
import com.xtn.utils.MD5Util;
import com.xtn.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //验证用户登录方法
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public Object userLogin(HttpServletRequest request, String loginAct, String loginPwd){
        Map<String,Object> map = new HashMap<>();
        //System.out.println("===" + loginAct + "||" + loginPwd + "===");
        //将密码的明文转化为md5密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收ip地址
        String ip = request.getRemoteAddr();
        try {
            User user = userService.selectByNameAndPassword(loginAct,loginPwd,ip);

            //程序执行至此表示验证成功，将用户存入session
            request.getSession().setAttribute("user",user);
            //向浏览器返回结果
            map.put("success",true);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();

            map.put("success",false);
            map.put("msg",msg);
            return map;
        }
    }

    //获取登录者信息的方法
    @RequestMapping(value = "/loginAct.do",method = RequestMethod.POST)
    @ResponseBody
    public Object getLoginAct(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //System.out.println(user);
        return user;
    }

}
