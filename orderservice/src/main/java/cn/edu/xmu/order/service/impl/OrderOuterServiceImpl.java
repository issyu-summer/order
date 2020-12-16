package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.inner.service.PaymentInnerService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.outer.model.bo.*;
import cn.edu.xmu.outer.service.IOrderService;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @Override
    public MyReturn<OrderItemInfo> getOrderItemInfo(Long orderItemId) {
        return null;
    }

    @Override
    public MyReturn<Boolean> aftersaleRefund(Long orderItemId) {
        return null;
    }

    @Override
    public MyReturn<Long> aftersaleSendback(Long orderItemId) {
        return null;
    }


}
