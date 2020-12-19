package cn.edu.xmu.user.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.mapper.CustomerPoMapper;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.po.CustomerPoExample;
import cn.edu.xmu.user.model.vo.CustomerModifyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDao {
    @Autowired
    private CustomerPoMapper customerPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    /**
     * 检查用户名重复
     * @author 张晨远
     * @param userName 需要检查的用户名
     * @return boolean
     */
    public boolean isUserNameExist(String userName){
        logger.debug("is checking userName in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }

    /**
     * 检查邮箱重复
     * @author 张晨远
     * @param email 需要检查的邮箱
     * @return boolean
     */
    public boolean isEmailExist(String email){
        logger.debug("is checking email in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }

    /**
     * 检查电话重复
     * @author 张晨远
     * @param mobile 电话号码
     * @return boolean
     */
    public boolean isMobileExist(String mobile){
        logger.debug("is checking mobile in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }

    /**
     * 查找所有用户信息
     * @author 张晨远
     * @return 用户信息列表
     */
    public ReturnObject<List<Customer>> selectAllUser(){

        List<CustomerPo> customerPos;
        try {
            customerPos = customerPoMapper.selectByExample(new CustomerPoExample());
        }catch (DataAccessException e){
            logger.error("SelectAllUser: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        List<Customer> customers=new ArrayList<>(customerPos.size());
        for(CustomerPo customerPo:customerPos){
            customers.add(new Customer(customerPo));
        }
        return new ReturnObject<>(customers);
    }

    /**
     * 插入用户信息
     * @author 张晨远
     * @return 用户信息
     */
    public ReturnObject<Customer> insertUser(Customer customer){
        CustomerPo customerPo=customer.createPo();
        Customer customerRet;

        if(isEmailExist(customerPo.getEmail())){
            return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED);
        }
        if(isMobileExist(customerPo.getMobile())){
            return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED);
        }
        if(isUserNameExist(customerPo.getUserName())){
            return new ReturnObject<>(ResponseCode.USER_NAME_REGISTERED);
        }

        try {
            int ret = customerPoMapper.insertSelective(customerPo);
            if (ret == 0) {
                //插入失败
                logger.debug("insertUser: insert fail");
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "注册用户失败");
            } else {
                //插入成功
                logger.debug("insertUser: insert success" );
                customerRet=new Customer(customerPo);
            }
        }catch (DataAccessException e){
            logger.error("insertUser: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return new ReturnObject<>(customerRet);
    }

    /**
     * 根据ID查找user
     * @author 张晨远
     * @param id 用户ID
     * @return 用户信息
     */
    public ReturnObject<Customer> selectUserById(Long id){
        CustomerPo customerPo;
        try {
            customerPo = customerPoMapper.selectByPrimaryKey(id);
        }catch (DataAccessException e){
            logger.error("selectUserById: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        if(customerPo==null){
            logger.debug("selectUserById: Id not exist");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"用户ID不存在");
        }
        Customer customer=new Customer(customerPo);
        return new ReturnObject<>(customer);
    }

    /**
     * 根据用户名查找user
     * @author 张晨远
     * @param username 用户名
     * @return 用户信息
     */
    public ReturnObject<Customer> selectUserByUsername(String username){
        List<CustomerPo> customerPos;
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(username);
        try {
            customerPos = customerPoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectUserByUsername: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        if(customerPos.isEmpty()){
            logger.debug("selectUserByUsername: username not exist");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"用户不存在");
        }
        Customer customer=new Customer(customerPos.get(0));
        return new ReturnObject<>(customer);
    }

    /**
     * 根据用户ID修改用户信息
     * @author 张晨远
     * @param id 用户ID
     * @param customer 需要修改的用户信息
     */
    public ReturnObject<Object> modifyUserInfoById(Long id,Customer customer){
        //判断是否逻辑删除
        CustomerPo oldCustomer=customerPoMapper.selectByPrimaryKey(id);
        if (oldCustomer == null || oldCustomer.getBeDeleted()==(byte)1) {
            logger.info("用户不存在或已被删除：id = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        CustomerPo customerPo=customer.createPo();
        customerPo.setId(id);
        int ret=0;
        try {
            ret = customerPoMapper.updateByPrimaryKeySelective(customerPo);
        } catch (DataAccessException e) {
            // 其他情况属未知错误
            logger.error("数据库错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他 Exception 即属未知错误
            logger.error("严重错误：" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,
                    String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        if(ret==0){
            logger.debug("modifyUserInfoById: userId ="+id+" not exist");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else{
            logger.debug("modifyUserInfoById: userId = "+id);
        }
        return new ReturnObject<>();
    }

    /**
     * 根据用户名修改用户信息
     * @author 张晨远
     * @param customer 需要修改的用户信息
     */
    public ReturnObject<Object> modifyUserInfoByUsername(Customer customer){
        CustomerPo customerPo=customer.createPo();
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(customer.getUserName());
        try {
            int ret = customerPoMapper.updateByExampleSelective(customerPo,example);
            if(ret==0){
                logger.debug("modifyUserInfoByUsername: username ="+customer.getUserName()+" not exist");
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, ResponseCode.RESOURCE_ID_NOTEXIST.getMessage());
            }
            else{
                logger.debug("modifyUserInfoByUsername: username = "+customer.getUserName());
            }
        }catch (DataAccessException e){
            logger.error("modifyUserInfoByUsername: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        return new ReturnObject<>();
    }

    /**
     * 根据条件查询用户
     * @param username 用户名
     * @param email 邮箱
     * @param mobile 电话号码
     * @param page 分页
     * @param pageSize 分页大小
     * @return 分页用户信息
     */
    public ReturnObject<PageInfo<VoObject>> selectUser(String username, String email, String mobile, Integer page,Integer pageSize){
        List<CustomerPo> customerPoList;
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        if(username!=null)
            criteria.andUserNameEqualTo(username);
        if(email!=null)
            criteria.andEmailEqualTo(email);
        if(mobile!=null)
            criteria.andMobileEqualTo(mobile);
        PageHelper.startPage(page, pageSize);
        try {
            customerPoList = customerPoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("SelectUser: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        List<VoObject> customerList=new ArrayList<>(customerPoList.size());
        for(CustomerPo customerPo:customerPoList){
            customerList.add(new Customer(customerPo));
        }
        PageInfo pageInfo=new PageInfo<>(customerPoList);
        PageInfo<VoObject> customerPage = PageInfo.of(customerList);
        customerPage.setPageNum(pageInfo.getPageNum());
        customerPage.setPages(pageInfo.getPages());
        customerPage.setTotal(pageInfo.getTotal());
        customerPage.setPageSize(pageInfo.getPageSize());
        return new ReturnObject<>(customerPage);
    }
}
