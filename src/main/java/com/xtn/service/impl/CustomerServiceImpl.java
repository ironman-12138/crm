package com.xtn.service.impl;

import com.xtn.dao.CustomerDao;
import com.xtn.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户业务层实现类
 * 时间：2020年12月19日 14:06:29
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;


    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }
}
