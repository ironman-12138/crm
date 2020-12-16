package com.xtn.service.impl;

import com.xtn.dao.DicTypeDao;
import com.xtn.dao.DicValueDao;
import com.xtn.domain.DicType;
import com.xtn.domain.DicValue;
import com.xtn.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    //获取数据字典的map集合(map:[appellation:list1,clueState:list2,...])
    @Override
    public Map<String, List> getMap() {
        Map<String, List> map = new HashMap<>();
        //获取数据字典类集合
        List<DicType> tList = dicTypeDao.getTypeList();
        List<DicValue> vList = null;
        for (DicType t:tList) {
            //根据每一个数据字典类型获取相应数据字典值的集合
            vList = dicValueDao.getDicValueList(t.getCode());
            //存入map集合
            map.put(t.getCode(),vList);
        }
        return map;
    }
}
