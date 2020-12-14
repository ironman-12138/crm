package com.xtn.service;

import com.xtn.domain.Activity;
import com.xtn.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * 市场活动业务层
 * 时间：2020年12月13日 15:38:09
 */
public interface ActivityService {

    //添加用户市场活动信息
    public boolean saveUserActivity(Activity activity);

    //根据条件查询市场活动信息集合
    public PaginationVo selectActivityList(Map map);
}
