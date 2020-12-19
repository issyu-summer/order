//package cn.edu.xmu.user.service;
//
//import cn.edu.xmu.external.model.CustomerInfo;
//import cn.edu.xmu.external.model.MyReturn;
//import cn.edu.xmu.external.service.IUserService;
//import cn.edu.xmu.ooad.util.ResponseCode;
//import cn.edu.xmu.ooad.util.ReturnObject;
//import cn.edu.xmu.user.dao.UserDao;
//import cn.edu.xmu.user.model.bo.Customer;
////import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.beans.factory.annotation.Autowired;
//
////@DubboService(version = "0.0.1")
//public class UserServiceImpl implements IUserService {
//    @Autowired
//    UserDao userDao;
//
//    @Override
//    public MyReturn<Object> addPoint(int rebate, Long userId) {
//        ReturnObject<Customer> retCustomer=userDao.selectUserById(userId);
//        if(retCustomer.getCode()!= ResponseCode.OK){
//            return new MyReturn<>(retCustomer.getCode());
//        }
//        Customer customer=new Customer();
//        customer.setPoint(rebate+retCustomer.getData().getPoint());
//        userDao.modifyUserInfoById(userId, customer);
//        return new MyReturn<>();
//    }
//
//    @Override
//    public MyReturn<CustomerInfo> getUserInfo(Long userId) {
//        ReturnObject<Customer> retCustomer=userDao.selectUserById(userId);
//        if(retCustomer.getCode()!= ResponseCode.OK){
//            return new MyReturn<>(retCustomer.getCode());
//        }
//        CustomerInfo customerInfo=new CustomerInfo();
//        customerInfo.setId(userId);
//        customerInfo.setUserName(retCustomer.getData().getUserName());
//        customerInfo.setRealName(retCustomer.getData().getRealName());
//        return new MyReturn<>(customerInfo);
//    }
//}
