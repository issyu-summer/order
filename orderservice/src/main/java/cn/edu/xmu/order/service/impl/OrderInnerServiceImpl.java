package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.inner.model.bo.MyReturn;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.po.OrderPoExample;
import cn.edu.xmu.inner.service.OrderInnerService;
import io.lettuce.core.StrAlgoArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

/**
 * provider
 * @author issyu 30320182200070
 * @date 2020/12/8 18:10
 */
@Slf4j
@DubboService
public class OrderInnerServiceImpl implements OrderInnerService {
    @Autowired
    private OrderPoMapper orderPoMapper;

    @Autowired
    private OrderItemPoMapper orderItemPoMapper;

    @Override
    public List<Long> getOrderIdByUserId(Long userId) {
        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        criteria.andCustomerIdEqualTo(userId);
        List<Long> orderIds = new ArrayList<>();
        try {
            List<OrderPo> orderPoList = orderPoMapper.selectByExample(orderPoExample);
            for (OrderPo orderPo : orderPoList) {
                orderIds.add(orderPo.getId());
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
        }
        return orderIds;
    }

    @Override
    public List<Long> getOrderIdByShopId(Long shopId) {
        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        criteria.andShopIdEqualTo(shopId);
        List<Long> orderIds = new ArrayList<>();
        try {
            List<OrderPo> orderPoList = orderPoMapper.selectByExample(orderPoExample);
            for (OrderPo orderPo : orderPoList) {
                orderIds.add(orderPo.getId());
            }
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
        }
        return orderIds;
    }

    /**
     * orderservice提供给其他微服务使用
     * @author issyu 30320182200070
     * @date 2020/12/16 12:32
     * @param orderId
     * @return
     */
    @Override
    public Long getCustomerIdByOrderId(Long orderId) {
        Long userId = orderPoMapper.selectByPrimaryKey(orderId).getCustomerId();
        return userId;
    }

    @Override
    public Long getUserIdByOrderId(Long orderId){
        try{
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(orderId);
            return orderPo.getCustomerId();
        }catch (Exception e){
            log.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public Long getShopIdByOrderId(Long orderId){
        try{
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(orderId);
            return orderPo.getShopId();
        }catch (Exception e){
            log.debug(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean orderIsExistByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        if(orderPo==null){
            return false;
        }
        return true;
    }

    //未实现
    @Override
    public Long getActAmountByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        Long actAmount=orderPo.getOriginPrice();
        //+运费
        if(orderPo.getFreightPrice()!=null)
            actAmount=actAmount+orderPo.getFreightPrice();
        //普通订单
        if(orderPo.getOrderType().equals((byte)0));
        //团购
        if(orderPo.getOrderType().equals((byte)1));
        //预售
        if(orderPo.getOrderType().equals((byte)2));
        return actAmount;
    }

    @Override
    public Boolean payForOrder(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        Byte type=orderPo.getOrderType();
        Byte state=orderPo.getState();
        Byte subState=orderPo.getSubstate();
        //普通订单
        if(type.equals((byte)0))
        {
            //待收货 付款完成
            orderPo.setState((byte)2);
            orderPo.setSubstate((byte)21);
        }
        //团购
        if(type.equals((byte)1))
        {
            //待收货 待成团
            orderPo.setState((byte)2);
            orderPo.setSubstate((byte)22);
        }
        //预售
        if(type.equals((byte)2))
        {
            //支付定金
            // 待付款  新订单 --》待付款  待支付尾款
            if(state.equals((byte)1)&&subState.equals((byte)11))
            {
                orderPo.setState((byte)1);
                orderPo.setSubstate((byte)12);
            }
            //支付尾款
            // 待付款  待支付尾款 --》待收货 付款完成
            else {
                orderPo.setState((byte)2);
                orderPo.setSubstate((byte)21);
            }
        }
        return null;
    }

    @Override
    public Byte getTypeByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        return orderPo.getOrderType();
    }

    @Override
    public Byte getStateByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        return orderPo.getState();
    }

    @Override
    public Byte getSubstateByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        return orderPo.getSubstate();
    }

    @Override
    public Long getPresaleIdByOrderId(Long orderId) {
        OrderPo orderPo= orderPoMapper.selectByPrimaryKey(orderId);
        return orderPo.getPresaleId();
    }

}
