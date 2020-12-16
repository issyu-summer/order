package cn.edu.xmu.order.service.impl;

import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.po.OrderPoExample;
import cn.edu.xmu.inner.service.OrderInnerService;
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
}

