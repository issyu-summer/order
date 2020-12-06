package cn.edu.xmu.order.model.bo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.vo.OrderBriefRetVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单Bo
 * @author 30320182200071 王子扬
 * Created at 2020/12/3 18:19
 **/
@Data
public class OrderBrief implements VoObject {
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

    public OrderBrief(OrderPo po){
        this.id=po.getId();
        this.customerId=po.getCustomerId();
        this.shopId=po.getShopId();
        this.pid=po.getShopId();
        this.orderType=po.getOrderType();
        this.state=po.getState();
        this.subState=po.getSubstate();
        this.gmtCreate=po.getGmtCreate();
        this.originPrice=po.getOriginPrice();
        this.discountPrice=po.getDiscountPrice();
        this.freightPrice=po.getFreightPrice();
    }

    @Override
    public OrderBriefRetVo createVo(){
        OrderBriefRetVo orderRetVo=new OrderBriefRetVo();
        orderRetVo.setId(id);
        orderRetVo.setCustomerId(customerId);
        orderRetVo.setShopId(shopId);
        orderRetVo.setPid(pid);
        orderRetVo.setOrderType(orderType);
        orderRetVo.setState(state);
        orderRetVo.setSubState(subState);
        orderRetVo.setGmtCreate(gmtCreate);
        orderRetVo.setOriginPrice(originPrice);
        orderRetVo.setDiscountPrice(discountPrice);
        orderRetVo.setFreightPrice(freightPrice);
        return orderRetVo;
    }
    @Override
    public OrderBriefRetVo createSimpleVo(){
        OrderBriefRetVo orderRetVo=new OrderBriefRetVo();
        orderRetVo.setId(id);
        orderRetVo.setCustomerId(customerId);
        orderRetVo.setShopId(shopId);
        orderRetVo.setPid(pid);
        orderRetVo.setOrderType(orderType);
        orderRetVo.setState(state);
        orderRetVo.setSubState(subState);
        orderRetVo.setGmtCreate(gmtCreate);
        orderRetVo.setOriginPrice(originPrice);
        orderRetVo.setDiscountPrice(discountPrice);
        orderRetVo.setFreightPrice(freightPrice);
        return orderRetVo;
    }
}
