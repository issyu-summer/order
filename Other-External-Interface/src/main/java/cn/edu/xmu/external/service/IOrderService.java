package cn.edu.xmu.external.service;

import cn.edu.xmu.external.model.OrderItem;

import java.util.List;

public interface IOrderService {
    public List<OrderItem> getOrderItems(List<Long> orderIdList);
}
