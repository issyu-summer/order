package cn.edu.xmu.inner.service;

import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/11 10:44
 */
public interface OrderInnerService {

    /**
     * 通过用户Id获得订单Id
     * @author issyu 30320182200070
     * @date 2020/12/8 17:44
     * @param userId
     * @return
     */
    List<Long> getOrderIdByUserId(Long userId);

    /**
     * 通过店铺Id获取订单Id
     * @author issyu 30320182200070
     * @date 2020/12/16 12:59
     */
    List<Long> getOrderIdByShopId(Long shopId);
    /**
     * 通过订单Id获取用户Id
     * @author issyu 30320182200070
     * @date 2020/12/16 12:28
    */
    Long getCustomerIdByOrderId(Long orderId);
    /**
     * 通过订单Id获得用户Id
     * @author 王子扬 30320182200071
     * @date 2020/12/11 17:44
     * @param orderId
     * @return
     */
    Long getUserIdByOrderId(Long orderId);

    Long getShopIdByOrderId(Long orderId);
}
