package cn.edu.xmu.order.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.order.model.vo.OrderStateRetVo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 15:22
 */
@Data
public class OrderStateBo implements VoObject {

    private OrderStateCode orderState;

    public OrderStateBo( OrderStateCode orderState){
        this.orderState=orderState;
    }

    @Override
    public Object createVo() {
        OrderStateRetVo retVo = new OrderStateRetVo(this);
        return retVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
