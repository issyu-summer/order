package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.outer.model.bo.*;
import cn.edu.xmu.outer.service.IOrderService;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
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
@DubboService
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
     * 通过skuId查找对应的OrderItemId列表
     * 售后按条件进行查找时使用
     * @param skuId
     */
    @Override
    public MyReturn<List<Long>> getOrderItemList(Long skuId) {
        List<Long> orderItemIdList=new ArrayList<>();
        try{
            OrderItemPoExample orderItemPoExample=new OrderItemPoExample();
            OrderItemPoExample.Criteria criteria= orderItemPoExample.createCriteria();
            criteria.andGoodsSkuIdEqualTo(skuId);
            List<OrderItemPo> orderItemPos=orderItemPoMapper.selectByExample(orderItemPoExample);
            //若未找到orderItem，则继续搜索
            if(orderItemPos==null){
                logger.error("getOrderItemIdList:未找到orderItem！");
            }
            else {
                for (OrderItemPo po : orderItemPos) {
                    orderItemIdList.add(po.getId());
                }
            }
        }catch (DataAccessException e){
            logger.error("getOrderItemIdList:数据库查询错误！");
        }
        return new MyReturn<>(orderItemIdList);
    }


    @Override
    public boolean aftersaleRefund(Aftersale aftersale) {
        return false;
    }
    /**
     * 完成售后发货流程
     * @param aftersale 可以提供所有售后单中存在的信息
     * @return
     */
    @Override
    public Long aftersaleSendback(Aftersale aftersale) {
        //换货
        System.out.println("ok");
            Long itemId=aftersale.getOrderItemId();
            OrderItemPo orderItemPo=orderItemPoMapper.selectByPrimaryKey(itemId);
            //获得相应订单
            OrderPo orderPo=orderPoMapper.selectByPrimaryKey(orderItemPo.getOrderId());
            //新建订单
            OrderPo orderPo1=orderPo;
            orderPo1.setId(null);
            //未发货
            orderPo1.setShipmentSn(null);
            orderPo1.setConfirmTime(null);
            //状态：待收货，子已完成付款，
            orderPo1.setState((byte)2);
            orderPo1.setSubstate((byte)21);
            //时间
            orderPo1.setGmtCreate(LocalDateTime.now());
            orderPo1.setGmtModified(LocalDateTime.now());
            //插入新订单
            orderPoMapper.insert(orderPo1);
            OrderItemPo orderItemPo1=orderItemPo;
            //订单明细订单id指向新订单
            orderItemPo1.setOrderId(orderItemPo1.getId());
            orderItemPoMapper.insert(orderItemPo1);
            return orderPo1.getId();
    }

    @Override
    public MyReturn<OrderItemInfo> getOrderItemInfo(Long orderItemId) {
        return null;
    }



}
