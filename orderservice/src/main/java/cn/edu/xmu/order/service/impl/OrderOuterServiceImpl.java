package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.external.model.bo.*;
import cn.edu.xmu.external.service.IOrderService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@DubboService
public class OrderOuterServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderOuterServiceImpl.class);

    @Autowired
    OrderPoMapper orderPoMapper;

    @Autowired
    OrderItemPoMapper orderItemPoMapper;

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
    public List<Long> getOrderItemIdList(Long skuId) {
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
        return orderItemIdList;
    }

    @Override
    public Freight getFreightInfoById(Long freightId) {
        return null;
    }

    @Override
    public boolean aftersaleRefund(Aftersale aftersale) {
        return false;
    }

    @Override
    public boolean aftersaleSendback(Aftersale aftersale) {
        return false;
    }

    @Override
    public OrderItemInfo getOrderItemInfo(Long orderItemId) {
        return null;
    }



}
