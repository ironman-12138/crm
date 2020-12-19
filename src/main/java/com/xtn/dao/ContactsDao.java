package com.xtn.dao;

import com.xtn.domain.Contacts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactsDao {

    //添加联系人
    int saveContacts(Contacts contacts);

    //根据姓名查询联系人
    List<Contacts> getContactsListByName(@Param("name") String name);
}
