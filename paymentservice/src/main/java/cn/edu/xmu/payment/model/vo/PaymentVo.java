package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.payment.model.po.PaymentPo;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;
@Data
public class PaymentVo {
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
    public PaymentVo(){

    }
    public PaymentVo(PaymentPo po){
        this.id=po.getId();
        this.orderId=po.getId();
        this.aftersaleId=po.getAftersaleId();
        this.amount=po.getAmount();
        this.actualAmount=po.getActualAmount();
        this.payTime=po.getPayTime();
        //this.paymentPattern=po.getPaymentPattern();
        this.state=po.getState();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }
}
