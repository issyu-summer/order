package cn.edu.xmu.order.model.vo;

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
    private String gmtCreateTime;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;

    public OrderSimpleInfoRetVo(OrderSimpleInfoBo bo){
        this.setId(bo.getId());
        this.setCustomerId(bo.getCustomerId());
        this.setFreightPrice(bo.getFreightPrice());
        this.setDiscountPrice(bo.getDiscountPrice());
        this.setGmtCreateTime(bo.getGmtCreateTime().toString());
        this.setOrderType(bo.getOrderType());
        this.setOriginPrice(bo.getOriginPrice());
        this.setState(bo.getState());
        this.setPid(bo.getPid());
        this.setShopId(bo.getShopId());
        this.setSubState(bo.getSubState());
    }
}
