package cn.edu.xmu.external.model.bo;


import lombok.Data;


@Data
public class OrderItemInfo {

    private Long orderId;

    private String orderSn;

    private Long skuId;

    private String skuName;

    private Long shopId;

    private Long price;
    //orderItem实际支付价格

}
