package com.xtn.dao;

import com.xtn.domain.DicValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DicValueDao {
    //根据typeCode获取DicValue集合
    public List<DicValue> getDicValueList(@Param("typeCode") String typeCode);

    //获取阶段集合
    List<String> getStageList(@Param("typeCode") String stage);
}
