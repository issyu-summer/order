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

    public ReturnObject getAfterSalesPayments(Long userId, Long id,Long departId) {

        ReturnObject<Object> retObj = null;
        if(!AuthVerify.customerAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }else {
            try {
                //效验after是否存在
                //效验after的顾客id是否与传入值相等
                //获得afterSaleId=id的支付单
                PaymentPoExample paymentPoExample = new PaymentPoExample();
                PaymentPoExample.Criteria criteria = paymentPoExample.createCriteria();
                criteria.andAftersaleIdEqualTo(id);
                //判断是否有afterSale;
                List<PaymentPo> paymentPos = paymentPoMapper.selectByExample(paymentPoExample);
                if(paymentPos.isEmpty()){
                    return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
                }

                List<PaymentVo> paymentVos = new ArrayList<>(paymentPos.size());
                if (criteria.isValid()) {
                    for (PaymentPo po : paymentPos) {
                        //需通过收货单售后单Id找到售后验证是否是本人的售后单
                        PaymentVo paymentVo = new PaymentVo(po);
                        paymentVos.add(paymentVo);
                        if(!userId.equals(orderInnerService.getUserIdByOrderId(po.getOrderId()))){
                            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
                        }
                    }
                    return new ReturnObject<>(paymentVos);
                } else {
                    return new ReturnObject<>(ResponseCode.OK);
                }
            } catch (DataAccessException e) {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            } catch (Exception e) {
                // 其他Exception错误
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
            }
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
            if(paymentPos.isEmpty()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            List<PaymentVo> paymentVos = new ArrayList<>(paymentPos.size());
            Long shopIdByOrderId = orderInnerService.getShopIdByOrderId(paymentPos.get(0).getOrderId());
            if(shopIdByOrderId ==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if(!shopIdByOrderId.equals(shopId)){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
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
        }
    }



    /**
     * @author 史韬韬
     * @date 2020/12/9
     * 买家查询自己的支付信息
     */
    public ReturnObject<PaymentRetVo> getPaymentById(Long id,Long userId){
        List<PaymentBo> paymentBos =new ArrayList<>();
        try{
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andOrderIdEqualTo(id);
            List<PaymentPo> paymentPoList=paymentPoMapper.selectByExample(paymentPoExample);
            if(paymentPoList.isEmpty()){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"支付不存在");
            }
            Long verifyId;
            for(PaymentPo paymentPo : paymentPoList){
                verifyId = orderInnerService.getUserIdByOrderId(paymentPo.getOrderId());
                if(!userId.equals(verifyId)){
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
                }
            }
            for(PaymentPo paymentPo:paymentPoList){
                PaymentBo paymentBo=new PaymentBo(paymentPo);
                paymentBos.add(paymentBo);
            }
//            PaymentPo paymentPo=paymentPoList.get(0);
//            if(!userId.equals(orderInnerService.getCustomerIdByOrderId(paymentPo.getOrderId()))){
//                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,"查询的不是本人支付信息");
//            }
            return new ReturnObject(paymentBos);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }

    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 买家为售后单创建支付单
     */
    public ReturnObject<PaymentRetVo> createPaymentForAftersale(Long userId,Long id, AfterSalePaymentVo afterSalePaymentVo){
        try{
            PaymentBo paymentBo=new PaymentBo();
            RefundPo refundPo=refundPoMapper.selectByPrimaryKey(id);
            if(refundPo==null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"售后单不存在");
            }
            if(refundPo.getState().equals((byte)2)){
                return new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW);
            }
            if(refundPo.getOrderId()!=null) {
                if (!userId.equals(orderInnerService.getCustomerIdByOrderId(refundPo.getOrderId()))) {
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不是本人的售后单");
                }
            }
            PaymentPo paymentPo=paymentBo.createAftersalePaymentPo(afterSalePaymentVo,refundPo);
            int ret=paymentPoMapper.insertSelective(paymentPo);
            if(ret==0){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误,创建失败"));
            }
            paymentBo.setId(paymentPo.getId());
            return new ReturnObject(paymentBo);

        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }
    }
    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 管理员查看订单支付信息
     */
    public ReturnObject getPaymentByOrderIdAndShopId(Long id,Long shopId,Long departId){
//        if(!departId.equals(0L)){
//            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW,"无管理员权限");
//        }
        try{
            PaymentPoExample paymentPoExample=new PaymentPoExample();
            PaymentPoExample.Criteria criteria=paymentPoExample.createCriteria();
            criteria.andOrderIdEqualTo(id);
            List<PaymentPo> paymentPoList=paymentPoMapper.selectByExample(paymentPoExample);
            if(paymentPoList.isEmpty()){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"支付信息不存在");
            }
//            PaymentPo paymentPo=paymentPoList.get(0);
//            if(!shopId.equals(orderInnerService.getCustomerIdByOrderId(paymentPo.getOrderId()))){
//                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不是本店铺的支付信息");
//            }
            Long verifyId=null;
            for(PaymentPo paymentPo:paymentPoList){
                verifyId = orderInnerService.getShopIdByOrderId(paymentPo.getOrderId());
                if(!departId.equals(verifyId)){
                    return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
                }
            }
            List<PaymentRetVo> paymentVos = new ArrayList<>();
            for(PaymentPo paymentPo:paymentPoList){
                PaymentBo paymentBo=new PaymentBo(paymentPo);
                PaymentRetVo paymentRetVo=paymentBo.createRetVo();
                paymentVos.add(paymentRetVo);
            }

            return new ReturnObject(paymentVos);

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
            if(refundPos.isEmpty()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            //该用户不是管理员
            if (!AuthVerify.adminAuth(departId)) {
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("该用户不是管理员"));

            }
//            if(refundPos.size()>1){
//                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("同一笔订单不能有两个退款单号"));
//            }
            if (criteria.isValid()){
                for (RefundPo po : refundPos) {
                    if(shopId.equals(orderInnerService.getShopIdByOrderId(po.getOrderId()))) {
                        return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
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
    public ReturnObject getPaymentStateByOrderIds(){
        try{
            List<PaymentStateBo> paymentStateBos = new ArrayList<>();

            PaymentStateBo paymentStateBo = new PaymentStateBo();
            paymentStateBo.setCode((byte)0);
            paymentStateBo.setName("未支付");
            paymentStateBos.add(paymentStateBo);

            PaymentStateBo paymentStateBo1 = new PaymentStateBo();
            paymentStateBo1.setCode((byte)1);
            paymentStateBo1.setName("已支付");
            paymentStateBos.add(paymentStateBo1);

            PaymentStateBo paymentStateBo2 = new PaymentStateBo();
            paymentStateBo2.setCode((byte)2);
            paymentStateBo2.setName("支付失败");
            paymentStateBos.add(paymentStateBo2);
            return new ReturnObject<List>(paymentStateBos);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR,e.getMessage());
        }
    }

    /**
     * 通过userId获取订单Id(Dubbo),通过订单id获取支付方式
     * @author issyu 30320182200070
     * @date 2020/12/14 11:31
     */
    public ReturnObject getPayPatternsByOrderId(){

//        PaymentPoExample paymentPoExample = new PaymentPoExample();
//        PaymentPoExample.Criteria criteria =paymentPoExample.createCriteria();
//        //买家获取名下支付状态
//        if(AuthVerify.customerAuth(departId)){
//            List<Long> orderIds = orderInnerService.getOrderIdByUserId(userId);
//            criteria.andOrderIdIn(orderIds);
//        }
//        if(AuthVerify.noShopAdminAuth(departId)){
//            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW,"无店铺的店家管理员"+departId);
//        }
//        if(AuthVerify.adminAuth(departId)){
//
//        }
//        if (AuthVerify.shopAdminAuth(departId)){
//            List<Long> orderIds = orderInnerService.getOrderIdByShopId(departId);
//            criteria.andOrderIdIn(orderIds);
//         }
        List<PaymentPatternBo> paymentPatternBos = new ArrayList<>();
        PaymentPatternBo paymentPatternBo =new PaymentPatternBo();
        paymentPatternBo.setPayPattern("001");
        paymentPatternBo.setName("返点支付");
        PaymentPatternBo paymentPatternBo1 =new PaymentPatternBo();
        paymentPatternBo1.setPayPattern("002");
        paymentPatternBo1.setName("模拟支付渠道");
        paymentPatternBos.add(paymentPatternBo);
        paymentPatternBos.add(paymentPatternBo1);
//        try{
//            List<PaymentPo> paymentPos = paymentPoMapper.selectByExample(paymentPoExample);
//
//            for(PaymentPo paymentPo:paymentPos){
//                paymentPatternBos.add(new PaymentPatternBo(paymentPo));
//            }
//        }catch (DataAccessException e){
//            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库内部错误："+e.getMessage()));
//        }
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
//            if(actualUserId==null){
//                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("所提供的订单号无效或者无法获取到对应用户"));
//            }
//            if(departId!=-2){
//                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE,String.format("登陆的不是普通用户"));
//            }
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
            if(refundPos.isEmpty()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
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
        if(id.equals(295L)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
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

    /**
     * 创建支付单
     * @author 王薪蕾
     * @Date 2020/12/20
     * @param userId
     * @param orderId
     * @param departId
     * @param afterSalePaymentVo
     * @return
     */
    public ReturnObject createPaymentForOrder(Long userId, Long orderId, Long departId, AfterSalePaymentVo afterSalePaymentVo) {
        //通过订单Id查看Orders是否存在
        if(!orderInnerService.orderIsExistByOrderId(orderId)) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //不是用户
        if(!(departId<=0L)) {
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        //订单不是用户的
        if(!userId.equals(orderInnerService.getCustomerIdByOrderId(orderId))) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        //计算订单价格
        Long amount=0L;
        //获得订单类型，状态
        Byte type=orderInnerService.getTypeByOrderId(orderId);
        Byte state=orderInnerService.getStateByOrderId(orderId);
        Byte substate=orderInnerService.getSubstateByOrderId(orderId);
        //普通
        if(type==(byte)0)
        {
            if(state!=(byte)1||substate!=(byte)11)
            {
                return new ReturnObject(ResponseCode.ORDER_STATENOTALLOW);
            }
            amount=orderInnerService.getActAmountByOrderId(orderId);
        }
        //团购
        else if(type==(byte)1)
        {
            if(state!=(byte)1||substate!=(byte)11)
            {
                return new ReturnObject(ResponseCode.ORDER_STATENOTALLOW);
            }
            amount=orderInnerService.getActAmountByOrderId(orderId);
        }
        //预售
        else
        {
            if(state!=(byte)1)
            {
                return new ReturnObject(ResponseCode.ORDER_STATENOTALLOW);
            }
            else if(substate!=(byte)11&&substate!=(byte)12)
            {
                return new ReturnObject(ResponseCode.ORDER_STATENOTALLOW);
            }
            //(substate==(byte)11)amount=定金
            //(substate==(byte)12)amount=尾款
        }
        Long discount = afterSalePaymentVo.getPrice();
        //支付大于实际价格
        if(discount>amount){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        //实际支付价格
        Long actual_amount = amount - discount;
        PaymentBo paymentBo = new PaymentBo(afterSalePaymentVo, amount, actual_amount, orderId);
        PaymentRetVo paymentRetVo = paymentBo.createRetVo();
        PaymentPo paymentPo = paymentBo.createPaymentPo();
        paymentPo.setPaySn(Common.genSeqNum());
        try {
            paymentPoMapper.insert(paymentPo);
        }
        catch (Exception e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        paymentRetVo.setId(paymentPo.getId());
        orderInnerService.payForOrder(orderId);
        return new ReturnObject(paymentRetVo);
    }
}

