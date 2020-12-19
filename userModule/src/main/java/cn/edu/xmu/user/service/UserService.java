package cn.edu.xmu.user.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.dao.UserDao;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.vo.*;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private  static  final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private final Integer jwtExpireTime=3600;

    //    @Autowired
//    private JavaMailSender mailSender;

    /**
     * 注册用户
     * @author 张晨远
     * @return 用户基本信息
     */
    public ReturnObject<Object> registerUser(CustomerCreateVo customercreateVo){
        ReturnObject<Customer> retObj=userDao.insertUser(customercreateVo.createCustomer());

        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        Customer customer= retObj.getData();
        return new ReturnObject<>(new CustomerRetVo(customer));
    }

    /**
     * 用户查看自己信息
     * @author 张晨远
     * @param id 用户id
     * @return 用户基本信息
     */
    public ReturnObject<Object> getUserInfo(Long id){
        ReturnObject<Customer> retObj=userDao.selectUserById(id);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        Customer customer= retObj.getData();
        return new ReturnObject<>(new CustomerRetVo(customer));
    }

    /**
     * 用户修改自己信息
     * @author 张晨远
     * @param id 用户id
     * @param customerModifyVo 用户修改信息
     */
    public ReturnObject<Object> modifySelfInfo(Long id, CustomerModifyVo customerModifyVo){
        Customer customer= customerModifyVo.createCustomer();
        ReturnObject<Object> retObj=userDao.modifyUserInfoById(id, customer);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return retObj;
    }

    /**
     * 用户修改密码
     * @author 张晨远
     * @param customerPasswordVo 密码信息
     */
    public ReturnObject<Object> modifyPassword(CustomerPasswordVo customerPasswordVo){
        //通过验证码取出id
        if(!redisTemplate.hasKey("cp_"+customerPasswordVo.getCaptcha())) {
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        String id= redisTemplate.opsForValue().get("cp_"+customerPasswordVo.getCaptcha()).toString();
        ReturnObject<Customer> oldCustomer=userDao.selectUserById(Long.parseLong(id));
        if(oldCustomer.getCode() != ResponseCode.OK){
            return new ReturnObject<>(oldCustomer.getCode(),oldCustomer.getErrmsg());
        }
        //判断密码是否重复
        if(oldCustomer.getData().getPassword().equals(customerPasswordVo.getNewPassword())){
            return new ReturnObject<>(ResponseCode.PASSWORD_SAME);
        }
        //修改密码
        ReturnObject<Object> retObj = userDao.modifyUserInfoById(Long.parseLong(id), customerPasswordVo.createCustomer());
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return new ReturnObject<>();
    }

    /**
     * 用户重置密码
     * @author 张晨远
     * @param customerResetVo 账号信息
     */
    public ReturnObject<Object> resetPassword(CustomerResetVo customerResetVo){
        Customer customer= customerResetVo.createCustomer();
        ReturnObject<Customer> retCustomer=userDao.selectUserByUsername(customerResetVo.getUserName());
        if(retCustomer.getCode()!=ResponseCode.OK){
            return new ReturnObject<>(retCustomer.getCode(),retCustomer.getErrmsg());
        }
        else if(!customerResetVo.getEmail().equals((retCustomer.getData().getEmail()))){
            return new ReturnObject<>(ResponseCode.EMAIL_WRONG);
        }
//
//        //随机生成验证码
//        String captcha = RandomCaptcha.getRandomString(6);
////        while(redisTemplate.hasKey(captcha))
////            captcha = RandomCaptcha.getRandomString(6);
//
//        String id = retCustomer.getData().getId().toString();
//        String key = "cp_" + captcha;
//        //key:验证码,value:id存入redis
//        redisTemplate.opsForValue().set(key,id);
//        //五分钟后过期
//        redisTemplate.expire("cp_" + captcha, 5*60*1000, TimeUnit.MILLISECONDS);
//
//        //发送邮件(请在配置文件application.properties填写密钥)
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setSubject("【oomall】密码重置通知");
//        msg.setSentDate(new Date());
//        msg.setText("您的验证码是：" + captcha + "，5分钟内有效。");
//        msg.setFrom("1968035918@qq.com");
//        msg.setTo(customerResetVo.getEmail());
//        try {
//            mailSender.send(msg);
//        } catch (MailException e) {
//            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
//        }
        return new ReturnObject<>();
    }

    /**
     * 查询用户，以分页的形式返回
     * @author 张晨远
     * @return 用户信息列表
     */
    public ReturnObject<PageInfo<VoObject>> queryUsers(String username, String email, String mobile, Integer page,Integer pageSize){
        List<String> params=new ArrayList<>();

        ReturnObject<PageInfo<VoObject>> retObj=userDao.selectUser(username,email,mobile,page,pageSize);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return retObj;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return jwt
     */
    public ReturnObject<String> login(String username,String password){
        ReturnObject<Customer> retObj= userDao.selectUserByUsername(username);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        Customer customer=retObj.getData();
        if(customer.getState()==Customer.State.FORBID){
            return new ReturnObject<>(ResponseCode.AUTH_USER_FORBIDDEN);
        }
        if(!customer.getPassword().equals(password)){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        JwtHelper jwtHelper=new JwtHelper();
        String jwt=jwtHelper.createToken(customer.getId(),-2L,jwtExpireTime);
        return new ReturnObject<>(jwt);
    }

    /**
     * 用户登出
     */
    public ReturnObject<Object> logout(String token, Long id){
        String value = "logout_" + id;
        logger.debug("logout id = " + id);
        redisTemplate.opsForValue().set(token,id);
        redisTemplate.expire(token, jwtExpireTime, TimeUnit.SECONDS);
        logger.debug("banJwt: " + token);
        return new ReturnObject<>();
    }

    /**
     * 封禁用户
     * @param id 用户ID
     */
    public ReturnObject<Object> banUser(Long id){
        Customer customer=new Customer();
        customer.setGmtModified(LocalDateTime.now());
        customer.setState(Customer.State.FORBID);
        ReturnObject<Object> retObj=userDao.modifyUserInfoById(id,customer);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return retObj;
    }

    /**
     * 解禁用户
     * @param id 用户ID
     */
    public ReturnObject<Object> unbanUser(Long id){
        Customer customer=new Customer();
        customer.setGmtModified(LocalDateTime.now());
        customer.setState(Customer.State.NORM);
        ReturnObject<Object> retObj=userDao.modifyUserInfoById(id,customer);
        if(retObj.getCode() != ResponseCode.OK){
            return new ReturnObject<>(retObj.getCode(),retObj.getErrmsg());
        }
        return retObj;
    }

}
