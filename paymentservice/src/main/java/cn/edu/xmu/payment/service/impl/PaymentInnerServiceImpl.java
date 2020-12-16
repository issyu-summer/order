package cn.edu.xmu.payment.service.impl;

import cn.edu.xmu.inner.service.PaymentInnerService;
import cn.edu.xmu.payment.mapper.RefundPoMapper;
import cn.edu.xmu.payment.model.po.RefundPo;
import cn.edu.xmu.payment.model.po.RefundPoExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

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
        int flag=0,ret;
        try{
            RefundPo refundPo = new RefundPo();
            List<RefundPo> refundPos = refundPoMapper.selectByExample(refundPoExample);
            if(refundPos == null) {
                log.debug("没有找到合适的退款单");
                return false;
            }
            if (!refundPos.isEmpty()) {
                for (RefundPo po : refundPos) {

                    if (po.getState() == 0)
                    {
                        refundPo  = refundPoMapper.selectByPrimaryKey(po.getId());
                        refundPo.setState((byte) 1);
                        flag=1;
                        break;
                    }


                }
            }
            ret = refundPoMapper.updateByExampleSelective(refundPo,refundPoExample);
            if(flag == 0){
                log.debug("退款单状态不可变更");
                return false;
            }
            else if(ret == 0){
                log.debug("数据库更新失败");
                return false;
            }else{
                return true;
            }
        }catch (DataAccessException e) {
            log.debug("数据库错误");
            return false;
        } catch (Exception e) {
            log.debug("发生了未知错误：%s", e.getMessage());
            return false;
        }

    }
}
