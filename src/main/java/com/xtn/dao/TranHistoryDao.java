package com.xtn.dao;

import com.xtn.domain.TranHistory;

public interface TranHistoryDao {

    //添加历史交易信息
    int saveTranHistory(TranHistory th);
}
