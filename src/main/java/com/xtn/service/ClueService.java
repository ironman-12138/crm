package com.xtn.service;

import com.xtn.domain.Activity;
import com.xtn.domain.Clue;
import com.xtn.domain.ClueActivityRelation;
import com.xtn.vo.PaginationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 线索业务层接口
 * 时间：2020年12月16日 11:22:50
 */
public interface ClueService {

    //分页查询线索信息
    PaginationVo<Clue> selectClueList(Map<String, Object> map);

    //保存线索信息
    public boolean saveClue(Clue clue);

    //根据id查询线索信息
    Clue selectClueById(String id);

    //解除线索和市场活动的关联
    boolean disconnectById(@Param("id") String id);

    //关联线索和市场活动信息
    boolean contactClueAndActivity(String clueId,String[] activityId);
}
