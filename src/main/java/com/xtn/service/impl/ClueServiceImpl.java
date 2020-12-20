package com.xtn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtn.dao.*;
import com.xtn.domain.*;
import com.xtn.exception.TransactionException;
import com.xtn.service.ClueService;
import com.xtn.utils.DateTimeUtil;
import com.xtn.utils.UUIDUtil;
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
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

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
        int count = clueActivityRelationDao.deleteClueActivityRelation(id);
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
            int count = clueActivityRelationDao.contactClueAndActivity(car);
            if (count == 1){
                flag = true;
            }else {
                throw new TransactionException("关联的市场活动信息数错误");
            }
        }
        return flag;
    }

    //转换线索
    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();

        //(1)根据id获取线索对象
        Clue clue = clueDao.selectClueById2(clueId);

        //(2)根据线索对象获取客户信息，当客户不存在时新建客户（根据公司名称判断客户是否存在）
        String company = clue.getCompany();
        Customer customer = customerDao.selectCustomerByName(company);
        if (customer == null){
            //不存在这个客户就新建客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setWebsite(clue.getWebsite());
            customer.setName(company);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            //添加客户
            int c1 = customerDao.saveCustomer(customer);
            if (c1 != 1){
                flag = false;
                throw new TransactionException("客户添加失败");
            }
        }

        //(3)根据线索提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setDescription(clue.getDescription());
        contacts.setAddress(clue.getAddress());
        //添加联系人
        int c2 = contactsDao.saveContacts(contacts);
        if (c2 != 1){
            flag = false;
            throw new TransactionException("联系人添加失败");
        }

        //(4)线索备注转换到客户备注以及联系人备注
        //获取线索备注对象
        List<ClueRemark> clueRemarkList = clueRemarkDao.selectClueRemarkByClueId(clueId);
        for (ClueRemark cr:clueRemarkList) {
            //线索备注的备注内容转换到客户备注以及联系人备注中
            String noteContent = cr.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setNoteContent(noteContent);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            int c3 = customerRemarkDao.saveCustomerRemark(customerRemark);
            if (c3 != 1){
                flag = false;
                throw new TransactionException("客户备注添加失败");
            }

            //创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            int c4 = contactsRemarkDao.saveContactsRemark(contactsRemark);
            if (c4 != 1){
                flag = false;
                throw new TransactionException("联系人备注添加失败");
            }
        }

        //(5)“线索和市场活动关系”转到“联系人和活动的关系”
        //查询出与当前线索关联的市场活动
        List<ClueActivityRelation> carList = clueActivityRelationDao.selectClueActivityRelationByClueId(clueId);
        for (ClueActivityRelation car:carList) {
            //从每一条线索关联的市场活动记录中取出市场活动id
            String activityId = car.getActivityId();

            //创建联系人和市场活动的关系
            ContactsActivityRelation csar = new ContactsActivityRelation();
            csar.setActivityId(activityId);
            csar.setContactsId(contacts.getId());
            csar.setId(UUIDUtil.getUUID());

            //添加联系人和市场活动的关系
            int c5 = contactsActivityRelationDao.saveContactsActivityRelation(csar);
            if (c5 != 1){
                flag = false;
                throw new TransactionException("联系人和市场活动的关系添加失败");
            }
        }

        //(6)如果有创建交易的需求，创建一条交易
        if (tran != null){
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(contacts.getContactSummary());
            tran.setContactsId(contacts.getId());

            //添加交易
            int c6 = tranDao.saveTransaction(tran);
            if (c6 != 1){
                flag = false;
                throw new TransactionException("交易信息添加失败");
            }

            //(7)如果创建了交易，就创建一条交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            int c7 = tranHistoryDao.saveTranHistory(th);
            if (c7 != 1){
                flag = false;
                throw new TransactionException("历史交易信息添加失败");
            }
        }

        //(8)删除线索备注
        for (ClueRemark cr:clueRemarkList) {
            int c8 = clueRemarkDao.deleteClueRemark(cr.getId());
            if (c8 != 1){
                flag = false;
                throw new TransactionException("删除线索备注失败");
            }
        }

        //(9)删除线索和市场活动的关联关系
        for (ClueActivityRelation car:carList) {
            int c9 = clueActivityRelationDao.deleteClueActivityRelation(car.getId());
            if (c9 != 1){
                flag = false;
                throw new TransactionException("删除线索和市场活动的关联失败");
            }
        }

        //(10)删除线索
        int c10 = clueDao.deleteClue(clueId);
        if (c10 != 1){
            flag = false;
            throw new TransactionException("删除线索失败");
        }

        return flag;
    }

}
