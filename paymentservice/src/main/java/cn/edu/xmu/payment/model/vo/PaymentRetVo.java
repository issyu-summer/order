package cn.edu.xmu.payment.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/*
* @author 史韬韬
* @Date 2020/12/9
 */
@Data
public class PaymentRetVo {
     private Long id;
     private Long orderId;
     private Long aftersaleId;
     private Long amount;
     private Long actualAmount;
     private LocalDateTime payTime;
     private Byte paymentPattern;
     private Byte state;
     private LocalDateTime beginTime;
     private LocalDateTime endTime;
     private LocalDateTime gmtCreate;
     private LocalDateTime gmtModified;

}
