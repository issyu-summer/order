package cn.edu.xmu.order.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.AuthVerify;
import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.bo.*;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.po.OrderPoExample;
import cn.edu.xmu.order.model.vo.AdressVo;
import cn.edu.xmu.order.model.vo.OrderInfoVo;
import cn.edu.xmu.order.model.vo.OrderRetVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author issyu 30320182200070
 * @date 2020/12/2 23:04
 */
@Repository
public class OrderDao {


    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);
    @Autowired
    private OrderPoMapper orderPoMapper;
    @Autowired
    private OrderItemPoMapper orderItemPoMapper;

    /**
     * @author issyu 30320182200070
     * 获取订单的所有状态
     * @param userId
     * @return
     */
    public ReturnObject getOrderStates(Long userId,Long departId){

        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        if(AuthVerify.customerAuth(departId)){
            criteria.andCustomerIdEqualTo(userId);
            criteria.andBeDeletedEqualTo((byte)0);
        }
        if(AuthVerify.noShopAdminAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW,"无店铺的店家管理员"+departId);
        }
        if(AuthVerify.adminAuth(departId)){

        }
        if (AuthVerify.shopAdminAuth(departId)){
            criteria.andShopIdEqualTo(departId);
        }
        try{
            logger.debug("getOrderStatesByUserId:"+userId);
            List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);

            if(orderPos.isEmpty()){
                logger.debug("order not found");
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"order not found");
            }
            List<OrderStateBo> bos = new ArrayList<>();

            for(OrderPo po:orderPos){
                bos.add(
                        //添加bo对象
                        new OrderStateBo(
                                //从枚举中找出对应的状态
                                OrderStateCode.getOrderStateByCode(po.getState())
                        )
                );
            }
            return new ReturnObject<>(bos);

        }catch (DataAccessException e){
            logger.error("getOrderStates: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 买家查询名下订单
     * @author issyu 30320182200070
     * @date 2020/12/3 16:10
     * @param orderSn
     * @param state
     * @param page
     * @param pageSize
     */
    public ReturnObject<PageInfo<VoObject>> getOrderSimpleInfo(Long userId,Long departId,
                                                               String orderSn, Byte state, Integer page, Integer pageSize){

        if(!AuthVerify.customerAuth(departId)){
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW,"仅买家可以调用此api,departId="+departId);
        }
        else {
            OrderPoExample orderPoExample = new OrderPoExample();
            OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

            criteria.andCustomerIdEqualTo(userId);

            if (orderSn != null) {
                criteria.andOrderSnEqualTo(orderSn);
            }

            if (state != null) {
                criteria.andStateEqualTo(state);
            }

            //不会返回逻辑删除的订单
            criteria.andBeDeletedEqualTo((byte) 0);

            try {
                PageHelper.startPage(page, pageSize);
                List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);
                if(orderPos.isEmpty()){
                    logger.debug("order not found");
                    return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"order not found");
                }
                PageInfo<OrderPo> orderPagePos = new PageInfo<>(orderPos);
                List<VoObject> operationOrders = orderPagePos.getList()
                        .stream()
                        .map(OrderSimpleInfoBo::new)
                        //this Bo implements VoObject
                        .collect(Collectors.toList());

                PageInfo<VoObject> returnObject = new PageInfo<>(operationOrders);
                returnObject.setPages(orderPagePos.getPages());
                returnObject.setPageNum(orderPagePos.getPageNum());
                returnObject.setPageSize(orderPagePos.getPageSize());
                returnObject.setTotal(orderPagePos.getTotal());

                return new ReturnObject<>(returnObject);
            } catch (DataAccessException e) {
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：" + e.getMessage()));
            }
        }
    }

    /**
     * 买家创建订单
     * @author issyu 30320182200070
     * @date 2020/12/4 23:37
     * @param orderInfoVo
     * @return OrederInfoRetVo 但是信息不全
     * 试试可不可以在dao层调用
     */
    public ReturnObject createOrder(OrderInfoVo orderInfoVo,Long userId,Long departId) {
        //插入orderInfo
        //OrderPoExample orderPoExample = new OrderPoExample();
        //插入orderItemInfo
        //OrderItemPoExample orderItemPoExample = new OrderItemPoExample();
        if(!AuthVerify.customerAuth(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW,"不是买家用户，departId="+departId);
        }

        OrderPo orderPo = orderInfoVo.getOrderPo();
        /**
         * 计算运费,计算原来的总价,运费,折扣加个,
         */
//        Long freightPrice;
//        Long price;
//        //orderPo
        List<OrderItemPo> orderItemPos = orderInfoVo.getOrderItemPoList();
        try {
            int retOrder = orderPoMapper.insertSelective(orderPo);
            if (retOrder == 0) {
                logger.debug("插入订单信息失败：收货人=" + orderPo.getConsignee());
            }
            for (OrderItemPo orderItemPo : orderItemPos) {
                int retOrderItem = orderItemPoMapper.insertSelective(orderItemPo);
                if (retOrderItem == 0) {
                    logger.debug("插入订单明细失败:" + orderItemPo.getName());
                }
            }

            //返回值
            OrderInfoBo orderInfoBo = new OrderInfoBo(orderPo);
            return new ReturnObject<>(orderInfoBo);
            //return orderInfoBo;
        }catch (DataAccessException e){
            //return new ReturnObject<>(ResponseCode.)
            return null;
        }
    }

    /**
     *查询订单完整信息
     * @parameter id 订单id
     * @author 史韬韬
     */
    public ReturnObject<OrderRetVo> getOrderById(Long id){

        logger.debug("getOrderByID: ID =" + id);
        try {
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
            Order order=new Order(orderPo);
            OrderRetVo orderRetVo=order.createOrderRetVo();
            return new ReturnObject<>(orderRetVo);

        }catch (DataAccessException e){
            logger.error("getOrderById: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }
    /**
     *买家修改本人名下订单
     * @parameter id 订单id
     * @author 史韬韬
     */
    public ReturnObject<VoObject> changeOrder(Long id, AdressVo adressVo){
        logger.debug("changeOrder: ID =" + id);
        try {
            OrderPo newPo = orderPoMapper.selectByPrimaryKey(id);
            Byte state=newPo.getState();
            //订单状态为创建订单、待支付或已支付时可以修改地址
            if(state.equals((byte)2)||state.equals((byte)6)||state.equals((byte)11)){
                newPo.setAddress(adressVo.getAddress());
                newPo.setConsignee(adressVo.getConsignee());
                newPo.setRegionId(adressVo.getRegionId());
                newPo.setMobile(adressVo.getMobile());
                orderPoMapper.updateByPrimaryKeySelective(newPo);
                return new ReturnObject<VoObject>();

            }else {
                ReturnObject<VoObject> returnObject = new ReturnObject<VoObject>(ResponseCode.ORDER_STATENOTALLOW, "不能修改此状态的订单");
                return returnObject;
            }

        }catch (DataAccessException e){
            logger.error("changeOrder: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /**
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * @parameter id 订单id
     * created in 2020/12/3
     */
    public ReturnObject<VoObject> deleteOrder(Long id) {
        logger.debug("deleteOrder: ID =" + id);
        try {
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
            Byte state = orderPo.getState();
            //发货前，订单处于待支付和创建订单状态时可以取消订单
            if (state.equals((byte) 6) || state.equals((byte) 2)) {
                orderPo.setState((byte) 0);
            }
            //已完成，订单处于取消0、已退款13、已签收18、订单中止14状态时可以逻辑删除订单
            else if (state.equals((byte) 0) || state.equals((byte) 13) || state.equals((byte)18)||state.equals((byte)14)) {
                orderPo.setBeDeleted((byte) 1);
            }
            else{
                return new ReturnObject<VoObject>(ResponseCode.ORDER_STATENOTALLOW,"不能取消或删除此状态的订单");
            }
            return new ReturnObject<VoObject>();
        } catch (DataAccessException e) {
            logger.error("deleteOrder: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 确认收货
     * createby 王薪蕾 2020/12/6
     */
    public ReturnObject<Object> confirmOrders(Long id,Long userId) {

        logger.debug("confirmOrder: ID =" + id);
        OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
        ReturnObject<Object> retObj = null;
        //订单不存在
        if (orderPo == null){
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }
        //该订单不是此用户订单
        if (orderPo.getCustomerId()!=userId) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            return retObj;
        }
        //状态不是到货的无法收货
        if (orderPo.getState()!=OrderStateCode.ORDER_STATE_UNCONFIRMED.getCode()
        ||orderPo.getSubstate()!=OrderStateCode.ORDER_STATE_SHIP.getCode()){
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW);
            return retObj;
        }
        //收货
        orderPo.setState((byte)OrderStateCode.ORDER_STATE_COMPLETED.getCode());
        orderPo.setGmtModified(LocalDateTime.now());
        try {
            int ret = orderPoMapper.updateByPrimaryKey(orderPo);
            if (ret == 0) {
                //确认收货失败
                logger.debug("confirmOrders: confirm order fail: " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                //确认收货成功
                logger.debug("confirmOrders: confirm order = " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            logger.error("database exception: " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return retObj;
    }

    /**
     * 团购转普通
     * createby 王薪蕾 2020/12/6
     */
    public ReturnObject<Object> grouponToNormalOrders(Long id,Long userId) {

        logger.debug("confirmOrder: ID =" + id);
        OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
        ReturnObject<Object> retObj = null;
        //订单不存在
        if (orderPo == null){
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            return retObj;
        }
        //该订单不是此用户订单
        if (orderPo.getCustomerId()!=userId) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            return retObj;
        }
        //订单不是团购订单
        if (orderPo.getOrderType()!=(byte)1){
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW);
            return retObj;
        }
        //状态不是待收货
        if (orderPo.getState() != OrderStateCode.ORDER_STATE_UNCONFIRMED.getCode()){
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW);
            return retObj;
        }
        //子状态不是未成团就无法转成普通订单
        if (orderPo.getSubstate()!= OrderStateCode.ORDER_STATE_GROUP_NOT_COMPLETED.getCode()) {
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW);
            return retObj;
        }
        //改订单类型
        orderPo.setOrderType((byte)0);
        //删除团购相关
        orderPo.setGrouponDiscount(null);
        orderPo.setGrouponId(null);
        //改变修改时间
        orderPo.setGmtModified(LocalDateTime.now());
        //状态：付款完成
        orderPo.setSubstate((byte)OrderStateCode.ORDER_STATE_PAID.getCode());
        try {
            int ret = orderPoMapper.updateByPrimaryKey(orderPo);
            if (ret == 0) {
                //数据库失败
                logger.debug("changeOrders: change order fail: " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                // 团购转普通成功
                logger.debug("changeOrders: confirm order = " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.OK);
            }
        } catch (DataAccessException e) {
            logger.error("database exception: " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
    /**
     * 店家查询商户所有订单 (概要)
     * @author 王子扬 30320182200071
     * @date  2020/12/5 16:09
     */
    public ReturnObject<PageInfo<VoObject>> findAllOrders(Long shopId, Long customerId, String orderSn, LocalDateTime beginTime,
                                                          LocalDateTime endTime, Integer page, Integer pageSize){
        OrderPoExample example = new OrderPoExample();
        OrderPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andBeDeletedEqualTo((byte)0);//已经删除订单即被删除状态不为0的订单就不再显示
        if(customerId != null){
            criteria.andCustomerIdEqualTo(customerId);
        }
        if(orderSn != null){
            criteria.andOrderSnEqualTo(orderSn);
        }
        if(beginTime != null && endTime != null){
            criteria.andConfirmTimeBetween(beginTime,endTime);
        }else{
            if(endTime != null){
                criteria.andConfirmTimeLessThanOrEqualTo(endTime);
            }
            if(beginTime != null){
                criteria.andConfirmTimeGreaterThanOrEqualTo(beginTime);
            }
        }

        PageHelper.startPage(page,pageSize);
        List<OrderPo> orderPos = null;
        logger.debug("page = " + page + "pageSize = " + pageSize);
        try{
            orderPos = orderPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(orderPos.size());
            for(OrderPo po : orderPos){
                OrderBrief order = new OrderBrief(po);
                ret.add(order);
            }
            PageInfo<VoObject> orderPage = PageInfo.of(ret);
            return new ReturnObject<>(orderPage);
        }catch (DataAccessException e){
            logger.error("OrderDao: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }catch (Exception e){
            logger.error("OrderDao other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }

    /**
     * 店家修改订单 (留言)
     * @author 王子扬 30320182200071
     * @date  2020/12/5 23:38
     */
    public ReturnObject<OrderBrief> updateOrderMessage(Long shopId, Long id, String message) {
        ReturnObject<OrderBrief> retObj = null;
        try{
            OrderPo orderPo=orderPoMapper.selectByPrimaryKey(id);
            if(orderPo==null || orderPo.getBeDeleted()!=(byte)0){//订单已经逻辑删除视为订单不存在
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("订单id不存在：" + id));
                return retObj;
            }
            if(orderPo.getShopId()!=shopId){
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("店铺不存在该订单：" + shopId));
                return retObj;
            }
            orderPo.setMessage(message);
            orderPoMapper.updateByPrimaryKey(orderPo);
            return new ReturnObject<>();
        }catch (DataAccessException e){
            logger.error("OrderDao: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }catch (Exception e){
            logger.error("OrderDao other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
    }
    /**
     * 店家查询店内订单完整信息(普通，团购，预售)
     * @author 陈星如
     * @date 2020/12/5 16:10
     * @param shopId
     * @param id
     **/
    public ReturnObject getOrderByShopId(Long shopId, Long id) {
        logger.debug("getOrderByShopID: ID =" + id);
        logger.debug("getOrderByShopID: ShopID =" + shopId);
        try {
            OrderPo ordersPo = orderPoMapper.selectByPrimaryKey(id);
            Order order = new Order(ordersPo);
            if (ordersPo == null) {
                logger.debug("操作的订单id不存在:");
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else if (!ordersPo.getShopId().equals(shopId)) {
                logger.debug("操作的商店id不是自己的对象: " + ordersPo.toString());
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
            OrderRetVo orderRetVo = order.createOrderRetVo();
            return new ReturnObject<>(orderRetVo);

        } catch (DataAccessException e) {
            logger.error("getOrderById: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /*
     * 管理员取消本店铺订单
     * @author 陈星如
     * @date 2020/12/5 15:15
     */
    public ReturnObject<VoObject> deleteShopOrder(Long shopId, Long id) {
        logger.debug("confirmOrder: ID =" + id);
        OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
        ReturnObject<VoObject> retObj = null;
        //订单不存在
        if (orderPo == null){
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("订单不存在：id=" + id));
            return retObj;
        }
        //该订单不是此店铺订单
        if (!shopId.equals(orderPo.getShopId())) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("操作的店铺id不是自己的对象：id=" + id));
            return retObj;
        }
        //订单处于无法取消状态
        if (!orderPo.getState().equals((byte) 8)&&!orderPo.getState().equals((byte) 9)&&!orderPo.getState().equals((byte) 10)&&!orderPo.getState().equals((byte) 11)&&!orderPo.getState().equals((byte) 12)&&!orderPo.getState().equals((byte) 15)) {
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW, String.format("订单状态禁止：id=" + id));
            return retObj;
        }
        //取消订单
        orderPo.setState((byte) 0);
        try {
            int ret = orderPoMapper.updateByPrimaryKey(orderPo);
            if (ret == 0) {
                //取消订单失败
                logger.debug("confirmOrders: confirm order fail: " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库更新不成功"));
            } else {
                //取消订单成功
                logger.debug("confirmOrders: confirm order = " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.OK,String.format("成功"));
            }
        } catch (DataAccessException e) {
            logger.error("database exception: " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }
    /*
     * 店家对订单标记发货
     * @author 陈星如
     * @date 2020/12/5 15:16
     */
    public ReturnObject<VoObject> shipOrder(Long shopId, Long id, String shipmentSn) {
        logger.debug("confirmOrder: ID =" + id);
        OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
        ReturnObject<VoObject> retObj = null;
        //订单不存在
        if (orderPo == null){
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("订单不存在：id=" + id));
            return retObj;
        }
        //该订单不是此店铺订单
        if (!shopId.equals(orderPo.getShopId())) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("操作的店铺id不是自己的对象：id=" + id));
            return retObj;
        }
        //订单处于无法发货状态
        if (!orderPo.getState().equals((byte) 8)&&!orderPo.getState().equals((byte) 9)&&!orderPo.getState().equals((byte) 10)&&!orderPo.getState().equals((byte) 11)&&!orderPo.getState().equals((byte) 12)&&!orderPo.getState().equals((byte) 15)) {
            retObj = new ReturnObject<>(ResponseCode.ORDER_STATENOTALLOW, String.format("订单状态禁止：id=" + id));
            return retObj;
        }
        //发货
        orderPo.setState((byte) 16);
        orderPo.setShipmentSn(shipmentSn);
        try {
            int ret = orderPoMapper.updateByPrimaryKey(orderPo);
            if (ret == 0) {
                //标记发货失败
                logger.debug("confirmOrders: confirm order fail: " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库更新不成功"));
            } else {
                //标记发货成功
                logger.debug("confirmOrders: confirm order = " + orderPo.toString());
                retObj = new ReturnObject<>(ResponseCode.OK,String.format("成功"));
            }
        } catch (DataAccessException e) {
            logger.error("database exception: " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了未知错误：%s", e.getMessage()));
        }
        return retObj;
    }

}
