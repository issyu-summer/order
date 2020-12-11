package cn.edu.xmu.payment.model.bo;

import cn.edu.xmu.payment.model.po.RefundPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefundBo {
    private Long id;
    private Long paymentId;
    private Long amount;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long orderId;
    private Long aftersaleId;
    public RefundBo(Long paymentId,Long amount,Long orderId,Long aftersaleId){
        this.paymentId=paymentId;
        this.amount=amount;
        this.state=1;
        this.orderId=orderId;
        this.aftersaleId=aftersaleId;
        this.gmtCreate=LocalDateTime.now();
        this.gmtModified=LocalDateTime.now();
    }

    public RefundPo createPo() {
        RefundPo refundPo=new RefundPo();
        refundPo.setPaymentId(this.paymentId);
        //refundPo.setAmout(this.amount);
        refundPo.setState(this.state);
        refundPo.setGmtCreate(this.gmtCreate);
        refundPo.setGmtModified(this.gmtModified);
        refundPo.setOrderId(this.orderId);
        refundPo.setAftersaleId(this.aftersaleId);
        return refundPo;
    }
}
