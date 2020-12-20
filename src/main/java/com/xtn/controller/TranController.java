package com.xtn.controller;

import com.xtn.domain.*;
import com.xtn.service.*;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易控制层
 * 时间：2020年12月19日 10:38:40
 */
@Controller
@RequestMapping("/tran")
public class TranController {
    @Resource
    private UserService userService;
    @Resource
    private TranService tranService;
    @Resource
    private ActivityService activityService;
    @Resource
    private CustomerService customerService;
    @Resource
    private DicService dicService;


    //获取数据字典数据
    @GetMapping(value = "/getDicTypeValue.do")
    @ResponseBody
    public Object getDicTypeValue(HttpServletRequest request, String[] type){
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        Map<String, List> map = new HashMap<>();
        for (int i = 0; i < type.length; i++) {
            map.put(type[i], (List) application.getAttribute(type[i]));
        }
        return map;
    }

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

    //获取市场活动列表信息
    @GetMapping(value = "/getActivityListByName.do")
    @ResponseBody
    public Object getActivityListByName(String name){
        List<Activity> aList = activityService.getActivityListByName(name);
        return aList;
    }

    //获取联系人列表信息
    @GetMapping(value = "/getContactsListByName.do")
    @ResponseBody
    public Object getContactsListByName(String name){
        List<Contacts> cList = tranService.getContactsListByName(name);
        return cList;
    }

    //获取客户名称列表
    @GetMapping(value = "/getCustomerName.do")
    @ResponseBody
    public Object getCustomerName(String name){
        List<String> cList = customerService.getCustomerName(name);
        return cList;
    }

    //获取阶段和可能性关系
    @GetMapping(value = "/getStageAndPossibility.do")
    @ResponseBody
    public Object getStageAndPossibility(HttpServletRequest request){
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        Map<String ,String> map = (Map<String, String>) application.getAttribute("pMap");
        return map;
    }

    //添加交易
    @PostMapping(value = "/saveTran.do")
    public String saveTran(HttpServletRequest request, String customerName, Tran tran){
        //添加id
        tran.setId(UUIDUtil.getUUID());
        //添加创建时间
        tran.setCreateTime(DateTimeUtil.getSysTime());
        //添加创建人
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = tranService.saveTran(customerName,tran);

        if (flag == true){
            //重定向
            return "redirect:/workbench/transaction/index.html";
        }else {
            return "redirect:/workbench/transaction/save.html";
        }
    }

    //分页查询交易信息
    @GetMapping(value = "/pageList.do")
    @ResponseBody
    public Object pageList(Tran tran, String pageNum, String pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("tran",tran);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        //将vo：{"total":?,"dataList":[{交易1},{2},{3}...]}返回给浏览器
        PaginationVo<Tran> vo = tranService.selectTranList(map);
        return vo;
    }

    //根据交易id获取交易详细信息
    @GetMapping(value = "/getTranById.do")
    @ResponseBody
    public Object getTranById(HttpServletRequest request,String id){
        Tran tran = tranService.selectTran(id);
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        //获取阶段和可能性关系集合
        Map<String ,String> map = (Map<String, String>) application.getAttribute("pMap");
        //获取当前阶段对应的可能性
        String possibility = map.get(tran.getStage());
        //将可能性加入交易对象
        tran.setPossibility(possibility);
        return tran;
    }

    //根据交易id获取交易历史列表
    @GetMapping(value = "/getTranHistoryList.do")
    @ResponseBody
    public Object getTranHistoryList(HttpServletRequest request,String id){
        List<TranHistory> th = tranService.selectTranHistoryList(id);
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        //获取阶段和可能性关系集合
        Map<String ,String> map = (Map<String, String>) application.getAttribute("pMap");
        for (TranHistory t:th) {
            //获取当前阶段对应的可能性
            String possibility = map.get(t.getStage());
            //将可能性加入交易历史对象
            t.setPossibility(possibility);
        }
        return th;
    }

    //获取交易阶段集合
    @GetMapping(value = "/getStage.do")
    @ResponseBody
    public Object getStage(HttpServletRequest request){
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        //阶段和可能性的关系对象
        Map<String ,String> map = (Map<String, String>) application.getAttribute("pMap");
        Map<String,Object> m = new HashMap<>();
        m.put("map",map);
        List<String> list = dicService.getStage();
        m.put("stageList",list);
        return m;
    }

    //改变交易阶段
    @GetMapping(value = "/changeStage.do")
    @ResponseBody
    public Object changeStage(HttpServletRequest request,Tran t){
        boolean flag = false;
        //添加修改时间
        t.setEditTime(DateTimeUtil.getSysTime());
        //添加修改人
        t.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        flag = tranService.changeStage(t);
        //获取上下文域对象
        ServletContext application = request.getServletContext();
        //阶段和可能性的关系对象
        Map<String ,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        //获取改变之后阶段的可能性
        String possibility = pMap.get(t.getStage());
        t.setPossibility(possibility);

        //向浏览器返回结果
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);
        return map;
    }

    //获取交易阶段和相应的交易数量
    @GetMapping(value = "/getCharts.do")
    @ResponseBody
    public Object getCharts(){
        Map<String,Object> map = tranService.getCharts();
        return map;
    }
}
