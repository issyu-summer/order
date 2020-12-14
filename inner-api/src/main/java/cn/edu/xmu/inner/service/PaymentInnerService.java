package cn.edu.xmu.inner.service;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 1:02
 */
public interface PaymentInnerService {

    /**
     * provider
     * 根据OrderId更改退款单状态
     * @author issyu 30320182200070
     * @date 2020/12/14 1:06
     */
    Boolean updateRefundStateByOrderId(Long orderId);
}
