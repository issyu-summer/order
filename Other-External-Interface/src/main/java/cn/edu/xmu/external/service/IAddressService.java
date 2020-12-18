package cn.edu.xmu.external.service;

import cn.edu.xmu.external.model.MyReturn;

public interface IAddressService {
    /**
     * 由regionId校验region是否存在
     * @param regionId
     * @return
     */
    MyReturn<Boolean> verifyRegionId(Long regionId);
}
