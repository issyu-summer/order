package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.ooad.util.TimeFormat;
import cn.edu.xmu.order.model.bo.OrderInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * orderInfo
 * @author issyu 30320182200070
 * @date 2020/12/4 23:30
 */
@Data
public class OrderInfoVo {
    private OrderItemVo orderItemVo;
    private String consignee;
    private Long regionId;
    private String address;
    private String mobile;
    private String message;
    private Long couponId;
    private Long presaleId;
    private Long grouponId;

    public OrderInfoVo(OrderInfo orderInfo){
        this.setAddress(orderInfo.getAddress());
        this.setConsignee(orderInfo.getConsignee());
        this.setCouponId(orderInfo.getCouponId());
        this.setGrouponId(orderInfo.getGrouponId());
        this.setMessage(orderInfo.getMessage());
        this.setMobile(orderInfo.getMobile());
        this.setOrderItemVo(orderInfo.getOrderItemVo());
        this.setPresaleId(orderInfo.getPresaleId());
        this.setRegionId(orderInfo.getRegionId());
    }

    public OrderInfo createOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAddress(this.getAddress());
        orderInfo.setConsignee(this.getConsignee());
        orderInfo.setCouponId(this.getCouponId());
        orderInfo.setGrouponId(this.getGrouponId());
        orderInfo.setMessage(this.getMessage());
        orderInfo.setMobile(this.getMobile());
        orderInfo.setOrderItemVo(this.getOrderItemVo());
        orderInfo.setPresaleId(this.getPresaleId());
        orderInfo.setRegionId(this.getRegionId());
        return orderInfo;
    }
}
