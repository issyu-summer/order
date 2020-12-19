package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.ooad.util.TimeFormat;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.RefundPo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopsPaymentsInfoRetVo {
    private Long id;
    private Long paymentId;
    private Long amount;
    private Byte state;
    private String gmtCreate;
    private String gmtModified;
    private Long orderId;
    private Long aftersaleId;
    public ShopsPaymentsInfoRetVo(RefundPo po){
        this.id=po.getId();
        this.paymentId=po.getPaymentId();
        this.amount=po.getAmount();
        this.state=po.getState();
        this.gmtCreate= TimeFormat.localDateTimeToString(po.getGmtCreate());
        this.gmtModified=TimeFormat.localDateTimeToString(po.getGmtModified());
        this.orderId=po.getOrderId();
        this.aftersaleId=po.getAftersaleId();

    }

}
