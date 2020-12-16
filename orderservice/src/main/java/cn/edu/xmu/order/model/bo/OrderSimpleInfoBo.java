package cn.edu.xmu.order.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.vo.OrderSimpleInfoRetVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 16:19
 */
@Data
public class OrderSimpleInfoBo implements VoObject {

    private Long id;
    private Long customerId;
    private Long shopId;
    private Long pid;
    private Byte orderType;
    private Byte state;
    private Byte subState;
    private LocalDateTime gmtCreate;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private Long grouponId;
    private Long presaleId;
    private String shipmentSn;

    public OrderSimpleInfoBo(OrderPo po){
        this.setId(po.getId());
        this.setCustomerId(po.getCustomerId());
        this.setFreightPrice(po.getFreightPrice());
        this.setDiscountPrice(po.getDiscountPrice());
        this.setGmtCreate(po.getGmtCreate());
        this.setOrderType(po.getOrderType());
        this.setOriginPrice(po.getOriginPrice());
        this.setState(po.getState());
        this.setPid(po.getPid());
        this.setShopId(po.getShopId());
        this.setSubState(po.getSubstate());
        this.setGrouponId(po.getGrouponId());
        this.setPresaleId(po.getPresaleId());
        this.setShipmentSn(po.getShipmentSn());
    }

    @Override
    public Object createVo() {
        OrderSimpleInfoRetVo vo = new OrderSimpleInfoRetVo(this);
        return vo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
