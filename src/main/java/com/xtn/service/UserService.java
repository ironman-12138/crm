package com.xtn.service;

import com.xtn.domain.User;
import com.xtn.exception.LoginException;

import java.util.List;

public interface UserService {
    //根据用户名和密码查询用户信息
    public User selectByNameAndPassword(String loginAct, String loginPwd, String ip) throws LoginException;

    //查询所有用户信息
    public List<User> selectUserList();
}
