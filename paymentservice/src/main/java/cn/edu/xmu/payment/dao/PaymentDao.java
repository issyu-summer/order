package cn.edu.xmu.payment.dao;

import cn.edu.xmu.inner.service.OrderInnerService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.PaymentStateCode;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.payment.mapper.PaymentPoMapper;
import cn.edu.xmu.payment.mapper.RefundPoMapper;
import cn.edu.xmu.payment.model.bo.PaymentBo;
import cn.edu.xmu.payment.model.bo.PaymentPatternBo;
import cn.edu.xmu.payment.model.bo.PaymentStateBo;
import cn.edu.xmu.payment.model.bo.RefundBo;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.PaymentPoExample;
import cn.edu.xmu.payment.model.po.RefundPo;
import cn.edu.xmu.payment.model.po.RefundPoExample;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import cn.edu.xmu.payment.model.vo.PaymentRetVo;
import cn.edu.xmu.payment.model.vo.PaymentVo;
import cn.edu.xmu.payment.model.vo.ShopsPaymentsInfoRetVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PaymentDao {

    @Autowired
    private PaymentPoMapper paymentPoMapper;
    @Autowired
    private RefundPoMapper refundPoMapper;

    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */

    public ReturnObject getAfterSalesPayments(Long userId, Long id) {

        ReturnObject<Object> retObj = null;
        try {
            //获得afterSaleId=id的支付单
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            //判断是否有afterSale;
            List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
            List<PaymentVo> paymentVos = new ArrayList<PaymentVo>(paymentPos.size());
            if (criteria.isValid()){
                for (PaymentPo po : paymentPos) {
                    //需通过收货单售后单Id找到售后验证是否是本人的售后单
                    PaymentVo paymentVo=new PaymentVo(po);
                    paymentVos.add(paymentVo);
                }
                return new ReturnObject<>(paymentVos);
            }
            else{
                return new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    } /**
     * 管理员查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */

    public ReturnObject getAfterSalesPayments(Long userId,Long shopId, Long id) {

        try {
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
            List<PaymentVo> paymentVos = new ArrayList<PaymentVo>(paymentPos.size());
            if (criteria.isValid()){
                for (PaymentPo po : paymentPos) {
                    //需通过售后单Id找到售后单，确定是否是shopId的订单
                    PaymentVo paymentVo=new PaymentVo(po);
                    paymentVos.add(paymentVo);
                }
                return new ReturnObject<>(paymentVos);
            }
            else{
                return new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /*
     * @author 史韬韬
     * @date 2020/12/9
     * 买家查询自己的支付信息
     */
    public ReturnObject<PaymentRetVo> getPaymentById(Long id){
        try{
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andOrderIdEqualTo(id);
            List<PaymentPo> paymentPoList=paymentPoMapper.selectByExample(paymentPoExample);
            PaymentPo paymentPo=paymentPoList.get(0);
            PaymentBo paymentBo=new PaymentBo(paymentPo);
            PaymentRetVo paymentRetVo=paymentBo.createRetVo();
            return new ReturnObject<PaymentRetVo>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }

    /*
     * @author 史韬韬
     * @date 2020/12/10
     * 买家为售后单创建支付单
     */
    public ReturnObject<PaymentRetVo> createPaymentForAftersale(Long id, AfterSalePaymentVo afterSalePaymentVo){
        try{
            PaymentBo paymentBo=new PaymentBo();
            RefundPo refundPo=refundPoMapper.selectByPrimaryKey(id);
            PaymentPo paymentPo=paymentBo.createAftersalePaymentPo(id,afterSalePaymentVo,refundPo.getOrderId());
            paymentPoMapper.insertSelective(paymentPo);
            PaymentRetVo paymentRetVo=paymentBo.createRetVo();
            return new ReturnObject<PaymentRetVo>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }
    /*
     * @author 史韬韬
     * @date 2020/12/10
     * 管理员查看订单支付信息
     */
    public ReturnObject<PaymentRetVo> getPaymentByOrderIdAndShopId(Long id,Long shopId){
        try{
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andOrderIdEqualTo(id);
            List<PaymentPo> paymentPoList=paymentPoMapper.selectByExample(paymentPoExample);
            PaymentPo paymentPo=paymentPoList.get(0);
//            OrderPo orderPo=orderPoMapper.selectByPrimaryKey(paymentPo.getOrderId());
//            if(!orderPo.getShopId().equals(shopId)){
//                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,"商铺id与售后单id不符");
//            }
            PaymentBo paymentBo=new PaymentBo(paymentPo);
            PaymentRetVo paymentRetVo=paymentBo.createRetVo();
            return new ReturnObject<PaymentRetVo>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }
    /*
     *管理员查询订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:13
     */

    public ReturnObject getShopsOrdersRefunds(Long shopId, Long id) {
        try {
            //获得订单id=退款单id的退款单
            RefundPoExample refundPoExample= new RefundPoExample();
            RefundPoExample.Criteria criteria = refundPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<RefundPo> refundPos = refundPoMapper.selectByExample(refundPoExample);
            if (criteria.isValid()) {
                for (RefundPo po : refundPos) {
                    ShopsPaymentsInfoRetVo shopsOrdersRefundsInfoRetVo = new ShopsPaymentsInfoRetVo(po);
                    return new ReturnObject<>(shopsOrdersRefundsInfoRetVo);
                }
            } else {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        } catch(
                DataAccessException e)

        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch(
                Exception e)

        {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }
    /*
     *管理员查询售后订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:10
     */

    public ReturnObject getShopsAftersalesRefunds(Long shopId, Long id) {
        try {
            //获得afterSaleId=id的退款单
            RefundPoExample refundPoExample=new RefundPoExample();
            RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<RefundPo> refundPos=refundPoMapper.selectByExample(refundPoExample);
            if (criteria.isValid()){
                for (RefundPo po : refundPos) {
                    ShopsPaymentsInfoRetVo shopsAftersalesPaymentsInfoRetVo=new ShopsPaymentsInfoRetVo(po);
                    return new ReturnObject<>(shopsAftersalesPaymentsInfoRetVo);
                }
            }
            else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }
    /**
     *管理员创建退款信息
     * @author 王薪蕾
     * @date 2020/12/11
     */
    public ReturnObject postRefunds(Long shopId, Long id,Long amount) {
        PaymentPo paymentPo=paymentPoMapper.selectByPrimaryKey(id);
        //未查找到
        if(paymentPo==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //查询是否已有退款
        RefundPoExample refundPoExample=new RefundPoExample();
        RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
        criteria.andPaymentIdEqualTo(id);
        List<RefundPo> refundPos = refundPoMapper.selectByExample(refundPoExample);
        if (!refundPos.isEmpty()) {
            for (RefundPo po : refundPos) {
                //如果找到payment的退款，且退款成功，则不在进行退款
                if (po.getPaymentId().equals(id)&&po.getState().equals((byte)1)) {
                    return new ReturnObject<>(ResponseCode.REFUND_MORE,String.format("该支付已退款"));
                }
            }
        }
        //支付未成功或退款大于付款
        if(paymentPo.getState()!=(byte)1||paymentPo.getAmount()<amount)
        {
            return new ReturnObject<>(ResponseCode.REFUND_MORE);
        }
        //未验证shopId与paymen的shopid，通过order或aftersale判断
        RefundBo refundBo=new RefundBo(id,amount,paymentPo.getOrderId(),paymentPo.getAftersaleId());
        RefundPo refundPo=refundBo.createPo();
        refundPoMapper.insert(refundPo);
        refundBo.setId(refundPo.getId());
        //改变order相应状态-->13
        //改变其他模块售后的状态
        return new ReturnObject<>(refundBo);
    }

    /**
     * 通过userId获取订单Id(Dubbo),通过订单id获取支付状态
     * @author issyu 30320182200070
     * @date 2020/12/12 18:45
     * 不在dao层实装DubboReference
     */
    public ReturnObject getPaymentStateByOrderIds(List<Long> orderIds){

        PaymentPoExample paymentPoExample = new PaymentPoExample();
        PaymentPoExample.Criteria criteria = paymentPoExample.createCriteria();

        criteria.andOrderIdIn(orderIds);
        List<PaymentStateBo> paymentStateBos = new ArrayList<>();
        try{
            List<PaymentPo> paymentPos = paymentPoMapper.selectByExample(paymentPoExample);
            for(PaymentPo paymentPo:paymentPos){
                PaymentStateBo paymentStateBo = new PaymentStateBo();
                paymentStateBo.setCode(paymentPo.getState());
                paymentStateBo.setMessage(PaymentStateCode.getMessageByCode(paymentPo.getState()));
                paymentStateBos.add(paymentStateBo);
            }
        }catch (DataAccessException e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库内部错："+e.getMessage()));
        }
        return new ReturnObject<List>(paymentStateBos);
    }

    /**
     * 通过userId获取订单Id(Dubbo),通过订单id获取支付方式
     * @author issyu 30320182200070
     * @date 2020/12/14 11:31
     */
    public ReturnObject getPayPatternsByOrderId(List<Long> orderIds){

        PaymentPoExample paymentPoExample = new PaymentPoExample();
        PaymentPoExample.Criteria criteria =paymentPoExample.createCriteria();
        criteria.andOrderIdIn(orderIds);
        List<PaymentPatternBo> paymentPatternBos = new ArrayList<>();

        try{
            List<PaymentPo> paymentPos = paymentPoMapper.selectByExample(paymentPoExample);

            for(PaymentPo paymentPo:paymentPos){
                paymentPatternBos.add(new PaymentPatternBo(paymentPo));
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库内部错误："+e.getMessage()));
        }
        return new ReturnObject<>(paymentPatternBos);
    }

}
