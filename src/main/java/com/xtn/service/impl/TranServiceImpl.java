package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ContactsDao;
import com.xtn.dao.CustomerDao;
import com.xtn.dao.TranDao;
import com.xtn.dao.TranHistoryDao;
import com.xtn.domain.*;
import com.xtn.exception.TransactionException;
import com.xtn.service.TranService;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 交易业务层实现类
 * 时间：2020年12月19日 10:37:43
 */
@Service
public class TranServiceImpl implements TranService {
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    //获取联系人列表信息
    @Override
    public List<Contacts> getContactsListByName(String name) {
        return contactsDao.getContactsListByName(name);
    }

    //添加交易
    @Override
    public boolean saveTran(String customerName, Tran tran) {
        boolean flag = true;
        //根据客户名获取客户id
        Customer customer = customerDao.selectCustomerByName(customerName);
        if (customer == null){
            //客户表中没有该客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            //添加客户
            int c1 = customerDao.saveCustomer(customer);
            if (c1 != 1){
                flag =false;
                throw new TransactionException("客户添加失败");
            }
        }

        //将客户id封装入交易对象中
        tran.setCustomerId(customer.getId());

        //添加交易
        int c2 = tranDao.saveTransaction(tran);
        if (c2 != 1){
            flag =false;
            throw new TransactionException("交易添加失败");
        }

        //添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        int c3 = tranHistoryDao.saveTranHistory(tranHistory);
        if (c3 != 1){
            flag =false;
            throw new TransactionException("交易历史添加失败");
        }

        return flag;
    }

    //分页查询获取交易列表
    @Override
    public PaginationVo<Tran> selectTranList(Map<String, Object> map) {
        Tran tran = (Tran) map.get("tran");
        PaginationVo<Tran> vo = new PaginationVo<>();
        Integer pageNum = Integer.parseInt((String) map.get("pageNum"));
        Integer pageSize = Integer.parseInt((String) map.get("pageSize"));

        //pageNum:查询的页数，pageSize:一页显示的数量
        PageHelper.startPage(pageNum,pageSize);
        List<Tran> tranList = tranDao.selectTranList(tran);

        //获取总记录数pageInfo.getTotal()
        PageInfo<Tran> pageInfo = new PageInfo<>(tranList);
        vo.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        vo.setDataList(tranList);
        return vo;
    }

    //根据id获取交易详细信息
    @Override
    public Tran selectTran(String id) {
        return tranDao.selectTran(id);
    }

    //根据交易id获取交易历史列表
    @Override
    public List<TranHistory> selectTranHistoryList(String id) {
        return tranHistoryDao.selectTranHistoryList(id);
    }
}
