package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.order.model.po.OrderItemPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建订单传值对象
 * @author issyu 30320182200070
 * @date 2020/12/4 23:30
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private Long orderId;
    private String name;
    private Integer quantity;
    private Long price;
    private Long discount;
    private Long couponActId;
    private Long beSharedId;

    public OrderItemPo getOrderItemPo() {
        OrderItemPo orderItemPo = new OrderItemPo();
        orderItemPo.setGoodsSkuId(this.getSkuId());
        orderItemPo.setQuantity(this.getQuantity());
        orderItemPo.setGmtCreate(LocalDateTime.now());
        orderItemPo.setGmtModified(LocalDateTime.now());
        return orderItemPo;
    }
}
