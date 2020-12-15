package com.xtn.controller;

import com.xtn.domain.Activity;
import com.xtn.domain.ActivityRemark;
import com.xtn.domain.User;
import com.xtn.service.ActivityService;
import com.xtn.service.UserService;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

    //删除市场活动信息
    @RequestMapping(value = "/deleteActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public Object deleteActivity(String[] id){
        boolean flag = false;
        flag = activityService.deleteActivity(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        return map;
    }

    //根据id查询市场活动信息详情和user对象集合
    @RequestMapping(value = "/selectUserActivity.do")
    @ResponseBody
    public Object selectUserActivity(String id){
        //获取当前id的市场活动信息和user对象集合
        Map<String,Object> map = activityService.selectUserActivity(id);
        return map;
    }

    //修改市场活动信息
    @RequestMapping(value = "/updateUserActivity.do")
    @ResponseBody
    public Object updateUserActivity(HttpServletRequest request, Activity activity){
        //修改时间
        activity.setEditTime(DateTimeUtil.getSysTime());
        //修改人
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setEditBy(editBy);
        boolean flag = false;
        flag = activityService.updateUserActivity(activity);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);

        return map;
    }

    //根据id查询市场活动信息详情
    @RequestMapping(value = "/selectActivity.do")
    @ResponseBody
    public Object selectActivity(String id){
        Activity activity = activityService.selectActivity(id);
        return activity;
    }

    //根据id查询市场活动信息备注集合
    @RequestMapping(value = "/getRemarkListByAId.do")
    @ResponseBody
    public Object getRemarkListByAId(String activityId){
        List<ActivityRemark> list = activityService.getRemarkListByAId(activityId);
        return list;
    }

    //根据id删除市场活动信息备注
    @PostMapping(value = "/deleteRemark.do")
    @ResponseBody
    public Object deleteRemarkById(String id){
        boolean flag = false;
        flag = activityService.deleteRemarkById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        return map;
    }

    //根据noteContent和activityId添加活动备注
    @PostMapping(value = "/saveActivityRemark.do")
    @ResponseBody
    public Object saveActivityRemark(HttpServletRequest request,ActivityRemark ar){
        boolean flag = false;
        //添加创建时间
        ar.setCreateTime(DateTimeUtil.getSysTime());
        //添加创建人
        ar.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        //修改状况，0：没有被修改过，1：修改过
        ar.setEditFlag("0");
        //生成id添加
        ar.setId(UUIDUtil.getUUID());
        flag = activityService.saveActivityRemark(ar);

        //向前端返回信息
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("remark",ar);
        return map;
    }

    //根据noteContent和id修改活动备注信息
    @PostMapping(value = "/updateActivityRemark.do")
    @ResponseBody
    public Object updateActivityRemark(HttpServletRequest request,ActivityRemark ar){
        boolean flag = false;
        ar.setEditFlag("1");
        ar.setEditTime(DateTimeUtil.getSysTime());
        ar.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        flag = activityService.updateActivityRemark(ar);

        //向前端返回信息
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("remark",ar);
        return map;
    }
}
