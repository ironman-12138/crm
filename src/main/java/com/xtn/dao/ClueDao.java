package com.xtn.dao;

import com.xtn.domain.Clue;
import com.xtn.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {

	//根据条件查询线索信息返回List集合
    public List<Clue> selectClueList(Clue clue);

    //保存线索信息
    public int saveClue(Clue clue);

    //根据id查询线索信息
    Clue selectClueById(@Param("id") String id);

    //删除线索
    int deleteClue(@Param("id") String clueId);
}
