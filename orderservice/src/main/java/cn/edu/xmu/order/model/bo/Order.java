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
        this.setId(po.getId());
        this.setCustomerId(po.getCustomerId());
        this.setShopId(po.getShopId());
        this.setOrderSn(po.getOrderSn());
        this.setPid(po.getPid());
        this.setConsignee(po.getConsignee());
        this.setRegionId(po.getRegionId());
        this.setAddress(po.getAddress());
        this.setMobile(po.getMobile());
        this.setMessage(po.getMessage());
        this.setOrderType(po.getOrderType());
        this.setFreightPrice(po.getFreightPrice());
        this.setCouponId(po.getCouponId());
        this.setCouponActivityId(po.getCouponActivityId());
        this.setDiscountPrice(po.getDiscountPrice());
        this.setOriginPrice(po.getOriginPrice());
        this.setPresaleId(po.getPresaleId());
        this.setGrouponDiscount(po.getGrouponDiscount());
        this.setRebateNum(po.getRebateNum());
        this.setConfirmTime(po.getConfirmTime());
        this.setShipmentSn(po.getShipmentSn());
        this.setState(po.getState());
        this.setSubstate(po.getSubstate());
        this.setBeDeleted(po.getBeDeleted());
        this.setGmtCreate(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
        this.setGrouponId(po.getGrouponId());
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

    /**
     * @author
     * @date
     * @return
     */
    public OrderPo getOrderPo() {
        OrderPo po = new OrderPo();
        po.setId(this.getId());
        po.setCustomerId(this.getCustomerId());
        po.setShopId(this.getShopId());
        po.setOrderSn(this.getOrderSn());
        po.setPid(this.getPid());
        po.setConsignee(this.getConsignee());
        po.setRegionId(this.getRegionId());
        po.setAddress(this.getAddress());
        po.setMobile(this.getMobile());
        po.setMessage(this.getMessage());
        po.setOrderType(this.getOrderType());
        po.setFreightPrice(this.getFreightPrice());
        po.setCouponId(this.getCouponId());
        po.setCouponActivityId(this.getCouponActivityId());
        po.setDiscountPrice(this.getDiscountPrice());
        po.setOriginPrice(this.getOriginPrice());
        po.setPresaleId(this.getPresaleId());
        po.setGrouponDiscount(this.getGrouponDiscount());
        po.setRebateNum(this.getRebateNum());
        po.setConfirmTime(this.getConfirmTime());
        po.setShipmentSn(this.getShipmentSn());
        po.setState(this.getState());
        po.setSubstate(this.getSubstate());
        po.setBeDeleted(this.getBeDeleted());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        po.setGrouponId(this.getGrouponId());
        return po;
    }
}
