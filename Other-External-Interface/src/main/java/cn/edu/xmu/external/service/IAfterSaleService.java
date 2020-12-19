package cn.edu.xmu.external.service;

import cn.edu.xmu.external.model.MyReturn;

public interface IAfterSaleService {
    //int selectAfterSaleQuantity(Long id);//传入orderItemid,回传退货的数量，没有退货则传0
    /**
     * 校验该售后服务id是否存在   其他模块
     *
     * @param aftersaleId
     * @return
     */
    MyReturn<Boolean> verifyAfterSaleId(Long aftersaleId);
}
