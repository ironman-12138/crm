package com.xtn.dao;

import com.xtn.domain.CustomerRemark;

public interface CustomerRemarkDao {

    //添加客户备注
    int saveCustomerRemark(CustomerRemark customerRemark);
}
