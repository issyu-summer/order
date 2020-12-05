package cn.xmu.edu.order.dao;

import cn.edu.xmu.ooad.util.OrderStateCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.order.OrderServiceApplication;
import cn.edu.xmu.order.dao.OrderDao;
import org.json.JSONException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 0:01
 */
@SpringBootTest(classes = OrderServiceApplication.class)

public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void getOrderStatesTest(){
        Long userId = 8L;
        ReturnObject returnObject=orderDao.getOrderStates(userId);
        List<OrderStateCode> list =new ArrayList<>();
        list= (List<OrderStateCode>) returnObject.getData();
        int i=0;
        for(OrderStateCode orderStateCode:list){
            i++;
            try {
                JSONAssert.assertEquals("6", String.valueOf(orderStateCode.getCode()), true);
                System.out.println(orderStateCode.getCode());
                System.out.println(orderStateCode.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(i);
    }
}
