package com.xtn.service;

import com.xtn.domain.Activity;
import com.xtn.domain.ActivityRemark;
import com.xtn.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * 市场活动业务层
 * 时间：2020年12月13日 15:38:09
 */
public interface ActivityService {

    //添加用户市场活动信息
    public boolean saveUserActivity(Activity activity);

    //根据条件查询市场活动信息集合
    public PaginationVo selectActivityList(Map map);

    //根据id删除市场活动信息
    public boolean deleteActivity(String[] id);

    //根据id查询市场活动信息详情和user对象集合
    public Map<String,Object> selectUserActivity(String id);

    //修改市场活动信息
    public boolean updateUserActivity(Activity activity);

    //根据id查询市场活动信息详情
    Activity selectActivity(String id);

    //根据活动id（activityId）获取所有记录
    public List<ActivityRemark> getRemarkListByAId(String activityId);

    //根据id删除市场活动信息备注
    public boolean deleteRemarkById(String id);

    //添加活动备注
    public boolean saveActivityRemark(ActivityRemark ar);

    //修改备注信息
    public boolean updateActivityRemark(ActivityRemark ar);

    //根据线索id获取相关市场活动信息
    List<Activity> getActivityListByClueId(String clueId);

    //根据活动名name查询市场活动信息排除已关联的市场活动
    List<Activity> getActivityListByNameAndNotByClueId(String name,String clueId);
}
