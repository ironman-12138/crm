package com.xtn.dao;

import com.xtn.domain.Activity;
import com.xtn.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    //添加用户市场活动信息
    public int saveUserActivity(Activity activity);

    //根据条件查询市场活动信息集合
    public List<Activity> selectActivityList(Activity activity);

    //根据id删除市场活动信息
    public int deleteActivity(String[] id);

    //根据id查询市场活动信息详情
    public Activity selectUserActivity(String id);

    //修改市场活动信息
    public int updateUserActivity(Activity activity);

    //根据id查询市场活动信息(owner为user的name)
    public Activity detail(String id);

}
