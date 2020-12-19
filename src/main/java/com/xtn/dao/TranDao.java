package com.xtn.dao;

import com.xtn.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranDao {

    //添加交易
    int saveTransaction(Tran tran);

    //查询交易信息
    List<Tran> selectTranList(Tran tran);

    //根据id获取交易详细信息
    Tran selectTran(@Param("id") String id);
}
