package cn.edu.xmu.payment.model.vo;

import lombok.Data;

/**
* @author 史韬韬
* @date 2020/12/10
* 为售后单创建支付的传入对象
 */
@Data
public class AfterSalePaymentVo {
    private Long price;
    private Byte paymentPattern;
}
