package cn.edu.xmu.payment.controller;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.payment.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
