package com.xtn.dao;

import org.apache.ibatis.annotations.Param;

public interface ActivityRemarkDao {

    //根据活动id（activityId）查询记录数
    public int getActivityRemark(String[] activityId);

    //根据活动id（activityId）删除备注记录
    public int deleteActivityRemark(String[] activityId);
}
