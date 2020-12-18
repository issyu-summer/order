package cn.edu.xmu.external.service;

import cn.edu.xmu.external.model.CustomerInfo;
import cn.edu.xmu.external.model.MyReturn;

public interface IUserService {
    public MyReturn<Object> addPoint(int rebate, Long userId);

    public MyReturn<CustomerInfo> getUserInfo(Long userId);
}
