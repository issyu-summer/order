package cn.xmu.edu.order.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.order.OrderServiceApplication;
import cn.edu.xmu.order.controller.OrderController;
import cn.edu.xmu.order.service.OrderService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author issyu 30320182200070
 * @date 2020/12/3 10:15
 */
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderControllerTest {

    private WebTestClient webTestClient;

    public OrderControllerTest(){
        //绑定****正在运行的服务器****
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
        //this.webTestClient = WebTestClient.bindToController(new OrderController()).build();
    }

    /**
     * 创建测试用token
     * @author issyu 30320182200070
     * @date 2020/12/3 10:58
     */
    private final String createTestToken(Long userId,Long departId,int expireTime){
        String token = new JwtHelper().createToken(userId,departId,expireTime);
        return token;
    }


    /**
     * 获取订单的所有状态 成功
     * @author issyu 30320182200070
     * @date 2020/12/3 11:00
     */
    @Test
    public void getOrderStates(){
        //将userId替换为1，将有19个订单状态。userId为23时将有1个订单状态。
        //String responseString = null;
        String token = createTestToken(23L,0L,100);

        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\",\"data\": [{\"code\":6,\"name\":\"创建订单\"}]}";
        try{
                    byte [] responseString = webTestClient.get().uri("/order/orders/states")
                                                .header("authorization",token)
                                                .exchange()
                                                .expectStatus().isOk()
                                                .expectHeader().contentType("application/json;charset=UTF-8")
                                                .expectBody()
                                                .returnResult().getResponseBodyContent();
                    String responseStr = new String(responseString,"UTF-8");
                    System.out.println(responseStr);
                    JSONAssert.assertEquals(expectedResponse,responseStr,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 买家查询本人名下所有订单概要
     * @author issyu 30320182200070
     * @date 2020/12/3 17:06
     */
    @Test
    public void getOrderSimpleInfo(){

        //将userId替换为1，将有19个订单状态。userId为23时将有1个订单状态。
        //String responseString = null;
        String token = createTestToken(23L,0L,100);

        String expectedResponse =
                "{\"errno\":0,\"errmsg\":\"成功\",\"data\":{\"page\":1,\"pageSize\":10,\"total\":1,\"pages\":1,\"list\":[{\"id\":17656,\"customerId\":23,\"shopId\":1,\"pid\":2,\"orderType\":1,\"state\":6,\"subState\":6,\"gmtCreateTime\":\"2020-12-01T17:33:10\",\"originPrice\":20,\"discountPrice\":30,\"freightPrice\":40}]}}";
        try{
            byte [] responseString = webTestClient.get().uri("/order/orders")
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);
            JSONAssert.assertEquals(expectedResponse,responseStr,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    * 查询订单完整信息
    * @author 史韬韬
    * @date 2020/12/6
     */
    @Test
    public void getOrderById(){
        String token = createTestToken(23L,0L,100);

        try{
            byte [] responseString = webTestClient.get().uri("/order/orders/{id}",1)
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
