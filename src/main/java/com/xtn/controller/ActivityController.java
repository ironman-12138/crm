package com.xtn.controller;

import com.xtn.domain.Activity;
import com.xtn.domain.User;
import com.xtn.service.ActivityService;
import com.xtn.service.UserService;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动表操作的控制层
 * 时间：2020年12月13日 13:49:41
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    //获取所有用户
    @RequestMapping(value = "/getUserList.do",method = RequestMethod.GET)
    @ResponseBody
    public Object getUserList(){
        List<User> userList = new ArrayList<>();
        userList = userService.selectUserList();
        return userList;
    }

    //保存用户市场活动信息
    @RequestMapping(value = "/saveUserActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public Object saveUserActivity(HttpServletRequest request, Activity activity){
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setCreateBy(createBy);
        boolean flag = false;
        flag = activityService.saveUserActivity(activity);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);

        return map;
    }

    //查询市场活动信息
    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public Object pageList(Activity activity,String pageNum,String pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("activity",activity);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        PaginationVo<Activity> vo = activityService.selectActivityList(map);
        return vo;
    }
}
