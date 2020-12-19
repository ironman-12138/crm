package com.xtn.dao;

import com.xtn.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationDao {

    //通过线索id查询线索和市场活动联系列表
    List<ClueActivityRelation> selectClueActivityRelationByClueId(@Param("clueId") String clueId);

    //关联线索和市场活动信息
    int contactClueAndActivity(ClueActivityRelation car);

    //删除线索和市场活动的关联关系
    int deleteClueActivityRelation(@Param("id") String id);

}
