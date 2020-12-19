package cn.edu.xmu.customer.controller;

import cn.edu.xmu.outer.service.IOrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
/**
 * @author issyu 30320182200070
 * @date 2020/12/17 22:39
 */
@RestController
@RequestMapping("/test")
public class CustomerController {

    @DubboReference(version = "0.0.1")
    private IOrderService iOrderService;

    @GetMapping("/test")
    public Object test(){
        List<Long> list = new ArrayList<>();
        list.add(2L);
        return iOrderService.getOrderItemInfo(1L);   //这个逻辑不对
        //return iOrderService.aftersaleSendback(2L);
        //return iOrderService.getOrderItems(list);
    }
}
