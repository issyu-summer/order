package cn.edu.xmu.order.model.vo;

import cn.edu.xmu.order.model.bo.OrderStateBo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 11:09
 */
@Data
public class OrderStateRetVo {

    private int code;
    private String name;

    public OrderStateRetVo(OrderStateBo bo){
        this.setCode(bo.getOrderState().getCode());
        this.setName(bo.getOrderState().getMessage());
    }
}
