package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.po.OrderPoExample;
import cn.edu.xmu.outer.model.bo.MyReturn;
import cn.edu.xmu.outer.model.bo.OrderItem;
import cn.edu.xmu.outer.model.bo.OrderItemInfo;
import cn.edu.xmu.outer.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@DubboService(version = "0.0.1")
public class OrderOuterServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderOuterServiceImpl.class);

    @Autowired
    private OrderPoMapper orderPoMapper;

    @Autowired
    private OrderItemPoMapper orderItemPoMapper;

    /**
     * 通过OrderId获取订单详细信息
     * @author 韬韬
     * 通过orderId/orderItemId(可能更改)列表获得OrderItem列表
     * 定时任务1相关
     * @param orderIdList
     * @return List<OrderItem> Order对应的OrderItem列表
     */
    @Override
    public MyReturn<List<OrderItem>> getOrderItems(List<Long> orderIdList) {
        List<OrderItem> returnOrderItemList=new ArrayList<>();
        OrderItem orderItem=new OrderItem();

        OrderItemPoExample orderItemPoExample=new OrderItemPoExample();
        OrderItemPoExample.Criteria criteria= orderItemPoExample.createCriteria();
        criteria.andOrderIdIn(orderIdList);
           try{
             List<OrderItemPo> orderItemPos=orderItemPoMapper.selectByExample(orderItemPoExample);

             for(OrderItemPo po:orderItemPos) {
                 orderItem.setId(po.getId());
                 orderItem.setBeShareId(po.getBeShareId());
                 orderItem.setOrderId(po.getOrderId());
                 orderItem.setPrice(po.getPrice());
                 orderItem.setQuantity(po.getQuantity());
                 orderItem.setSkuId(po.getGoodsSkuId());
                 returnOrderItemList.add(orderItem);
             }
             MyReturn myReturn=new MyReturn(returnOrderItemList);
             return myReturn;
           }catch (DataAccessException e){
               logger.error("getOrderItems:数据库查询错误！");
               return null;
           }
    }
    /**
     * 获得OrderItem的信息
     * @author 陈星如 直接catch，不会有任何返回
     * @date 12/12/20 8:05 PM
     **/
    @Override
    public MyReturn<OrderItemInfo> getOrderItemInfo(Long orderItemId) {
        try {
            OrderItemPo orderItemPo = orderItemPoMapper.selectByPrimaryKey(orderItemId);
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(orderItemPo.getOrderId());

            //订单明细不存在
            if (orderItemPo == null) {
                logger.error("getOrderItemInfo:未找到orderItem！");
                return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            OrderItemInfo orderItemInfo = new OrderItemInfo(orderItemPo.getOrderId(),orderPo.getOrderSn(),
                    orderItemPo.getGoodsSkuId(), orderItemPo.getName(),orderPo.getShopId(), orderItemPo.getPrice());
            System.out.println(orderPo.getOrderSn());
            System.out.println(orderPo.getShopId());
            if (orderItemInfo != null) {
                return new MyReturn<>(orderItemInfo);
            }else{
                return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR);
            }

        } catch (DataAccessException e) {
            logger.error("数据库错误");
            return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }


    @Override
    public MyReturn<Boolean> aftersaleRefund(Long orderItemId) {
        return null;

    }

    /**
     * 完成售后发货流程
     * @param
     * @return
     */
    @Override
    public MyReturn<Long> aftersaleSendback(Long orderItemId) {
        //换货
try {
    OrderItemPo orderItemPo = orderItemPoMapper.selectByPrimaryKey(orderItemId);
    //获得相应订单
    if(orderItemPo==null){
        return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }
    OrderPo orderPo = orderPoMapper.selectByPrimaryKey(orderItemPo.getOrderId());
    //新建订单
    if(orderPo==null){
        return new MyReturn<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }

    OrderPo orderPo1 = orderPo;
    orderPo1.setId(null);
    orderPo1.setOrderSn(Common.genSeqNum());
    //未发货
    orderPo1.setShipmentSn(null);
    orderPo1.setConfirmTime(null);
    //状态：待收货，子已完成付款，
    orderPo1.setState((byte) OrderStateCode.ORDER_STATE_UNCONFIRMED.getCode());
    orderPo1.setSubstate((byte) OrderStateCode.ORDER_STATE_PAID.getCode());
    //时间
    orderPo1.setGmtCreate(LocalDateTime.now());
    orderPo1.setGmtModified(LocalDateTime.now());
    //插入新订单
    orderPoMapper.insert(orderPo1);
    OrderItemPo orderItemPo1 = orderItemPo;
    //订单明细订单id指向新订单
    orderItemPo1.setOrderId(orderPo1.getId());
    orderItemPoMapper.insert(orderItemPo1);
    //orderItemPo.setOrderId(orderPo);

    return new MyReturn(orderPo1.getId());
}catch (DataAccessException e){
    return new MyReturn<>(ResponseCode.INTERNAL_SERVER_ERR);
}
    }

    @Override
    public MyReturn<Boolean> confirmBought(Long userId, Long skuId) {
        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        criteria.andCustomerIdEqualTo(userId);

        OrderItemPoExample orderItemPoExample = new OrderItemPoExample();
        OrderItemPoExample.Criteria criteria1 = orderItemPoExample.createCriteria();
        criteria1.andGoodsSkuIdEqualTo(skuId);

        List<OrderItemPo> orderItemPos = orderItemPoMapper.selectByExample(orderItemPoExample);
        List<OrderPo>  orderPos = orderPoMapper.selectByExample(orderPoExample);
        if(orderItemPos.isEmpty()){
            return new MyReturn<>(Boolean.FALSE);
        }
        else if(orderPos.isEmpty()){
            return new MyReturn<>(Boolean.FALSE);
        }else{
            for(OrderPo orderPo:orderPos){
                for(OrderItemPo orderItemPo:orderItemPos){
                    if(orderPo.getId().equals(orderItemPo.getOrderId())){
                        return new MyReturn<>(Boolean.TRUE);
                    }
                }
            }
        }
            return new MyReturn<>(Boolean.FALSE);
    }


}
