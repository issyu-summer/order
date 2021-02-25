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
    private String orderSn;

    public CustomerVo customer=new CustomerVo();

    public ShopVo shop=new ShopVo();

    private Long pid;
    private Byte orderType;
    private Byte state;
    private Byte subState;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private LocalDateTime confirmTime;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private Integer rebateNum;
    private String message;
    private Long regionId;
    private String address;
    private String mobile;
    private String consignee;
    private Long couponId;
    private Long grouponId;
    private Long presaleId;
    private String shipmentSn;

    public OrderItemVo orderItem=new OrderItemVo();
}