package cn.edu.xmu.external.model;

import lombok.Data;

@Data
public class OrderItem {
    Long id;
    int quantity;
    int price;
    Long beShareId;
    Long skuId;
    Long orderId;
}
