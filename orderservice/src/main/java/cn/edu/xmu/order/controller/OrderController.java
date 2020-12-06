package cn.edu.xmu.order.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.*;
import cn.edu.xmu.order.model.bo.OrderInfo;
import cn.edu.xmu.order.model.bo.OrderStateBo;
import cn.edu.xmu.order.model.vo.AdressVo;
import cn.edu.xmu.order.model.vo.OrderInfoVo;
import cn.edu.xmu.order.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/2 23:00
 */
@Api(value = "订单服务",tags ="order")
@RestController
@RequestMapping(value = "/order",produces = "application/json;charset=UTF-8")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获得订单的所有状态
     * @author issyu 30320182200070
     * @date 2020/12/2 23:16
     */
    @ApiOperation(value = "获得订单的所有状态",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/orders/states")
    public Object getOrderStates(@LoginUser @ApiIgnore Long userId){
        ReturnObject<List> returnObject = orderService.getOrderStates(userId);
        return Common.getListRetObject(returnObject);
    }

    /**
     * 买家查询名下所有订单概要
     * @author issyu 30320182200070
     * @date 2020/12/3 16:55
     *
     * *****userId从哪里来。*****
     */
    @ApiOperation(value = "买家查询名下所有订单概要",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "query",dataType = "String",name = "orderSn", value = "订单编号", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "state", value = "订单状态", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/orders")
    public Object getOrderSimpleInfo(
            @LoginUser  @ApiIgnore Long userId,
            @RequestParam(required = false) String orderSn,
            @RequestParam(required = false) Byte state,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        ReturnObject<PageInfo<VoObject>> returnObject = orderService.getOrderSimpleInfo(userId,orderSn,state,page,pageSize);
        return  Common.getPageRetObject(returnObject);

    }

    /**
     * 创建订单
     * @author issyu 30320182200070
     * @date 2020/12/4 23:22
     */
    @ApiOperation(value = "买家申请建立订单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "OrderInfoVo", name = "orderInfo", value = "制定新订单的资料", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 900, message = "商品库存不足"),
    })
    @Audit
    @PostMapping("/orders")
    public Object createOrder(
            @Validated @RequestBody OrderInfoVo vo, BindingResult bindingResult,
            @LoginUser @ApiIgnore @RequestParam(required = false) Long userId
    ){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        OrderInfo orderInfo = vo.createOrderInfo();
        orderInfo.setCustomerId(userId);
        orderInfo.setGmtCreate(LocalDateTime.now());
        ReturnObject retObject = orderService.createOrder(orderInfo);

        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
            /*
            此处返回值错误。
             */
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
    /*
     * 买家查询订单完整信息
     * @author 史韬韬
     * created in 2020/12/2
     */
    @ApiOperation(value = "买家查询订单完整信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(name="id",required = true, dataType="Integer", paramType="path")

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作id不存在")
    })
    @Audit
    @GetMapping("/orders/{id}")
    public Object getOrderById(@PathVariable Long id){
        return Common.decorateReturnObject(orderService.getOrderById(id));

    }

    /*
     * 买家修改本人名下订单
     * @author 史韬韬
     * created in 2020/12/3
     */
    @ApiOperation(value = "买家修改本人名下订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @RequestMapping(value="/orders/{id}",method = RequestMethod.PUT)
    public Object changeOrder(@PathVariable Long id, @RequestBody AdressVo adressVo){
        return Common.decorateReturnObject(orderService.changeOrder(id,adressVo));
    }
    /*
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * created in 2020/12/3
     */
    @ApiOperation(value = "买家取消、修改本人名下订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @RequestMapping(value="/orders/{id}",method = RequestMethod.DELETE)
    public Object deleteOrder(@PathVariable Long id){
        return Common.getRetObject(orderService.deleteOrder(id));
    }

    /**
     * 买家标记确认收货
     *
     * @param id 订单ID
     * @return Object 确认后结果
     * createdBy 王薪蕾 2020/11/30
     */
    @ApiOperation(value = "买家标记确认收货", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path", dataType="Long",name="id", value = "订单Id",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 800, message = "订单状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/orders/{id}/confirm")
    public Object confirmOrders(
            @PathVariable("id") Long id,
            @LoginUser Long userId) {
        return Common.getRetObject(orderService.confirmOrders(id,userId));
        //return Common.getNullRetObj()
    } /**
     * 团购订单转普通
     *
     * @param id 订单ID
     * @return Object 确认后结果
     * createdBy 王薪蕾 2020/12/6
     */
    @ApiOperation(value = "团购订单转普通", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path", dataType="Long",name="id", value = "订单Id",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 800, message = "订单状态禁止"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("/orders/{id}/groupon-normal")
    public Object grouponToNormalOrders(
            @PathVariable("id") Long id,
            @LoginUser Long userId) {
        return Common.getRetObject(orderService.grouponToNormalOrders(id,userId));

    }

    /**
     * 店家查询商户所有订单 (概要)
     * @author 王子扬 30320182200071
     * @date 2020/12/5 23:04
     */
    @ApiOperation(value = "店家查询商户所有订单 (概要)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", value = "Token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "shopId", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "customerId", value = "顾客Id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "orderSn", value = "订单Sn", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "beginTime", value = "开始时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "endTime", value = "结束时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/shops/{shopId}/orders")
    public Object getAllOrders(@PathVariable("shopId") Long shopId,
                               @RequestParam(required = false) Long customerId,
                               @RequestParam(required = false) String orderSn,
                               @RequestParam(required = false) String beginTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer pageSize){
        logger.debug("getAllOrders: page = "+ page +"  pageSize ="+pageSize);
        page = (page == null)?1:page;
        pageSize = (pageSize == null)?10:pageSize;
        LocalDateTime localBeginTime = null;
        LocalDateTime localEndTime = null;
        if(beginTime!=null){
            localBeginTime= TimeFormat.stringToDateTime(beginTime);
        }
        if(endTime!=null){
            localEndTime=TimeFormat.stringToDateTime(endTime);
        }

        ReturnObject<PageInfo<VoObject>> returnObject = orderService.selectOrders(shopId,customerId,orderSn,localBeginTime,localEndTime,page,pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 店家修改订单 (留言)
     * @author 王子扬 30320182200071
     * @date  2020/12/5 23:38
     */
    @ApiOperation(value = "店家修改订单 (留言)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", value = "Token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "shopId", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "path", name = "id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "message", value = "操作字段（状态）", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{shopId}/orders/{id}")
    public Object getAllOrders(@PathVariable("shopId") Long shopId,
                               @PathVariable("id") Long id,
                               @RequestParam(required = true) String message){
        ReturnObject retObject = orderService.updateOrderMessage(shopId,id,message);

        if (retObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObject);
            /*
            此处返回值错误。
             */
        } else {
            return Common.getNullRetObj(new ReturnObject<>(retObject.getCode(), retObject.getErrmsg()), httpServletResponse);
        }
    }
    /**
     * /shops/{shopId}/orders
     * @author 王子扬 30320182200071
     * @date  2020/12/5 23:38
     */
}
