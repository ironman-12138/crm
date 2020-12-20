package com.xtn.service;

import java.util.List;
import java.util.Map;

public interface DicService {

    //获取数据字典的map集合(map:[appellation:list1,clueState:list2,...])
    Map<String, List> getMap();

    //获取阶段集合
    List<String> getStage();
}
