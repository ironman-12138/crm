package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.ClueDao;
import com.xtn.domain.Clue;
import com.xtn.service.ClueService;
import com.xtn.vo.PaginationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
