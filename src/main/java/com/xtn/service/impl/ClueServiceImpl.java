package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ActivityDao;
import com.xtn.dao.ClueDao;
import com.xtn.dao.ContactsActivityRelationDao;
import com.xtn.domain.Activity;
import com.xtn.domain.Clue;
import com.xtn.domain.ClueActivityRelation;
import com.xtn.exception.TransactionException;
import com.xtn.service.ClueService;
import com.xtn.utils.UUIDUtil;
import com.xtn.vo.PaginationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 线索业务层实现类
 * 时间：2020年12月16日 11:23:34
 */
@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueDao clueDao;
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //分页查询线索信息
    @Override
    public PaginationVo<Clue> selectClueList(Map<String, Object> map) {
        Clue clue = (Clue) map.get("clue");
        PaginationVo<Clue> vo = new PaginationVo<>();
        Integer pageNum = Integer.parseInt((String) map.get("pageNum"));
        Integer pageSize = Integer.parseInt((String) map.get("pageSize"));

        //pageNum:查询的页数，pageSize:一页显示的数量
        PageHelper.startPage(pageNum,pageSize);
        List<Clue> clueList = clueDao.selectClueList(clue);

        //获取总记录数pageInfo.getTotal()
        PageInfo<Clue> pageInfo = new PageInfo<>(clueList);
        vo.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        vo.setDataList(clueList);
        return vo;
    }

    //保存线索信息
    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = false;
        int count = clueDao.saveClue(clue);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    //根据id查询线索信息
    @Override
    public Clue selectClueById(String id) {
        return clueDao.selectClueById(id);
    }

    //解除线索和市场活动的关联
    @Override
    public boolean disconnectById(String id) {
        boolean flag = false;
        int count = clueDao.disconnectById(id);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    //关联线索和市场活动信息
    @Override
    public boolean contactClueAndActivity(String clueId,String[] activityId) {
        boolean flag = false;
        for (int i = 0; i < activityId.length; i++) {
            String id = UUIDUtil.getUUID();
            //创建线索活动关联对象
            ClueActivityRelation car = new ClueActivityRelation();
            //添加id
            car.setId(id);
            //添加市场活动id
            car.setActivityId(activityId[i]);
            //添加线索id
            car.setClueId(clueId);
            int count = contactsActivityRelationDao.contactClueAndActivity(car);
            if (count == 1){
                flag = true;
            }else {
                throw new TransactionException("关联的市场活动信息数错误");
            }
        }
        return flag;
    }

}
