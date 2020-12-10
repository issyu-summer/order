package cn.edu.xmu.payment.controller;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.payment.model.vo.AfterSalePaymentVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.payment.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@Api(value = "支付服务",tags ="payment")
@RestController
@RequestMapping(value = "/payment",produces = "application/json;charset=UTF-8")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HttpServletResponse httpServletResponse;
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
            @PathVariable("id") Long id,
            @LoginUser Long userId){
        return Common.decorateReturnObject(paymentService.getAfterSalesPayments(userId,id));
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
    public Object getAfterSalesPayments(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @LoginUser Long userId){
        return Common.decorateReturnObject(paymentService.getAfterSalesPayments(userId,shopId,id));
    }
    /*
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
    @GetMapping("/orders/{id}/payment")
    public Object getPaymentById(@PathVariable Long id){
        return Common.decorateReturnObject(paymentService.getPaymentById(id));
    }
    /*
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
    public Object createPaymentForAftersale(@PathVariable Long id,@RequestBody AfterSalePaymentVo afterSalePaymentVo){
        return Common.decorateReturnObject(paymentService.createPaymentForAftersale(id,afterSalePaymentVo));
    }
    /*
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
    public Object getPaymentByOrderIdAndShopId(@PathVariable Long id,@PathVariable Long shopId){
        return Common.decorateReturnObject(paymentService.getPaymentByOrderIdAndShopId(id,shopId));
    }
    /*
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
            @PathVariable("id")  Long id){
        return Common.decorateReturnObject(paymentService.getShopsOrdersRefunds(shopId,id));
    }
}
