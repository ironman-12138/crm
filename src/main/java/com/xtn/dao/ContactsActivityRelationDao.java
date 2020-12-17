package com.xtn.dao;

import com.xtn.domain.ClueActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {
    //关联线索和市场活动信息
    int contactClueAndActivity(ClueActivityRelation car);
}
