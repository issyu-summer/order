package cn.edu.xmu.payment.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.PaymentStateCode;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.vo.PaymentStateVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 18:54
 */
@Data
public class PaymentStateBo implements VoObject, Serializable {

    private Byte code;
    private String message;

    public PaymentStateBo(){

    }
    public PaymentStateBo(PaymentPo paymentPo){
        //this.code= paymentPo.getState();
        //this.message = PaymentStateCode.getMessageByCode(paymentPo.getState());
        this.setCode(paymentPo.getState());
        this.setMessage(PaymentStateCode.getMessageByCode(paymentPo.getState()));
    }
    @Override
    public Object createVo() {
        PaymentStateVo paymentStateVo = new PaymentStateVo(this);
        return paymentStateVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
