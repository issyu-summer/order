package cn.edu.xmu.order.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 4:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoBo implements VoObject {

    private Long id;

    /**
     * need CustomerBO
     */
    private CustomerRetVo customerVo;
    /**
     * need ShopBo
     */
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
     * po->bo
     * @author issyu 30320182200070
     * param shop信息，user信息,来自其他接口。
     * controller层调用外部接口,将信息放入dao层，在dao层组装ReturnObject.
     * public OrderInfoBo(OrderPo orderPo,shopInfo,customerInfo)
     */
    public OrderInfoBo(OrderPo orderPo){

    }
    /**
     * bo->vo
     * @author issyu 30320182200070
     * @return
     */
    @Override
    public Object createVo() {
        OrderInfoRetVo orderInfoVo = new OrderInfoRetVo(this);
        return orderInfoVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
