package com.xtn.dao;

import com.xtn.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    //添加用户市场活动信息
    public int saveUserActivity(Activity activity);

    //根据条件查询市场活动信息集合
    public List<Activity> selectActivityList(Activity activity);
}
