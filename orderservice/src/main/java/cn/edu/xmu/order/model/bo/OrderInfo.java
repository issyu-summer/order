package cn.edu.xmu.order.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.vo.OrderInfoVo;
import cn.edu.xmu.order.model.vo.OrderItemVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author issyu 30320182200070
 * @date 2020/12/4 23:38
 * 价格等因素
 */
@Data
public class OrderInfo implements VoObject, Serializable {

    private Long customerId;
    private OrderItemVo orderItemVo;
    private String consignee;
    private Long regionId;
    private String address;
    private String mobile;
    private String message;
    private Long couponId;
    private Long presaleId;
    private Long grouponId;
    private LocalDateTime gmtCreate;

    public OrderPo getOrderPo(){
        OrderPo orderPo = new OrderPo();
        orderPo.setAddress(this.getAddress());
        orderPo.setConsignee(this.getConsignee());
        orderPo.setCouponId(this.getCouponId());
        orderPo.setGmtCreate(this.getGmtCreate());
        orderPo.setBeDeleted((byte) 0);
        orderPo.setGrouponId(this.getGrouponId());
        orderPo.setMessage(this.getMessage());
        orderPo.setMobile(this.getMobile());
        orderPo.setPresaleId(this.getPresaleId());
        orderPo.setRegionId(this.getRegionId());
        orderPo.setCustomerId(this.getCustomerId());
        /*
        价格、运费等等
         */
        return orderPo;
    }

    public OrderItemPo getOrderItemPo(Long orderId){
        OrderItemPo orderItemPo = new OrderItemPo();
        orderItemPo.setOrderId(orderId);
        orderItemPo.setGoodsSkuId(this.orderItemVo.getSkuId());
        orderItemPo.setQuantity(this.orderItemVo.getQuantity());
        orderItemPo.setCouponActivityId(this.orderItemVo.getCouponActId());
        return orderItemPo;
    }
    @Override
    public Object createVo() {
        OrderInfoVo vo = new OrderInfoVo(this);
        return vo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
