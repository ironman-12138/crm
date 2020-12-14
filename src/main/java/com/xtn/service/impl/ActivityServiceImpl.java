package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ActivityDao;
import com.xtn.dao.ActivityRemarkDao;
import com.xtn.domain.Activity;
import com.xtn.service.ActivityService;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    public ActivityDao activityDao;
    @Resource
    public ActivityRemarkDao activityRemarkDao;

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
        System.out.println("count1 = " + count1);
        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteActivityRemark(id);
        System.out.println("count2 = " + count2);
        if (count1 != count2){
            flag = false;
        }

        count = activityDao.deleteActivity(id);
        System.out.println("count = " + count);
        System.out.println("id.length = " + id.length);
        if (count == id.length){
            //所有删除语句都执行成功
            flag = true;
        }
        return flag;
    }
}
