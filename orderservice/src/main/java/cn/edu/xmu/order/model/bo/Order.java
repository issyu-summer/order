package cn.edu.xmu.order.model.bo;


import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.vo.OrderRetVo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {

    private Long id;

    private Long customerId;

    private Long shopId;

    private String orderSn;

    private Long pid;

    private String consignee;

    private Long regionId;

    private String address;

    private String mobile;

    private String message;

    private Byte orderType;

    private Long freightPrice;

    private Long couponId;

    private Long couponActivityId;

    private Long discountPrice;

    private Long originPrice;

    private Long presaleId;

    private Long grouponDiscount;

    private Integer rebateNum;

    private LocalDateTime confirmTime;

    private String shipmentSn;

    private Byte state;

    private Byte substate;

    private Byte beDeleted;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long grouponId;

    Order(){}

    public Order(OrderPo po){
        id=po.getId();
        customerId=po.getCustomerId();
        shopId=po.getShopId();
        orderSn=po.getOrderSn();
        pid=po.getPid();
        consignee=po.getConsignee();
        regionId=po.getRegionId();
        address=po.getAddress();
        mobile=po.getMobile();
        message=po.getMessage();
        orderType=po.getOrderType();
        freightPrice=po.getFreightPrice();
        couponId=po.getCouponId();
        couponActivityId=po.getCouponActivityId();
        discountPrice=po.getDiscountPrice();
        originPrice=po.getOriginPrice();

    }

    public OrderRetVo createVo(){
        OrderRetVo orderRetVo=new OrderRetVo();
        orderRetVo.setId(id);
        orderRetVo.setConsignee(consignee);
        orderRetVo.setCouponActivityId(couponActivityId);
        orderRetVo.setDiscountPrice(discountPrice);
        orderRetVo.setFreightPrice(freightPrice);
        orderRetVo.setGmtCreateTime(gmtCreate);
        orderRetVo.setMessage(message);
        orderRetVo.setCouponId(couponId);
        return  orderRetVo;
    }

}
