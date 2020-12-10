package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.RefundPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopsPaymentsInfoRetVo {
    private Long id;
    private Long paymentId;
    private Long amout;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long orderId;
    private Long aftersaleId;
    public ShopsPaymentsInfoRetVo(RefundPo po){
        this.id=po.getId();
        this.paymentId=po.getPaymentId();
        this.amout=po.getAmout();
        this.state=po.getState();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.orderId=po.getOrderId();
        this.aftersaleId=po.getAftersaleId();

    }

}
