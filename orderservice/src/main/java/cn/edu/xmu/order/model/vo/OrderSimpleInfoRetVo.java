package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.ooad.util.TimeFormat;
import cn.edu.xmu.order.model.bo.OrderSimpleInfoBo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 16:13
 */
@Data
public class OrderSimpleInfoRetVo {

    private Long id;
    private Long customerId;
    private Long shopId;
    private Long pid;
    private Byte orderType;
    private Byte state;
    private Byte subState;
    private String gmtCreate;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private Long grouponId;
    private Long presaleId;
    private String shipmentSn;

    public OrderSimpleInfoRetVo(OrderSimpleInfoBo bo){
        this.setId(bo.getId());
        this.setCustomerId(bo.getCustomerId());
        this.setFreightPrice(bo.getFreightPrice());
        this.setDiscountPrice(bo.getDiscountPrice());
        this.setGmtCreate(TimeFormat.localDateTimeToString(bo.getGmtCreate()));
        this.setOrderType(bo.getOrderType());
        this.setOriginPrice(bo.getOriginPrice());
        this.setState(bo.getState());
        this.setPid(bo.getPid());
        this.setShopId(bo.getShopId());
        this.setSubState(bo.getSubState());
        this.setGrouponId(bo.getGrouponId());
        this.setPresaleId(bo.getPresaleId());
        this.setShipmentSn(bo.getShipmentSn());
    }
}
