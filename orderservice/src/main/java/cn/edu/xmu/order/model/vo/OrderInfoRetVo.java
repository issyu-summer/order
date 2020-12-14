package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.order.model.bo.OrderInfoBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 3:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoRetVo {

    private Long id;
    private CustomerRetVo customerVo;
    private ShopRetVo shopVo;
    private Long pid;
    private Byte orderType;
    private Byte state;
    private Byte subState;
    private String gmtCreateTime;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private String message;
    private String consignee;
    private Long couponId;
    private Long couponActivityId;
    private List<OrderItemRetVo> orderItems;

    /**
     * Bo->Vo
     * @author issyu 30320182200070
     */
    public OrderInfoRetVo(OrderInfoBo orderInfoBo){
        this.setConsignee(orderInfoBo.getConsignee());
        this.setCouponActivityId(orderInfoBo.getCouponActivityId());
        this.setCouponId(orderInfoBo.getCouponId());
        this.setCustomerVo(orderInfoBo.getCustomerVo());
        this.setDiscountPrice(orderInfoBo.getDiscountPrice());
        this.setFreightPrice(orderInfoBo.getFreightPrice());
        this.setGmtCreateTime(LocalDateTime.now().toString());
        this.setId(orderInfoBo.getId());
        this.setMessage(orderInfoBo.getMessage());
        this.setOrderItems(orderInfoBo.getOrderItems());
        this.setOrderType(orderInfoBo.getOrderType());
        this.setOriginPrice(orderInfoBo.getOriginPrice());
        this.setPid(orderInfoBo.getPid());
        this.setShopVo(orderInfoBo.getShopVo());
        this.setState(orderInfoBo.getState());
        this.setSubState(orderInfoBo.getSubState());

    }
}
