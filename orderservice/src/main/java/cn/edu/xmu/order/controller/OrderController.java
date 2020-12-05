package cn.edu.xmu.order.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.model.bo.OrderInfo;
import cn.edu.xmu.order.model.bo.OrderStateBo;
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
}
