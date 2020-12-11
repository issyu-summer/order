package cn.edu.xmu.payment.model.bo;

import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import cn.edu.xmu.payment.model.vo.PaymentRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/*
* @author 史韬韬
* @date 2020/12/9
 */
@Data
public class PaymentBo {
    private Long id;
    private Long amount;
    private Long actualAmount;
    private Byte paymentPattern;
    private LocalDateTime payTime;
    private String paySn;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Long orderId;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long aftersaleId;

    public PaymentBo(){}

    public PaymentBo(PaymentPo paymentPo){
        id=paymentPo.getId();
        amount=paymentPo.getAmount();
        actualAmount=paymentPo.getActualAmount();
        //paymentPattern=paymentPo.getPaymentPattern();
        payTime=paymentPo.getPayTime();
        paySn=paymentPo.getPaySn();
        beginTime=paymentPo.getBeginTime();
        endTime=paymentPo.getEndTime();
        orderId=paymentPo.getOrderId();
        state=paymentPo.getState();
        gmtCreate=paymentPo.getGmtCreate();
        gmtModified=paymentPo.getGmtModified();
        aftersaleId=paymentPo.getAftersaleId();
    }

    public PaymentRetVo createRetVo(){
        PaymentRetVo paymentRetVo=new PaymentRetVo();
        paymentRetVo.setId(id);
        paymentRetVo.setOrderId(orderId);
        paymentRetVo.setAftersaleId(aftersaleId);
        paymentRetVo.setAmount(amount);
        paymentRetVo.setActualAmount(actualAmount);
        paymentRetVo.setBeginTime(beginTime);
        paymentRetVo.setEndTime(endTime);
        paymentRetVo.setGmtCreate(gmtCreate);
        paymentRetVo.setGmtModified(gmtModified);
        paymentRetVo.setPaymentPattern(paymentPattern);
        paymentRetVo.setPayTime(payTime);
        paymentRetVo.setState(state);
        return paymentRetVo;
    }
    public PaymentPo createAftersalePaymentPo(Long id, AfterSalePaymentVo afterSalePaymentVo,Long orderId){
        PaymentPo paymentPo=new PaymentPo();
        paymentPo.setAftersaleId(id);
        this.aftersaleId=id;
        paymentPo.setAmount(afterSalePaymentVo.getPrice());
        this.amount=afterSalePaymentVo.getPrice();
        paymentPo.setActualAmount(afterSalePaymentVo.getPrice());
        this.actualAmount=afterSalePaymentVo.getPrice();
        //paymentPo.setPaymentPattern(afterSalePaymentVo.getPaymentPattern());
        this.paymentPattern=afterSalePaymentVo.getPaymentPattern();
        paymentPo.setState((byte)0);//支付成功
        this.state=(byte)0;
        paymentPo.setPayTime(LocalDateTime.now());
        this.payTime=LocalDateTime.now();
        paymentPo.setGmtCreate(LocalDateTime.now());
        this.gmtCreate=LocalDateTime.now();
        paymentPo.setGmtModified(LocalDateTime.now());
        this.gmtModified=LocalDateTime.now();
        UUID paysn=UUID.randomUUID();//使用uuid生成支付编号
        paymentPo.setPaySn(String.valueOf(paysn));
        this.paySn=String.valueOf(paysn);
        paymentPo.setOrderId(orderId);
        this.orderId=orderId;
        return paymentPo;
    }
}
