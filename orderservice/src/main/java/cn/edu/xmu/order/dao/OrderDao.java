package cn.edu.xmu.order.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.mapper.OrderItemPoMapper;
import cn.edu.xmu.order.mapper.OrderPoMapper;
import cn.edu.xmu.order.model.bo.Order;
import cn.edu.xmu.order.model.bo.OrderInfo;
import cn.edu.xmu.order.model.bo.OrderSimpleInfoBo;
import cn.edu.xmu.order.model.bo.OrderStateBo;
import cn.edu.xmu.order.model.po.OrderItemPo;
import cn.edu.xmu.order.model.po.OrderItemPoExample;
import cn.edu.xmu.order.model.po.OrderPo;
import cn.edu.xmu.order.model.po.OrderPoExample;
import cn.edu.xmu.order.model.vo.AdressVo;
import cn.edu.xmu.order.model.vo.OrderInfoVo;
import cn.edu.xmu.order.model.vo.OrderRetVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

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
     * 获取订单的所有状态
     * @param userId
     * @return
     */
    public ReturnObject getOrderStates(Long userId){

        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        criteria.andCustomerIdEqualTo(userId);

        try{
            logger.debug("getOrderStatesByUserId:"+userId);
            List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);

            if(orderPos==null){
                logger.debug("order not found");
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,"order not found");
            }

            //List<Byte> stateList = new ArrayList<>();
            //List<OrderStateCode> orderStateList = new ArrayList<>();
            List<OrderStateBo> bos = new ArrayList<>();

            for(OrderPo po:orderPos){
                bos.add(
                        //添加bo对象
                        new OrderStateBo(
                                //从枚举中找出对应的状态
                                OrderStateCode.getOrderStateByCode(po.getState())
                        )
                );

                //orderStateList.add(OrderStateCode.getOrderStateByCode(po.getState()));
                //stateList.add(po.getState());
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
    public ReturnObject<PageInfo<VoObject>> getOrderSimpleInfo(Long userId,String orderSn, Byte state, Integer page, Integer pageSize){

        OrderPoExample orderPoExample = new OrderPoExample();
        OrderPoExample.Criteria criteria = orderPoExample.createCriteria();

        criteria.andCustomerIdEqualTo(userId);

        if(orderSn!=null){
            criteria.andOrderSnEqualTo(orderSn);
        }

        if(state!=null){
            criteria.andStateEqualTo(state);
        }

        //不会返回逻辑删除的订单
        criteria.andBeDeletedEqualTo((byte) 0);

        try{
            PageHelper.startPage(page,pageSize);
            List<OrderPo> orderPos = orderPoMapper.selectByExample(orderPoExample);

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
        }catch(DataAccessException e){

            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库错误："+e.getMessage()));
        }

    }

    /**
     * 买家创建订单
     * @author issyu 30320182200070
     * @date 2020/12/4 23:37
     * @param orderInfo
     */
    public ReturnObject<OrderInfo> createOrder(OrderInfo orderInfo) {

        ReturnObject<OrderInfo> retObj = null;

        OrderPoExample orderPoExample = new OrderPoExample();
        OrderItemPoExample orderItemPoExample = new OrderItemPoExample();

        OrderPo orderPo = orderInfo.getOrderPo();
        try {
            int retOrder = orderPoMapper.insertSelective(orderPo);
            if (retOrder == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + orderPo.getConsignee()));
            } else {
                OrderItemPo orderItemPo = orderInfo.getOrderItemPo(orderPo.getId());
                int retOrderItem = orderItemPoMapper.insertSelective(orderItemPo);
                if (retOrderItem == 0) {
                    retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + orderPo.getConsignee()));
                } else {
                    retObj = new ReturnObject<>(orderInfo);
                }
            }
        } catch (DataAccessException e) {
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：" + e.getMessage()));
        }
        return retObj;
    }

    /**
     *查询订单完整信息
     * @parameter id 订单id
     * @author 史韬韬
     */
    public ReturnObject<OrderRetVo> getOrderById(Long id){

        logger.debug("getOrderByID: ID =" + id);
        try {
            OrderPo ordersPo = orderPoMapper.selectByPrimaryKey(id);
            Order order=new Order(ordersPo);
            OrderRetVo orderRetVo=order.createVo();
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
            if(state.equals((byte)0)||state.equals((byte)13)||state.equals((byte)14)||state.equals((byte)15)||state.equals((byte)16)||state.equals((byte)17)||state.equals((byte)18)){
                ReturnObject<VoObject> returnObject=new ReturnObject<VoObject>(ResponseCode.ORDER_STATENOTALLOW,"不能修改此状态的订单");
            }
            newPo.setAddress(adressVo.getAddress());
            newPo.setConsignee(adressVo.getConsignee());
            newPo.setRegionId(adressVo.getRegionId());
            newPo.setMobile(adressVo.getMobile());
            orderPoMapper.updateByPrimaryKeySelective(newPo);
            return new ReturnObject<VoObject>();

        }catch (DataAccessException e){
            logger.error("changeOrder: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /**
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * created in 2020/12/3
     */
    public ReturnObject<VoObject> deleteOrder(Long id) {
        logger.debug("deleteOrder: ID =" + id);
        try {
            OrderPo orderPo = orderPoMapper.selectByPrimaryKey(id);
            Byte state = orderPo.getState();
            //未发货
            if (state.equals((byte) 6) || state.equals((byte) 2)) {
                orderPo.setState((byte) 0);
            }
            //已发货
            if (state.equals((byte) 0) || state.equals((byte) 13) || state.equals((byte)18)) {
                orderPo.setBeDeleted((byte) 1);
            }
            return new ReturnObject<VoObject>();
        } catch (DataAccessException e) {
            logger.error("deleteOrder: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
}
