package cn.edu.xmu.payment.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.payment.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentDao paymentDao;
    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject getAfterSalesPayments(Long userId, Long id) {
        ReturnObject returnObject = paymentDao.getAfterSalesPayments(userId,id);
        return returnObject;
    }
    /**
     * 管理员查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    public ReturnObject getAfterSalesPayments(Long userId,Long shopId, Long id) {
        ReturnObject returnObject = paymentDao.getAfterSalesPayments(userId,shopId,id);
        return returnObject;
    }
}
