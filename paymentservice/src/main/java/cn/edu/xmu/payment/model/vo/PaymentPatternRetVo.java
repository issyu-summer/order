package cn.edu.xmu.payment.model.vo;

import cn.edu.xmu.payment.model.bo.PaymentPatternBo;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 11:17
 */
@Data
public class PaymentPatternRetVo {
    private String payPattern;
    private String name;

    public PaymentPatternRetVo(PaymentPatternBo paymentPatternBo){
        this.setName(paymentPatternBo.getName());
        this.setPayPattern(paymentPatternBo.getPayPattern());
    }
}
