package cn.edu.xmu.order.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.dao.OrderDao;
import cn.edu.xmu.order.model.bo.OrderInfo;
import cn.edu.xmu.order.model.vo.AdressVo;
import cn.edu.xmu.order.model.vo.OrderRetVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 获取订单完整信息
     * @author 史韬韬
     * created in 2020/12/2
     */
    public ReturnObject<OrderRetVo> getOrderById(Long id){
        return orderDao.getOrderById(id);
    }

    /**
     *买家修改本人名下订单
     * @parameter id 订单id
     * @author 史韬韬
     */
    public ReturnObject<VoObject> changeOrder(Long id, AdressVo adressVo){
        return orderDao.changeOrder(id,adressVo);
    }
    /**
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * created in 2020/12/3
     */
    public ReturnObject<VoObject> deleteOrder(Long id){
        return orderDao.deleteOrder(id);
    }

    /**
     * 客户确认收货
     *
     * @author 王薪蕾
     * created in 2020/12/6
     */
    @Transactional
    public ReturnObject confirmOrders(Long id,Long userId) {
        return orderDao.confirmOrders(id,userId);
    }    /**
     * 团购订单转普通
     *
     * @author 王薪蕾
     * created in 2020/12/6
     */
    @Transactional
    public ReturnObject grouponToNormalOrders(Long id,Long userId) {
        return orderDao.grouponToNormalOrders(id,userId);
    }
}