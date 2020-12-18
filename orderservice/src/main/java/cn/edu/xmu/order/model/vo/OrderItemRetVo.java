package cn.edu.xmu.order.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 3:25
 */
@Data
public class OrderItemRetVo {
    private Long skuId;
    private Long orderId;
    private String name;
    private Integer quantity;
    private Long price;
    private Long discount;
    private Long couponId;
    private Long couponActivityId;
    private Long beShareId;
}
