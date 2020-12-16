package com.xtn.service;

import com.xtn.domain.Activity;
import com.xtn.domain.Clue;
import com.xtn.vo.PaginationVo;

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
}
