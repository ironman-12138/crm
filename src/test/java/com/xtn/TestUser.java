package com.xtn;

import com.xtn.dao.DicTypeDao;
import com.xtn.dao.UserDao;
import com.xtn.service.ActivityService;
import com.xtn.service.DicService;
import com.xtn.service.impl.ActivityServiceImpl;
import com.xtn.service.impl.DicServiceImpl;
import com.xtn.utils.DateTimeUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

public class TestUser {

    //验证失效时间
    @Test
    public void TestExpireTime(){
        String expireTime = "2020-12-13 19:00:00";

        String sysTime = DateTimeUtil.getSysTime();
        System.out.println(sysTime);

        //字符串比较
        int count = expireTime.compareTo(sysTime);
        System.out.println(count);
    }

    //验证锁定时间
    @Test
    public void TestLockState(){
        String lock = "0";
        if ("0".equals(lock)){
            System.out.println("账号被锁定");
        }
    }

    //验证ip地址
    @Test
    public void TestAllowIps(){
        String ip = "127.0.0.2";
        String allowIps = "127.0.0.1,198.12.1.1";
        if (allowIps.contains(ip)){
            System.out.println("ip允许访问");
        }else {
            System.out.println("当前ip地址不允许访问，请联系管理员");
        }
    }

    //测试批量删除市场活动业务
    @Test
    public void TestDeleteActivity(){
        //测试前先去applicationContext.xml声明service对象
        String config = "applicationContext.xml";
        ApplicationContext ac = new ClassPathXmlApplicationContext(config);

        ActivityService service = (ActivityService) ac.getBean("activityService");
        String[] id = {"0a3a3173a6344db49f90567ae0d86505","0a3a3173a6344db49f90567ae0d86505"};
        boolean flag = service.deleteActivity(id);
        System.out.println("结果===>"+flag);
    }
}
