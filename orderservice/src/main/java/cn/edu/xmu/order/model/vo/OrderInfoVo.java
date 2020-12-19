package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderPo;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * orderInfo
 * @author issyu 30320182200070
 * @date 2020/12/4 23:30
 */
@Data
public class OrderInfoVo {
    /**
     *这里的orderItemVo的orderId全是一样的。
     * for(OrderItemPo orderItemPo:orderItemPos)
     *          insert(record);
     */
    private List<OrderItemVo> orderItemVos;
    private String consignee;
    private Long regionId;
    private String address;
    private String mobile;
    private String message;
    private Long couponId;
    private Long couponActivityId;
    private Long presaleId;
    private Long grouponId;

    public OrderPo getOrderPo(
            Long userId,Long originPrice,
            Long freightPrice,Long discount,
            Long grouponDiscount) {

        OrderPo orderPo = new OrderPo();
        orderPo.setOriginPrice(originPrice);
        orderPo.setFreightPrice(freightPrice);
        orderPo.setGrouponDiscount(grouponDiscount);
        orderPo.setDiscountPrice(discount);
        orderPo.setCustomerId(userId);
        orderPo.setMobile(this.getMobile());
        orderPo.setAddress(this.getAddress());
        orderPo.setRegionId(this.getRegionId());
        orderPo.setConsignee(this.getConsignee());
        orderPo.setGmtCreate(LocalDateTime.now());
        //创建订单时将修改时间设置为当前时间。
        orderPo.setGmtModified(LocalDateTime.now());
        orderPo.setCouponId(this.getCouponId());
        orderPo.setMobile(this.getMobile());
        orderPo.setMessage(this.getMessage());
        orderPo.setPresaleId(this.getPresaleId());
        orderPo.setGrouponId(this.getGrouponId());
        orderPo.setPid(null);
        if(presaleId!=null){
            orderPo.setOrderType((byte) 2);
        }else if(grouponId!=null){
            orderPo.setOrderType((byte) 1);
        }else{
            orderPo.setOrderType((byte) 0);
        }

        orderPo.setBeDeleted((byte) 0);
        orderPo.setSubstate((byte) 11);
        orderPo.setRebateNum(0);
        orderPo.setConfirmTime(null);
        orderPo.setShipmentSn(null);
        return orderPo;
    }
    /**
     * 从OrderInfoVo中获取List<OrderItemPo>
     * @author issyu 30320182200070
     */
    public List<OrderItemPo> getOrderItemPoList(){
        List<OrderItemPo> orderItemPos = new ArrayList<>();
        for(OrderItemVo orderItemVo:this.getOrderItemVos()){
            orderItemPos.add(orderItemVo.getOrderItemPo());
        }
        return orderItemPos;
    }
/*
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
    */
}
