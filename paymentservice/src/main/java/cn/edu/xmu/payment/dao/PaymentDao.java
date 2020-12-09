package cn.edu.xmu.payment.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.payment.mapper.PaymentPoMapper;
import cn.edu.xmu.payment.mapper.RefundPoMapper;
import cn.edu.xmu.payment.model.po.PaymentPo;
import cn.edu.xmu.payment.model.po.PaymentPoExample;
import cn.edu.xmu.payment.model.vo.PaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentDao {
    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */

    @Autowired
    private PaymentPoMapper paymentPoMapper;

    public ReturnObject getAfterSalesPayments(Long userId, Long id) {

        ReturnObject<Object> retObj = null;
        try {
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
            if (!paymentPos.isEmpty()){
                for (PaymentPo po : paymentPos) {
                    PaymentVo paymentVo=new PaymentVo(po);
                    return new ReturnObject<>(paymentVo);
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
        return retObj;
    }
}
