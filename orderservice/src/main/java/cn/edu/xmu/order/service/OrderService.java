package cn.edu.xmu.order.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.dao.OrderDao;
import cn.edu.xmu.order.model.bo.OrderBrief;
import cn.edu.xmu.order.model.bo.OrderInfoBo;
import cn.edu.xmu.order.model.vo.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
     * @param userId
     * @return
     * @author issyu 30320182200070
     * @date 2020/12/3 16:46
     */
    public ReturnObject getOrderStates(Long userId,Long departId) {
        ReturnObject returnObject = orderDao.getOrderStates(userId,departId);
        return returnObject;
    }

    /**
     * 查询所有订单概要
     * @param orderSn
     * @param state
     * @param page
     * @param pageSize
     * @author issyu 30320182200070
     * @date 2020/12/3 16:46
     */
    public ReturnObject<PageInfo<VoObject>> getOrderSimpleInfo(
            Long userId,Long departId,String orderSn,
            Byte state, Integer page, Integer pageSize,
            String beginTime,String endTime) {

        ReturnObject<PageInfo<VoObject>> returnObject = orderDao.getOrderSimpleInfo(
                userId,departId,orderSn, state, page, pageSize,beginTime,endTime);
        return returnObject;
    }

    /**
     * 买家创建订单
     * ？？？计算运费，计算价格？？？
     * @author issyu 30320182200070
     * @date 2020/12/5 0:30
     */

    public ReturnObject createOrder(OrderInfoVo vo,Long userId,Long departId){
        return orderDao.createOrder(vo,userId,departId);
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
    public ReturnObject<VoObject> changeOrder(Long userId,Long id, AdressVo adressVo){
        return orderDao.changeOrder(userId,id,adressVo);
    }


    /**
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * created in 2020/12/3
     */
    public ReturnObject<VoObject> deleteOrder(Long userId,Long id){
        return orderDao.deleteOrder(userId,id);
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
    }

    /**
     * 团购订单转普通
     *
     * @author 王薪蕾
     * created in 2020/12/6
     */
    @Transactional
    public ReturnObject grouponToNormalOrders(Long id,Long userId) {
        return orderDao.grouponToNormalOrders(id,userId);
    }

    /**
     * 分页查询店家查询商户所有订单 (概要)
     *
     * @author 30320182200071 王子扬
     * @param shopId 商户id, required
     * @param customerId 查询的购买者用户id
     * @param orderSn 订单Sn
     * @param beginTime 从开始时间查询
     * @param endTime 从结束时间开始查询
     * @param page  页码
     * @param pageSize 每页数目
     * @param departId 部门Id
     * @return ReturnObject<PageInfo < VoObject>> 分页返回订单信息
     * createdBy 王子扬 2020/12/04 16:17
     */
    public ReturnObject<PageInfo<VoObject>> selectOrders(Long shopId, Long customerId, String orderSn, String beginTime, String endTime, Integer page, Integer pageSize,Long departId){
        ReturnObject<PageInfo<VoObject>> returnObject = orderDao.findAllOrders(shopId, customerId, orderSn, beginTime, endTime, page, pageSize,departId);
        return returnObject;
    }

    /**
     * 店家修改订单 (留言)
     * @author 王子扬 30320182200071
     * @date  2020/12/5 23:38
     */
    public ReturnObject<OrderBrief> updateOrderMessage(Long shopId, Long id, OrderMessageVo orderMessageVo, Long departId) {
        ReturnObject<OrderBrief> returnObject = orderDao.updateOrderMessage(shopId,id,orderMessageVo,departId);
        return returnObject;
    }

    /**
     * 店家查询店内订单完整信息(普通，团购，预售)
     * @author 陈星如
     * @date 2020/12/5 16:10
     */
    public ReturnObject<VoObject>  getOrderByShopId(Long shopId, Long id) {
        return orderDao.getOrderByShopId(shopId,id);
    }

    /**
     * 管理员取消本店铺订单
     * @author 陈星如
     * @date 2020/12/5 15:15
     **/
    public ReturnObject<VoObject> deleteShopOrder(Long shopId,Long id,Long departId){
        return orderDao.deleteShopOrder(shopId,id,departId);
    }

    /**
     *店家对订单标记发货
     * @author 陈星如
     * @date 2020/12/5 21:16
     */
    public ReturnObject<VoObject> shipOrder(Long shopId, Long id, OrderShipmentSnVo orderShipmentSnVo, Long departId) {
        return orderDao.shipOrder(shopId,id,orderShipmentSnVo,departId);
    }
}