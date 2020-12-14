package cn.edu.xmu.payment.service.impl;

import cn.edu.xmu.inner.service.PaymentInnerService;
import cn.edu.xmu.payment.mapper.RefundPoMapper;
import cn.edu.xmu.payment.model.po.RefundPo;
import cn.edu.xmu.payment.model.po.RefundPoExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.sql.Ref;
import java.util.List;

/**
 * provider
 * @author issyu 30320182200070
 * @date 2020/12/14 1:07
 */
@Slf4j
@DubboService
public class PaymentInnerServiceImpl implements PaymentInnerService {

    @Autowired
    private RefundPoMapper refundPoMapper;

    /**
     * 根据订单id更新退款状态
     * @param orderId
     * @return
     */
    @Override
    public Boolean updateRefundStateByOrderId(Long orderId) {

        RefundPoExample refundPoExample = new RefundPoExample();
        RefundPoExample.Criteria criteria = refundPoExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);

        try{
            RefundPo refundPo = new RefundPo();
            List<RefundPo> refundPos = refundPoMapper.selectByExample(refundPoExample);
            refundPos.get(0);
            //if(refundPos == null){
                //log.debug("没有找到合适的退款单");
            //}else{
               // System.out.println(1);
               // System.out.println();
               // System.out.println(refundPo.getId());

               // refundPo.setState((byte)1);
            //}
            int ret = refundPoMapper.updateByExampleSelective(refundPo,refundPoExample);
            if(ret == 0){
                return false;
            }else{
                return true;
            }
        }catch (DataAccessException e){
            log.debug("没有找到合适的退款单");
            return false;
        }
    }
}
