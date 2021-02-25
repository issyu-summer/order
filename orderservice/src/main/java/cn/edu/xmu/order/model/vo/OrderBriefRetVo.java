package cn.edu.xmu.order.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户订单信息返回 Vo
 * @author 30320182200071 王子扬
 * Created at 2020/12/4 17:30
 **/
@Data
public class OrderBriefRetVo {
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
}
