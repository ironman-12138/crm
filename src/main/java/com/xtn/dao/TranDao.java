package com.xtn.dao;

import com.xtn.domain.Tran;

public interface TranDao {

    //添加交易
    int saveTransaction(Tran tran);
}
