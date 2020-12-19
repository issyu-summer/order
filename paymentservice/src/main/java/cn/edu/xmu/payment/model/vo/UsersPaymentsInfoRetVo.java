package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.ooad.util.TimeFormat;
import cn.edu.xmu.payment.model.po.RefundPo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UsersPaymentsInfoRetVo
 * 用户支付信息Vo
 * @author 王子扬 30320182200071
 * @date 2020/12/11
 */
@Data
public class UsersPaymentsInfoRetVo {
    private Long id;
    private Long paymentId;
    private Long amount;
    private Byte state;
    private String gmtCreate;
    private String gmtModified;
    private Long orderId;
    private Long aftersaleId;
    public UsersPaymentsInfoRetVo(RefundPo po){
        this.id=po.getId();
        this.paymentId=po.getPaymentId();
        this.amount=po.getAmount();
        this.state=po.getState();
        if(po.getGmtCreate()!=null){
            this.gmtCreate= TimeFormat.localDateTimeToString(po.getGmtCreate());
        }
        if(po.getGmtModified()!=null){
        this.gmtModified=TimeFormat.localDateTimeToString(po.getGmtModified());
        }
        this.orderId=po.getOrderId();
        this.aftersaleId=po.getAftersaleId();
    }

}