package cn.edu.xmu.payment.dao;

import cn.edu.xmu.inner.service.OrderInnerService;
import cn.edu.xmu.ooad.util.*;
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
import cn.edu.xmu.payment.model.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 3-7
 */
@Repository
public class PaymentDao {

    @Autowired
    private PaymentPoMapper paymentPoMapper;
    @Autowired
    private RefundPoMapper refundPoMapper;
    @DubboReference(check = false)
    private OrderInnerService orderInnerService;
    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */

    public ReturnObject getAfterSalesPayments(Long userId, Long id) {

        ReturnObject<Object> retObj = null;
        try {
            //效验after是否存在
            //效验after的顾客id是否与传入值相等
            //获得afterSaleId=id的支付单
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            //判断是否有afterSale;
            List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
            List<PaymentVo> paymentVos = new ArrayList<>(paymentPos.size());
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
    }
    /**
     * 管理员查询自己店铺售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */

    public ReturnObject getShopAfterSalesPayments(Long shopId, Long id,Long departId) {

        try {
            //效验after是否存在
            //效验after的shopId是否与传入值相等
            if(!shopId.equals(departId) &&departId!=0L){
                return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
            }
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            //获得AfterSaleId符合的支付
            criteria.andAftersaleIdEqualTo(id);
            List<PaymentPo> paymentPos=paymentPoMapper.selectByExample(paymentPoExample);
            List<PaymentVo> paymentVos = new ArrayList<>(paymentPos.size());
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

    /**
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
            return new ReturnObject<>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }

    /**
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
            return new ReturnObject<>(paymentRetVo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }
    /**
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
    /**
     *管理员查询订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:13
     */
    public ReturnObject getShopsOrdersRefunds(Long shopId, Long id,Long departId) {
        try {
            //该用户不是管理员
            if (!AuthVerify.adminAuth(departId)){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员"));

            }

            RefundPoExample refundPoExample= new RefundPoExample();
            RefundPoExample.Criteria criteria = refundPoExample.createCriteria();
            criteria.andOrderIdEqualTo(id);
            List<RefundPo> refundPos = refundPoMapper.selectByExample(refundPoExample);
            if (criteria.isValid()) {
                if(refundPos.size()>1){
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("同一笔订单不能有两个退款单号"));
                }
                for (RefundPo po : refundPos) {
                    if(shopId==null||!shopId.equals(orderInnerService.getShopIdByOrderId(po.getOrderId()))){
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("没有传入shopId或者该退款单不属于该商店"));
                    }
                    ShopsPaymentsInfoRetVo shopsOrdersRefundsInfoRetVo = new ShopsPaymentsInfoRetVo(po);
                    return new ReturnObject<>(shopsOrdersRefundsInfoRetVo);
                }
            } else {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        } catch(DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch(Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
    }
    /**
     *管理员查询售后订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:10
     */
    public ReturnObject getShopsAftersalesRefunds(Long shopId, Long id,Long departId) {
        try {

            //获得afterSaleId=id的退款单
            RefundPoExample refundPoExample=new RefundPoExample();
            RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<RefundPo> refundPos=refundPoMapper.selectByExample(refundPoExample);
            //该用户不是管理员
            if (!AuthVerify.adminAuth(departId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员"));

            }

            if (criteria.isValid()){
                for (RefundPo po : refundPos) {
                    if(refundPos.size()>1){
                        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("同一笔订单不能有两个退款单号"));
                    }
                    if(shopId==null||!shopId.equals(orderInnerService.getShopIdByOrderId(po.getOrderId()))){
                        return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("没有传入shopId或者该退款单不属于该商店"));
                    }
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
    public ReturnObject postRefunds(Long shopId, Long id,String amount_str,Long departId) {
        PaymentPo paymentPo=paymentPoMapper.selectByPrimaryKey(id);
        //支付不存在
        if(paymentPo==null)
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //管理员不是该店铺管理员
        if(!shopId.equals(departId) &&departId!=0L){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        //该支付不是店铺支付
        Long paymentShopId=orderInnerService.getShopIdByOrderId(paymentPo.getOrderId());
        if(!paymentShopId.equals(shopId))
        {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        //字段不合法
        Long amount;
        try{
            System.out.println("try");
            amount=Long.valueOf(amount_str);
        }
        catch (Exception e){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        //查询是否已有退款
        RefundPoExample refundPoExample=new RefundPoExample();
        RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
        criteria.andPaymentIdEqualTo(id);
        List<RefundPo> refundPos = null;
        try{
            refundPos= refundPoMapper.selectByExample(refundPoExample);
        }
        catch (Exception e){
        }
        if (refundPos!=null) {
            for (RefundPo po : refundPos) {
                //如果找到payment的退款，且退款成功，则不在进行退款
                if (po.getPaymentId().equals(id)&&po.getState().equals((byte)1)) {
                    return new ReturnObject<>(ResponseCode.REFUND_MORE,String.format("该支付已退款"));
                }
            }
        }
        //支付未成功或退款大于付款
        if(paymentPo.getState()!=(byte)1||paymentPo.getActualAmount()<amount)
        {
            return new ReturnObject<>(ResponseCode.REFUND_MORE);
        }
        RefundBo refundBo=new RefundBo(id,amount,paymentPo.getOrderId(),paymentPo.getAftersaleId());
        RefundPo refundPo=refundBo.createPo();
        refundPoMapper.insert(refundPo);
        refundBo.setId(refundPo.getId());
        //改变order相应状态-->13
        //改变其他模块售后的状态
        return new ReturnObject<>(refundBo);
    }

    /**
     * if(管理员) select *
     * if(店家)  select * where shopId
     * if(买家)  select * where userId
     * 通过userId获取订单Ids(Dubbo),通过订单id获取支付状态
     * @author issyu 30320182200070
     * @date 2020/12/12 18:45
     * 不在dao层实装DubboReference
     */
    public ReturnObject getPaymentStateByOrderIds(Long userId,Long departId){
        PaymentPoExample paymentPoExample = new PaymentPoExample();
        PaymentPoExample.Criteria criteria = paymentPoExample.createCriteria();
        List<PaymentStateBo> paymentStateBos = new ArrayList<>();
        //没有店铺的店家
        if(AuthVerify.noShopAdminAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW,String.format("没有店铺的店家，id=")+departId);
        }
        //管理员查询所有支付单
        if(AuthVerify.adminAuth(departId)){}
        //买家查询本人名下的支付单
        if(AuthVerify.customerAuth(departId)){
            List<Long> orderIds = orderInnerService.getOrderIdByUserId(userId);
            criteria.andOrderIdIn(orderIds);
        }
        //店家查询本店铺的所有支付单
        if(AuthVerify.shopAdminAuth(departId)){
            List<Long> orderIds = orderInnerService.getOrderIdByShopId(departId);
            criteria.andOrderIdIn(orderIds);
        }
        try{
            List<PaymentPo> paymentPos = paymentPoMapper.selectByExample(paymentPoExample);
            for(PaymentPo paymentPo:paymentPos){
                PaymentStateBo paymentStateBo = new PaymentStateBo();
                paymentStateBo.setCode(paymentPo.getState());
                paymentStateBo.setName(PaymentStateCode.getMessageByCode(paymentPo.getState()));
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
    public ReturnObject getPayPatternsByOrderId(Long userId,Long departId){

        PaymentPoExample paymentPoExample = new PaymentPoExample();
        PaymentPoExample.Criteria criteria =paymentPoExample.createCriteria();
        //买家获取名下支付状态
        if(AuthVerify.customerAuth(departId)){
            List<Long> orderIds = orderInnerService.getOrderIdByUserId(userId);
            criteria.andOrderIdIn(orderIds);
        }
        if(AuthVerify.noShopAdminAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW,"无店铺的店家管理员"+departId);
        }
        if(AuthVerify.adminAuth(departId)){

        }
        if (AuthVerify.shopAdminAuth(departId)){
            List<Long> orderIds = orderInnerService.getOrderIdByShopId(departId);
            criteria.andOrderIdIn(orderIds);
         }
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

    /**
     *买家查询自己的退款信息(订单)
     * 需要修复:需要Order模块获取用户ID进行校验
     * @author 王子扬 30320182200071
     * @date 2020/12/11
     */
    public ReturnObject getUsersOrdersRefunds(Long userId, Long id,Long departId,Long actualUserId) {
        try{
            if(actualUserId==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("所提供的订单号无效或者无法获取到对应用户"));
            }
            if(departId!=-2){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("登陆的不是普通用户"));
            }
            if(!actualUserId.equals(userId)){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("订单对应的UserId和登陆的不一致"));
            }
            RefundPoExample refundPoExample=new RefundPoExample();
            RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
            if(id == null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            criteria.andOrderIdEqualTo(id);

            List<RefundPo> refundPos=refundPoMapper.selectByExample(refundPoExample);
            if(refundPos.size() > 1){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("同一订单Id不能对拥有多个退款"));
            }
            for (RefundPo po : refundPos) {
                UsersPaymentsInfoRetVo usersPaymentsInfoRetVo = new UsersPaymentsInfoRetVo(po);
                return new ReturnObject<>(usersPaymentsInfoRetVo);
            }

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     *买家查询自己的退款信息(售后订单)
     * 需要修复:需要Order模块获取用户ID进行校验
     * @author 王子扬 30320182200071
     * @date 2020/12/11
     */
    public ReturnObject getUsersAftersalesRefunds(Long userId, Long id,Long departId) {
        try{
            if(userId==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("传入userId为空"));
            }
            if(departId!=-2){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("登陆用户不是普通买家"));
            }
//            RefundPo refundPo = refundPoMapper.selectByPrimaryKey(id);
            RefundPoExample refundPoExample=new RefundPoExample();
            RefundPoExample.Criteria criteria=refundPoExample.createCriteria();
            criteria.andAftersaleIdEqualTo(id);
            List<RefundPo> refundPo = refundPoMapper.selectByExample(refundPoExample);
            if(refundPo.isEmpty()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            RefundPo refundPo1 = refundPo.get(0);
            if(refundPo1 == null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("退款订单不存在"));
            }
            if(!userId.equals(orderInnerService.getUserIdByOrderId(refundPo1.getOrderId()))){
                return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW,String.format("登陆用户和所查询的退款订单对应用户不同"));
            }

            UsersPaymentsInfoRetVo usersPaymentsInfoRetVo = new UsersPaymentsInfoRetVo(refundPo1);
            return new ReturnObject<>(usersPaymentsInfoRetVo);
        }catch (DataAccessException e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }
}
