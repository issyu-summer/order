package cn.edu.xmu.test.controller;

import cn.edu.xmu.outer.model.bo.MyReturn;
import cn.edu.xmu.outer.service.IOrderService;
import cn.edu.xmu.inner.service.OrderInnerService;
import cn.edu.xmu.inner.service.PaymentInnerService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 21:15
 */
@Api(value = "消费者服务",tags ="customer")
@RestController
@RequestMapping(value = "/customer",produces = "application/json;charset=UTF-8")
public class CustomerController {

    @DubboReference(check = false)
    private OrderInnerService orderInnerService;
    /**
     * 根据用户id获取订单id
     * @author issyu 30320182200070
     * @date 2020/12/10 17:02
     */
    @DubboReference
    private IOrderService iOrderService;

    @DubboReference
    private PaymentInnerService paymentInnerService;


    /**
     * 内部集成,通过userId获取订单id测试
     * @author issyu 30320182200070
     * @date 2020/12/15 0:19
     * @return
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
     * 外部接口测试示例.通
     * @author issyu 30320182200070
     * @date 2020/12/12 21:33
     */
    @GetMapping("/orderitems")
    private Object getOrderItems(){
        List<Long> ids=new ArrayList<>();
        ids.add(1L);
        if(iOrderService.getOrderItems(ids)==null){
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST,"无订单明细");
        }else{
            return iOrderService.getOrderItems(ids);

        }
    }

    @GetMapping("/orderitemlist")
    private Object getOrderItemList(){
        Long id=1L;
        MyReturn myReturn = iOrderService.getOrderItemList(1L);
        return myReturn;
    }


    @GetMapping("/updateOrderId")
    private Object updateOrderId(){
        return paymentInnerService.updateRefundStateByOrderId(1L);
    }
}
