package com.xtn.dao;

import com.xtn.domain.Contacts;

public interface ContactsDao {

    //添加联系人
    int saveContacts(Contacts contacts);
}
