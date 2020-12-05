package cn.edu.xmu.order.model.vo;


import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRetVo {
    private Long id;
    @Data
    class customer {
        private Long customerId;
        private String userName;
        private String realName;
    }
    @Data
    class shop {
        private Long id;
        private String name;
        private LocalDateTime gmtCreateTime;
        private LocalDateTime gmtModiTime;
    }
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
    @Data
    class orderItems {
        private Long skuId;
        private Long orderId;
        private String name;
        private Long quantity;
        private Long price;
        private Long discount;
        private Long couponId;
        private Long couponActivityId;
        private Long beSharedId;
    }
}