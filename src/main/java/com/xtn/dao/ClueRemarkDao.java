package com.xtn.dao;

import com.xtn.domain.ClueRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueRemarkDao {

    //根据线索id获取线索备注
    List<ClueRemark> selectClueRemarkByClueId(@Param("clueId") String clueId);

    //根据线索id删除线索备注
    int deleteClueRemark(@Param("id") String id);
}
