package cn.edu.xmu.order.model.vo;


import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 史韬韬
 * @date 2020/12/6
 * 此类是get order/{id}返回信息的类，如有改动请通知我--史韬韬
 */
@Data
public class OrderRetVo {
    private Long id;
    public CustomerVo customer=new CustomerVo();
    public ShopVo shop=new ShopVo();
    private Long pid;
    private Byte orderType;
    private Byte state;
    private Byte subState;
    private LocalDateTime gmtCreateTime;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private String message;
    private String consignee;
    private Long couponId;
    private Long couponActivityId;
    private Long grouponId;

    public OrderItemVo orderItem=new OrderItemVo();
}