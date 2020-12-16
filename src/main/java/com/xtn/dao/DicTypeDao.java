package com.xtn.dao;

import com.xtn.domain.DicType;

import java.util.List;

public interface DicTypeDao {

    //获取所有数据字典的类型
    public List<DicType> getTypeList();
}
