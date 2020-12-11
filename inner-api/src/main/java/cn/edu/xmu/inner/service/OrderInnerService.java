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
}
