package cn.edu.xmu.payment.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "支付服务",tags ="payment")
@RestController
@RequestMapping(value = "/payment",produces = "application/json;charset=UTF-8")
public class PaymentController {
}
