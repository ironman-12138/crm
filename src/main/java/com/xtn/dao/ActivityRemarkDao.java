package com.xtn.dao;

import com.xtn.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDao {

    //根据活动id（activityId）查询记录数
    public int getActivityRemark(String[] activityId);

    //根据活动id（activityId）删除备注记录
    public int deleteActivityRemark(String[] activityId);

    //根据活动id（activityId）获取所有记录
    public List<ActivityRemark> getRemarkListByAId(@Param("activityId") String activityId);

    //根据id删除市场活动信息备注
    public int deleteRemarkById(@Param("id") String id);

    //添加备注信息
    public int saveActivityRemark(ActivityRemark ar);

    //修改备注信息
    public int updateActivityRemark(ActivityRemark ar);
}
