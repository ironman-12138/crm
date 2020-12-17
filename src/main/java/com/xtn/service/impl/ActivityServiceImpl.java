package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ActivityDao;
import com.xtn.dao.ActivityRemarkDao;
import com.xtn.domain.Activity;
import com.xtn.domain.ActivityRemark;
import com.xtn.exception.TransactionException;
import com.xtn.service.ActivityService;
import com.xtn.service.UserService;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    public ActivityDao activityDao;
    @Resource
    public ActivityRemarkDao activityRemarkDao;
    @Resource
    public UserService userService;

    @Override
    public boolean saveUserActivity(Activity activity) {
        boolean flag = false;
        int count = activityDao.saveUserActivity(activity);
        if (count != 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public PaginationVo selectActivityList(Map map) {
        Activity a = (Activity) map.get("activity");
        PaginationVo<Activity> vo = new PaginationVo<>();
        Integer pageNum = Integer.parseInt((String) map.get("pageNum"));
        Integer pageSize = Integer.parseInt((String) map.get("pageSize"));

        //pageNum:查询的页数，pageSize:一页显示的数量
        PageHelper.startPage(pageNum,pageSize);
        List<Activity> activityList = activityDao.selectActivityList(a);

        //获取总记录数pageInfo.getTotal()
        PageInfo<Activity> pageInfo = new PageInfo<>(activityList);
        vo.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        vo.setDataList(activityList);
        return vo;
    }

    @Override
    public boolean deleteActivity(String[] id) {
        System.out.println("deleteActivity执行了");
        int count = 0;
        boolean flag = false;

        //先要删除掉市场活动备注表中与id所在记录相关联的记录
        //查询出要删除的备注数
        int count1 = activityRemarkDao.getActivityRemark(id);
        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteActivityRemark(id);
        if (count1 != count2){
            flag = false;
        }else {
            throw new TransactionException("要删除的备注数量不对");
        }

        count = activityDao.deleteActivity(id);
        System.out.println("count = " + count);
        System.out.println("id.length = " + id.length);
        if (count == id.length){
            //所有删除语句都执行成功
            flag = true;
        }else {
            throw new TransactionException("没有删除成功所有要删除的市场活动");
        }
        return flag;
    }

    @Override
    public Map<String,Object> selectUserActivity(String id) {
        Activity activity = activityDao.selectUserActivity(id);
        List uList = userService.selectUserList();
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",activity);
        return map;
    }

    @Override
    public boolean updateUserActivity(Activity activity) {
        boolean flag = false;
        int count = activityDao.updateUserActivity(activity);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public Activity selectActivity(String id) {
        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListByAId(String activityId) {
        List<ActivityRemark> list = activityRemarkDao.getRemarkListByAId(activityId);
        return list;
    }

    @Override
    public boolean deleteRemarkById(String id) {
        boolean flag = false;
        int result = activityRemarkDao.deleteRemarkById(id);
        if (result == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean saveActivityRemark(ActivityRemark ar) {
        boolean flag = false;
        int count = activityRemarkDao.saveActivityRemark(ar);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateActivityRemark(ActivityRemark ar) {
        boolean flag = false;
        int count = activityRemarkDao.updateActivityRemark(ar);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    //根据线索id获取相关市场活动信息
    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return activityDao.getActivityListByClueId(clueId);
    }

    //根据活动名name查询市场活动信息排除已关联的市场活动
    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String name,String clueId) {
        return activityDao.getActivityListByNameAndNotByClueId(name, clueId);
    }
}
