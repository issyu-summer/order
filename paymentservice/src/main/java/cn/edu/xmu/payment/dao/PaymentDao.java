package cn.edu.xmu.payment.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.payment.mapper.PaymentPoMapper;
import cn.edu.xmu.payment.mapper.RefundPoMapper;
import cn.edu.xmu.payment.model.bo.PaymentBo;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.PaymentPoExample;
import cn.edu.xmu.payment.model.po.RefundPo;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import cn.edu.xmu.payment.model.vo.PaymentRetVo;
import cn.edu.xmu.payment.model.vo.PaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentDao {

    @Autowired
    private PaymentPoMapper paymentPoMapper;
    @Autowired
    RefundPoMapper refundPoMapper;
    @Autowired
    private OrderPoMapper orderPoMapper;

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
            if (criteria.isValid()){
                List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
                for (PaymentPo po : paymentPos) {
                    //通过收货单orderId找到订单
                    OrderPo orderPo=orderPoMapper.selectByPrimaryKey(po.getOrderId());
                    //如果此订单是本人的
                    if(orderPo.getCustomerId().equals(userId)){
                        PaymentVo paymentVo=new PaymentVo(po);
                        return new ReturnObject<>(paymentVo);
                    }
                    else {
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);

                    }
                }
            }
            //空
            else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("操作资源不存在"));
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
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
            if (criteria.isValid()){
                for (PaymentPo po : paymentPos) {
                    //通过收货单orderId找到订单
                    OrderPo orderPo=orderPoMapper.selectByPrimaryKey(po.getOrderId());
                    //如果此订单是本店铺的
                    if(orderPo.getShopId().equals(shopId)){
                        PaymentVo paymentVo=new PaymentVo(po);
                        return new ReturnObject<>(paymentVo);
                    }
                    else {
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                    }
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
            PaymentPo paymentPo=paymentBo.createAftersalePaymentPo(id,afterSalePaymentVo,refundPo.getBillId());
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
            OrderPo orderPo=orderPoMapper.selectByPrimaryKey(paymentPo.getOrderId());
            if(!orderPo.getShopId().equals(shopId)){
                return new ReturnObject<>(ResponseCode.FIELD_NOTVALID,"商铺id与售后单id不符");
            }
            PaymentBo paymentBo=new PaymentBo(paymentPo);
            PaymentRetVo paymentRetVo=paymentBo.createRetVo();
            return new ReturnObject<PaymentRetVo>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }
}
