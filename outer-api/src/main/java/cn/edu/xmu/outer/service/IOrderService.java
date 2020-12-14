package cn.edu.xmu.outer.service;

import cn.edu.xmu.outer.model.bo.*;

import java.util.List;

public interface IOrderService {

  /**
     * 通过orderId/orderItemId(可能更改)列表获得OrderItem列表
     * 定时任务1相关
     * @return Order对应的OrderItem列表
     */
    MyReturn<List<OrderItem>> getOrderItems(List<Long> orderIdList);

    /**
     * 通过skuId查找对应的OrderItemId列表
     * 售后按条件进行查找时使用
     * @param skuId
     */
    MyReturn<List<Long>> getOrderItemList(Long skuId);

    /**
     * 获得OrderItem的信息
     * 填充售后单返回对象
     * @param orderItemId
     * @return
     */
    MyReturn<OrderItemInfo> getOrderItemInfo(Long orderItemId);

    /**
     * 完成退款流程
     * @param aftersale 可以提供所有售后单中存在的信息
     * @return
     * payment中通过aftersaleId获得orderId,修改order表
     */
    boolean aftersaleRefund(Aftersale aftersale);//退款

    /**
     * 完成售后发货流程
     * @param aftersale 可以提供所有售后单中存在的信息
     * @return
     */
    boolean aftersaleSendback(Aftersale aftersale);//换货生成新订单

    /**
    *获取订单信息
    * @param freightId
    * @return
    */
    Freight getFreightInfoById(Long freightId);
}

/**额外:
 * 定时任务1：订单完成时发送消息，传输OrderId/OrderItemId(如果为分享订单)
 * 或定时任务2：定期检查订单，对满足：1.存有分享ID，2.完成时间在7-8内，3.未退货。三个条件的OrderItem，调用我们实现的方法
 * void giveRebate(List<OrderItem> orderItemList);
 * 定时任务优先级较低
 */