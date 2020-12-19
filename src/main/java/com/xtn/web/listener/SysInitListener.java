package com.xtn.web.listener;

import com.xtn.service.DicService;
import com.xtn.service.impl.DicServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 服务器启动的监听器
 * 时间：2020年12月16日 12:43:22
 */
public class SysInitListener implements ServletContextListener {
    /*
    * 系统启动时调用
    * 用来创建上下文域对象(全局作用域对象)
    * */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //获取上下文域对象
        ServletContext application = servletContextEvent.getServletContext();
        //获取数据字典业务层对象
        //DicService dicService = new DicServiceImpl();  //普通的service注入会报空指针异常,采用下面的方法，先在spring容器中注册声明dicService对象
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
        DicService dicService = (DicService) applicationContext.getBean("dicService");

        //获取数据字典
        //数据字典应该根据类型不同保存为7个list对象（appellation,clueState,returnPriority,returnState,source,stage,transactionType）
        //业务层打包成map:[appellation:list1,clueState:list2,...]
        Map<String , List> map = dicService.getMap();

        //将map解析为上下文域对象的键值对并保存
        Set<String> set = map.keySet();
        for (String key:set) {
            application.setAttribute(key,map.get(key));
        }

        //-------------------------------------------------------------------------------

        /**
         * 处理Stage2Possibility.properties资源文件
         * 将文件中的键值对关系解析为java中的键值对的关系
         * ("01资质审查",10)
         * ("02需求分析",10)
         * ...
         */
        Map<String , String> map2 = new HashMap<>();
        //解析properties
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> em = bundle.getKeys();
        while (em.hasMoreElements()){
            //阶段
            String key = em.nextElement();
            //可能性
            String value = bundle.getString(key);

            map2.put(key,value);
        }

        //将map2保存到服务器缓存中
        application.setAttribute("pMap",map2);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
