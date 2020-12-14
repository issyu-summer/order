package cn.edu.xmu.payment.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.PaymentPatternCode;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.vo.PaymentPatternRetVo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 11:26
 */
@Data
public class PaymentPatternBo implements VoObject {

    private String payPattern;
    private String name;

    public PaymentPatternBo(PaymentPo paymentPo){
        this.setPayPattern(paymentPo.getPaymentPattern());
        this.setName(PaymentPatternCode.getPatternNameByPayment(paymentPo.getPaymentPattern()));
    }
    @Override
    public Object createVo() {
        PaymentPatternRetVo paymentPatternRetVo = new PaymentPatternRetVo(this);
        return paymentPatternRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
