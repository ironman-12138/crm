package com.xtn.dao;

import com.xtn.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranDao {

    //添加交易
    int saveTransaction(Tran tran);

    //查询交易信息
    List<Tran> selectTranList(Tran tran);

    //根据id获取交易详细信息
    Tran selectTran(@Param("id") String id);

    //改变交易阶段
    int changeStage(Tran t);

    //获取交易数量
    int getTotal();

    //获取交易阶段及其数量
    List<Map<String, Object>> getCharts();
}
