package cn.edu.xmu.outer.service;

import cn.edu.xmu.outer.model.bo.MyReturn;

/**
 * @author issyu 30320182200070
 * @date 2020/12/16 14:18
 */
public interface IPaymentService {

    /**
     * 完成退款流程，创建退款单而不是更改退款单状态
     * @return
     */
    //MyReturn<Boolean> aftersaleRefund(Long orderItemId);
    MyReturn<Boolean> aftersaleRefund(Long afterSaleId);
}
