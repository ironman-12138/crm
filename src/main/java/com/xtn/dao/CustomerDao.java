package com.xtn.dao;

import com.xtn.domain.Customer;
import org.apache.ibatis.annotations.Param;

public interface CustomerDao {

    //根据公司名查询客户对象
    Customer selectCustomerByName(@Param("company") String company);

    //添加客户
    int saveCustomer(Customer customer);
}
