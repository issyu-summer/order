package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.payment.model.bo.PaymentStateBo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 19:01
 */
@Data
public class PaymentStateVo {

    private Byte code;
    private String message;

    public PaymentStateVo(){

    }
    public PaymentStateVo(PaymentStateBo paymentStateBo){
        //this.code= paymentStateBo.getCode();
        //this.message= paymentStateBo.getMessage();
        this.setCode(paymentStateBo.getCode());
        this.setMessage(paymentStateBo.getMessage());
    }

}
