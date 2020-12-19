package com.xtn.dao;

import com.xtn.domain.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDao {

    //根据公司名查询客户对象
    Customer selectCustomerByName(@Param("company") String company);

    //添加客户
    int saveCustomer(Customer customer);

    //根据名称模糊查询客户名称
    List<String> getCustomerName(String name);

}
