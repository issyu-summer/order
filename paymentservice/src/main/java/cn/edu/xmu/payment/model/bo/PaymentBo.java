package cn.edu.xmu.payment.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.RefundPo;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import cn.edu.xmu.payment.model.vo.PaymentRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author 史韬韬
 * @date 2020/12/9
 */
@Data
public class PaymentBo implements VoObject {
    private Long id;
    private Long amount;
    private Long actualAmount;
    private String paymentPattern;
    private LocalDateTime payTime;
    //private String paySn;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Long orderId;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long aftersaleId;

    public PaymentBo(){}

    public PaymentBo(PaymentPo paymentPo){
        //this.paySn = paymentPo.getPaySn();
        id=paymentPo.getId();
        amount=paymentPo.getAmount();
        actualAmount=paymentPo.getActualAmount();
        paymentPattern=paymentPo.getPaymentPattern();
        payTime=paymentPo.getPayTime();
        //paySn=paymentPo.getPaySn();
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
    public PaymentPo createAftersalePaymentPo(AfterSalePaymentVo afterSalePaymentVo, RefundPo refundPo){
        PaymentPo paymentPo=new PaymentPo();
        paymentPo.setAftersaleId(refundPo.getId());
        this.aftersaleId= refundPo.getId();
        paymentPo.setAmount(refundPo.getAmount());
        this.amount= refundPo.getAmount();
        paymentPo.setActualAmount(refundPo.getAmount());
        this.actualAmount=refundPo.getAmount();
        paymentPo.setPaymentPattern(afterSalePaymentVo.getPaymentPattern());
        this.paymentPattern=afterSalePaymentVo.getPaymentPattern();
        paymentPo.setState((byte)0);
        //支付成功
        this.state=(byte)0;
        paymentPo.setPayTime(LocalDateTime.now());
        this.payTime=LocalDateTime.now();
        paymentPo.setGmtCreate(LocalDateTime.now());
        this.gmtCreate=LocalDateTime.now();
        paymentPo.setGmtModified(LocalDateTime.now());
        this.gmtModified=LocalDateTime.now();
        UUID paysn=UUID.randomUUID();
        //使用uuid生成支付编号
        paymentPo.setPaySn(String.valueOf(paysn));
        //this.paySn=String.valueOf(paysn);
        paymentPo.setOrderId(refundPo.getOrderId());
        this.orderId= refundPo.getOrderId();
        return paymentPo;
    }

    public PaymentPo createPaymentPo() {
        PaymentPo paymentPo=new PaymentPo();
        paymentPo.setAmount(this.amount);
        paymentPo.setActualAmount(this.actualAmount);
        paymentPo.setPaymentPattern(this.paymentPattern);
        paymentPo.setPayTime(this.payTime);
        //paymentPo.setPaySn(this.paySn);
        paymentPo.setBeginTime(this.beginTime);
        paymentPo.setEndTime(this.endTime);
        paymentPo.setGmtCreate(this.gmtCreate);
        paymentPo.setGmtModified(this.gmtModified);
        paymentPo.setState(this.state);
        paymentPo.setAftersaleId(this.aftersaleId);
        paymentPo.setOrderId(this.orderId);
        return paymentPo;
    }


    public PaymentBo(AfterSalePaymentVo afterSalePaymentVo, Long amount, Long actual_amount,Long orderId) {
        this.amount=amount;
        this.actualAmount=actual_amount;
        if(afterSalePaymentVo.getPrice()!=0) {
            paymentPattern="001";
        }
        payTime=LocalDateTime.now();
        beginTime=LocalDateTime.now();
        gmtCreate=gmtModified=LocalDateTime.now();
        this.orderId=orderId;
        state=(byte)1;
    }
    @Override
    public Object createVo() {
        PaymentRetVo paymentRetVo = new PaymentRetVo(this);
        return paymentRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
