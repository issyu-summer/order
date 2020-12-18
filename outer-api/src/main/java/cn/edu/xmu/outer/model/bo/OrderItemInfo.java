package cn.edu.xmu.outer.model.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemInfo implements Serializable {

    private Long orderId;

    private String orderSn;

    private Long skuId;

    private String skuName;

    private Long shopId;

    private Long price;
    //orderItem实际支付价格


}
