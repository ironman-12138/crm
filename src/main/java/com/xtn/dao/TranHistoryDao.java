package com.xtn.dao;

import com.xtn.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    //添加历史交易信息
    int saveTranHistory(TranHistory th);

    //根据交易id获取交易历史列表
    List<TranHistory> selectTranHistoryList(String id);
}
