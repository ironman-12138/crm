package com.xtn.service.impl;

import com.xtn.dao.UserDao;
import com.xtn.domain.User;
import com.xtn.exception.LoginException;
import com.xtn.service.UserService;
import com.xtn.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User selectByNameAndPassword(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = null;
        Map<String ,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        user = userDao.selectByNameAndPassword(map);
        //验证是否存在用户
        if (user == null){
            throw new LoginException("用户名密码错误");
        }

        //验证失效时间
        String expireTime = user.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(sysTime) < 0){
            //账号失效
            throw new LoginException("账号已失效");
        }

        //验证是否锁定
        if ("0".equals(user.getLockState())){
            //被锁定
            throw new LoginException("账号已被锁定，请联系管理员");
        }

        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("IP地址受限");
        }

        //验证ip地址
        return user;
    }

    @Override
    public List<User> selectUserList() {
        List<User> userList = new ArrayList<>();
        userList = userDao.selectUserList();
        return userList;
    }
}
