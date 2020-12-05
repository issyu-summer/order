package cn.edu.xmu.order.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.dao.OrderDao;
import cn.edu.xmu.order.model.bo.OrderInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issyu 30320182200070
 * @date 2020/12/2 23:03
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * 获取订单的所有状态
     *
     * @param userId
     * @return
     * @author issyu 30320182200070
     * @date 2020/12/3 16:46
     */
    public ReturnObject getOrderStates(Long userId) {
        ReturnObject returnObject = orderDao.getOrderStates(userId);
        return returnObject;
    }

    /**
     * 查询所有订单概要
     *
     * @param orderSn
     * @param state
     * @param page
     * @param pageSize
     * @author issyu 30320182200070
     * @date 2020/12/3 16:46
     */
    public ReturnObject<PageInfo<VoObject>> getOrderSimpleInfo(Long userId,String orderSn, Byte state, Integer page, Integer pageSize) {

        ReturnObject<PageInfo<VoObject>> returnObject = orderDao.getOrderSimpleInfo(userId,orderSn, state, page, pageSize);
        return returnObject;
    }

    /**
     * 买家创建订单
     * ？？？计算运费，计算价格？？？
     * @author issyu 30320182200070
     * @date 2020/12/5 0:30
     */

    public ReturnObject<OrderInfo> createOrder(OrderInfo orderInfo){
        ReturnObject<OrderInfo> returnObject = orderDao.createOrder(orderInfo);
        return returnObject;
    }
}