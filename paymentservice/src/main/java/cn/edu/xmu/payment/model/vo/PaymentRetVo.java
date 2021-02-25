package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.payment.model.bo.PaymentBo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
* @author 史韬韬
* @Date 2020/12/9
 */
@Data
@NoArgsConstructor
public class PaymentRetVo {
     private Long id;
     private Long orderId;
     private Long aftersaleId;
     private Long amount;
     private Long actualAmount;
     private LocalDateTime payTime;
     private String paymentPattern;
     private Byte state;
     private LocalDateTime beginTime;
     private LocalDateTime endTime;
     private LocalDateTime gmtCreate;
     private LocalDateTime gmtModified;
     //private String paySn;


     public PaymentRetVo(PaymentBo paymentBo){
          this.setId(paymentBo.getId());
          this.setOrderId(paymentBo.getOrderId());
          this.setAftersaleId(paymentBo.getAftersaleId());
          this.setAmount(paymentBo.getAmount());
          this.setActualAmount(paymentBo.getActualAmount());
          this.setPayTime(paymentBo.getPayTime());
          this.setBeginTime(paymentBo.getBeginTime());
          this.setEndTime(paymentBo.getEndTime());
          this.setState(paymentBo.getState());
          this.setPaymentPattern(paymentBo.getPaymentPattern());
          this.setGmtCreate(paymentBo.getGmtCreate());
          this.setGmtModified(paymentBo.getGmtModified());
          this.setPaymentPattern(paymentBo.getPaymentPattern());
//          this.setAftersaleId(paymentBo.getAftersaleId());
          //this.setPaySn(paymentBo.getPaySn());
     }

}
