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
   * 获得OrderItem的信息
   * 填充售后单返回对象
   * @param orderItemId
   * @return
   */
  MyReturn<OrderItemInfo> getOrderItemInfo(Long orderItemId);

  /**
   * 完成退款流程
   * @return
   */
  MyReturn<Boolean> aftersaleRefund(Long orderItemId);

  /**
   * 完成售后换货发货流程
   * 传入orderItemId用以订单模块生成新订单
   * 需要返回生成的新订单orderId
   * @return
   */
  MyReturn<Long> aftersaleSendback(Long orderItemId);

  /**
   * 判断该订单是否存在
   * @author issyu 30320182200070
   * @date 2020/12/17 19:39
   * @param userId
   * @param skuId
   * @return
   */
  MyReturn<Boolean> confirmBought(Long userId,Long skuId);
}