package com.xtn.dao;

import com.xtn.domain.User;

import java.util.List;
import java.util.Map;

/**
 * dao层接口，持久层
 * 时间：2020年12月11日 18:46:41
 */
public interface UserDao {
    //根据用户名和密码查询用户信息
    public User selectByNameAndPassword(Map<String,Object> map);

    //查询所有用户信息
    public List<User> selectUserList();
}
