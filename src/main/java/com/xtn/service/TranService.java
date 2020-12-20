package com.xtn.service;

import com.xtn.domain.Activity;
import com.xtn.domain.Contacts;
import com.xtn.domain.Tran;
import com.xtn.domain.TranHistory;
import com.xtn.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface TranService {
    //获取联系人列表信息
    List<Contacts> getContactsListByName(String name);

    //添加交易
    boolean saveTran(String customerName, Tran tran);

    //分页查询获取交易列表
    PaginationVo<Tran> selectTranList(Map<String, Object> map);

    //根据id获取交易详细信息
    Tran selectTran(String id);

    //根据交易id获取交易历史列表
    List<TranHistory> selectTranHistoryList(String id);

    //改变交易阶段
    boolean changeStage(Tran t);

    //获取交易阶段和起对应的交易数量
    Map<String, Object> getCharts();
}
