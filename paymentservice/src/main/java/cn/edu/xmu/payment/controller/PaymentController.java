package cn.edu.xmu.payment.controller;
import cn.edu.xmu.external.service.IAfterSaleService;
import cn.edu.xmu.inner.service.OrderInnerService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "支付服务",tags ="payment")
@RestController
@RequestMapping(value = "/payment",produces = "application/json;charset=UTF-8")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    @DubboReference(check = false)
    private OrderInnerService orderInnerService;
    /**
     * 根据用户id获取订单id
     * @author issyu 30320182200070
     * @date 2020/12/10 17:02
     */
    @GetMapping("/orderid")
    public Object getOrderIdByUserId(){
        List<Long> orderIds = orderInnerService.getOrderIdByUserId(23L);
        for(Long id:orderIds){
            System.out.println(id);
        }
        return Common.getListRetObject(new ReturnObject<>(orderIds));
    }

    /**
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    @ApiOperation(value = "买家查询自己售后单的支付信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path", dataType="Long",name="id", value = "售后单Id",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}/payments")
    public Object getAfterSalesPayments(
            @Depart @ApiIgnore Long departId,
            @PathVariable("id") Long id,
            @LoginUser Long userId){
        return Common.decorateReturnObject(paymentService.getAfterSalesPayments(userId,id,departId));
    }
    /**
     * 管理员查询售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    @ApiOperation(value = "管理员查询售后单的支付信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType="path", dataType="Long",name="shopId", value = "店铺Id",required = true),
            @ApiImplicitParam(paramType="path", dataType="Long",name="id", value = "售后单Id",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/aftersales/{id}/payments")
    public Object getAfterSalesPaymentsForAdmin(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Depart Long departId){
        return Common.decorateReturnObject(paymentService.getShopAfterSalesPayments(shopId,id,departId));
    }
    /**
     * @author 史韬韬
     * @date 2020/12/9
     * 买家查询自己的支付信息
     */
    @ApiOperation(value = "买家查询自己的支付信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "订单Id", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/orders/{id}/payments")
    public Object getPaymentById(@PathVariable Long id,@LoginUser Long userId){
        return Common.decorateReturnObject(paymentService.getPaymentById(id,userId));
    }

    /**
     * @author 王薪蕾
     * @date 2020/12/20
     * 买家为订单创建支付单
     */
    @ApiOperation(value = "买家查询自己的支付信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "订单Id", required = true),
            @ApiImplicitParam(paramType = "body",dataType = "Object",name = "afterSalePaymentVo", value = "售后单支付信息", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/orders/{id}/payments")
    public Object postPaymentById(
            @PathVariable Long id,
            @LoginUser Long userId,
            @Depart Long departId,
            @RequestBody AfterSalePaymentVo afterSalePaymentVo){
        return Common.decorateReturnObject(paymentService.createPaymentForOrder(userId,id,departId,afterSalePaymentVo));
    }


    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 买家为售后单创建支付单
     */
    @ApiOperation(value = "买家为售后单创建支付单",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body",dataType = "Object",name = "afterSalePaymentVo", value = "售后单支付信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/aftersales/{id}/payments")
    public Object createPaymentForAftersale(@LoginUser Long userId,@PathVariable Long id,@RequestBody AfterSalePaymentVo afterSalePaymentVo){
        return Common.getListRetObject(paymentService.createPaymentForAftersale(userId,id,afterSalePaymentVo));
    }
    /**
     * @author 史韬韬
     * @date 2020/12/10
     * 管理员查看订单支付信息
     */
    @ApiOperation(value = "管理员查看订单支付信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "商铺id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/orders/{id}/payments")
    public Object getPaymentByOrderIdAndShopId(@PathVariable Long id,@PathVariable Long shopId,@Depart Long departId){
        return Common.decorateReturnObject(paymentService.getPaymentByOrderIdAndShopId(id,shopId,departId));
    }

    /**
     *管理员查询订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:13
     */
    @ApiOperation(value = "管理员查询订单的退款信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "订单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/orders/{id}/refunds")
    public Object getShopsOrdersRefunds(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id")  Long id,@Depart @ApiIgnore Long departId){
        return Common.decorateReturnObject(paymentService.getShopsOrdersRefunds(shopId,id,departId));
    }
    /**
     *管理员查询售后订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:10
     */
    @ApiOperation(value = "管理员查询订单的退款信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/aftersales/{id}/refunds")
    public Object getShopsAftersalesRefunds(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id")  Long id, @Depart @ApiIgnore Long departId){
        return Common.decorateReturnObject(paymentService.getShopsAftersalesRefunds(shopId,id,departId));
    }
    /**
     *管理员创建退款信息
     * @author 王薪蕾
     * @date 2020/12/11
     */
    @ApiOperation(value = "管理员查询订单的退款信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body",dataType = "String",name = "amount", value = "退款金额", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @PostMapping("/shops/{shopId}/payments/{id}/refunds")
    public Object postRefunds(
            @Depart Long departId,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id")  Long id,
            @RequestBody String amount){
            return Common.decoratePostReturnObject(paymentService.postRefunds(shopId,id,amount,departId));
    }

    /**
     * @author issyu 30320182200070
     * @date 2020/12/12 18:47
     * 不在dao层实装DubboReference
     */
    @ApiOperation(value = "查询所有支付状态",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    //@Audit
    @GetMapping("/payments/states")
    public Object postRefunds(//@LoginUser @ApiIgnore Long userId,
                              //@Depart @ApiIgnore Long departId
    ){
        //List<Long> orderIds = orderInnerService.getOrderIdByUserId(userId);

        ReturnObject returnObject = paymentService.getPaymentStateByOrderIds();
        return Common.getListRetObject(returnObject);
    }



    /**
     * 获取买家支付渠道。
     * @author issyu 30320182200070
     * @date 2020/12/14 11:10
     */
    @ApiOperation(value = "查询支付渠道",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/payments/patterns")
//    public Object getPayPatternsByToken(@LoginUser @ApiIgnore Long userId,@Depart @ApiIgnore Long departId){
    public Object getPayPatternsByToken(){

        //List<Long> orderIds = orderInnerService.getOrderIdByUserId(userId);

        ReturnObject returnObject = paymentService.getPayPatternsByOrderId();

        return Common.getListRetObject(returnObject);
    }

    @DubboReference(version = "0.0.1",check = false)
    private IAfterSaleService iAfterSaleService;

    @GetMapping("/test2")
    public Object test2(){
        return iAfterSaleService.verifyAfterSaleId(2L);
    }
    /**
     *买家查询自己的退款信息(订单)
     * @author 王子扬 30320182200071
     * @date 2020/12/11
     */
    @ApiOperation(value = "买家查询自己的退款信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "订单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/orders/{id}/refunds")
    public Object getUsersOrdersRefunds(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable("id")  Long id){
        Long actualUserId=orderInnerService.getUserIdByOrderId(id);
        return Common.decorateReturnObject(paymentService.getUsersOrdersRefunds(userId,id,departId,actualUserId));
    }

    /**
     *买家查询自己的退款信息(售后订单)
     * @author 王子扬 30320182200071
     * @date 2020/12/9 18:10
     */
    @ApiOperation(value = "买家查询自己的退款信息",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "Long",name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}/refunds")
    public Object getUsersAftersalesRefunds(
            @Depart @ApiIgnore Long departId,
            @LoginUser @ApiIgnore Long userId,
            @PathVariable("id")  Long id){
        System.out.println(userId);
        return Common.decorateReturnObject(paymentService.getUsersAftersalesRefunds(userId,id,departId));
    }
}
