package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ActivityDao;
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
}
