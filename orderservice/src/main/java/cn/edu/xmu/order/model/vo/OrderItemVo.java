package cn.edu.xmu.order.model.vo;

import lombok.Data;

/**
 * 创建订单传值对象
 * @author issyu 30320182200070
 * @date 2020/12/4 23:30
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private Integer quantity;
    private Long couponActId;
}
