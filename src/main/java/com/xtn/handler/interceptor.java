package com.xtn.handler;

import com.xtn.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器
 * 验证用户是否登录
 */
public class interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri=request.getRequestURI();
        if(uri.indexOf("login.do") != -1){
            return true;
        }
        System.out.println("===拦截器执行===");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null){
            return false;
        }
        return true;
    }
}
