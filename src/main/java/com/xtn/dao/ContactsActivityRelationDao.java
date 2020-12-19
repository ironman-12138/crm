package com.xtn.dao;

import com.xtn.domain.ContactsActivityRelation;

public interface ContactsActivityRelationDao {

    //添加联系人和市场活动的关系
    int saveContactsActivityRelation(ContactsActivityRelation csar);
}
