package cn.edu.xmu.external.model;


import lombok.Data;

/**
 * @author 卓
 * @date Created in 2020/11/23 19:33
 */
@Data
public class OrderItemInfo {

    private Long orderId;
    private String orderSn;
    private Long skuId;
    private String skuName;
    private Long shopId;
    private Long price;

    public OrderItemInfo(Long orderId, String orderSn, Long skuId, String skuName, Long shopId, Long refund) {
        this.orderId = orderId;
        this.orderSn = orderSn;
        this.skuId = skuId;
        this.skuName = skuName;
        this.shopId = shopId;
        this.price = refund;
    }
}
