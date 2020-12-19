package com.xtn.controller;

import com.xtn.domain.*;
import com.xtn.service.ActivityService;
import com.xtn.service.ClueService;
import com.xtn.service.UserService;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线索控制器
 * 时间：2020年12月16日 11:24:36
 */
@Controller
@RequestMapping("/clue")
public class ClueController {

    @Resource
    private UserService userService;
    @Resource
    private ClueService clueService;
    @Resource
    private ActivityService activityService;

    //获取用户信息
    @GetMapping(value = "/getUserList.do")
    @ResponseBody
    public Object getUserList(){
        List<User> uList = userService.selectUserList();
        return uList;
    }

    //获取当前登录的用户信息
    @GetMapping(value = "/getLoginAct.do")
    @ResponseBody
    public Object getLoginAct(HttpServletRequest request){
        User loginAct = (User) request.getSession().getAttribute("user");
        return loginAct;
    }

    //获取数据字典的数据
    @GetMapping(value = "/getDicTypeValue.do")
    @ResponseBody
    public Object getDicTypeValue(HttpServletRequest request,String[] type){
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        Map<String,List> map = new HashMap<>();
        for (int i = 0; i < type.length; i++) {
            map.put(type[i], (List) application.getAttribute(type[i]));
        }
        return map;
    }

    //分页查询线索信息展示给浏览器
    @GetMapping(value = "/pageList.do")
    @ResponseBody
    public Object pageList(Clue clue,String pageNum,String pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("clue",clue);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        //将vo：{"total":?,"dataList":[{线索1},{2},{3}...]}返回给浏览器
        PaginationVo<Clue> vo = clueService.selectClueList(map);
        return vo;
    }

    //保存线索信息
    @PostMapping(value = "/saveClue.do")
    @ResponseBody
    public Object saveClue(HttpServletRequest request, Clue clue){
        boolean flag = false;
        //添加id
        clue.setId(UUIDUtil.getUUID());
        //添加创建时间
        clue.setCreateTime(DateTimeUtil.getSysTime());
        //添加创建人
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        flag = clueService.saveClue(clue);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        return map;
    }

    //跳转到线索详细信息页
    @GetMapping(value = "/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.selectClueById(id);
        mv.addObject("c",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    //根据线索id获取相关市场活动信息
    @GetMapping(value = "/activityListByClueId.do")
    @ResponseBody
    public Object activityListByClueId(String clueId){
        List<Activity> aList = activityService.getActivityListByClueId(clueId);
        return aList;
    }

    //解除线索和市场活动的关联
    @PostMapping(value = "/disconnect.do")
    @ResponseBody
    public Object disconnect(String id){
        boolean flag = false;
        flag = clueService.disconnectById(id);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        return map;
    }

    //根据活动名name查询市场活动信息排除已关联的市场活动
    @GetMapping(value = "/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public Object getActivityListByNameAndNotByClueId(String name,String clueId){
        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(name,clueId);
        return aList;
    }

    //关联线索和市场活动信息
    @PostMapping(value = "/contactClueAndActivity.do")
    @ResponseBody
    public Object contactClueAndActivity(String clueId,String[] activityId){
        boolean flag = false;
        flag = clueService.contactClueAndActivity(clueId, activityId);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        return map;
    }

    //根据活动名搜索市场活动列表
    @GetMapping(value = "/getActivityListByName.do")
    @ResponseBody
    public Object getActivityListByName(String name){
        List<Activity> aList = activityService.getActivityListByName(name);
        return aList;
    }

    //线索转化为客户，若有交易添加交易记录
    @PostMapping(value = "/convert.do")
    public String convert(HttpServletRequest request, String flag, String clueId, Tran tran){
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        if ("true".equals(flag)){
            //flag为true代表有交易,创建交易
            //添加id
            tran.setId(UUIDUtil.getUUID());
            //添加创建时间
            tran.setCreateTime(DateTimeUtil.getSysTime());
            //添加创建人
            tran.setCreateBy(createBy);
        }

        //执行业务层的方法转换线索
        boolean f = clueService.convert(clueId, tran, createBy);

        //重定向
        if (f){
            return "redirect:/workbench/clue/index.html";
        }else {
            return null;
        }
    }
}
