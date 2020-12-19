package cn.edu.xmu.order.controller;

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

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

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
                .baseUrl("http://localhost:8086")
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

    /**
     * 标记确认收货
     * @author 王薪蕾
     * @date 2020/12/6
     */
    //order的cosumer不是userid
    //order不存在
    //order夫状态不对
    //order子状态不对
    //正确
    @Test
    public void confirmOrderTest(){
        String token = createTestToken(1L, -2L, 100);
        try {
            byte [] responseString = webTestClient.put().uri("/order/orders/38051/confirm")
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 团购转普通订单
     * @author 王薪蕾
     * @date 2020/12/6
     */
    //order的cosumer不是userid
    //order不存在
    //order夫状态不对
    //order子状态不对
    //正确
    @Test
    public void grouponToNormalOrderTest(){
        String token = createTestToken(1L, -2L, 100);
        String expectStr = "{\"errno\":0,\"errmsg\":\"成功\"}";
        try {
            byte [] responseString = webTestClient.post().uri("/order/orders/1/groupon-normal")
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);

        } catch (UnsupportedEncodingException e) {
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

    /*
     * 买家修改本人名下订单
     * @author 史韬韬
     * @date 2020/12/6
     */
    @Test
    public void changeOrder(){
        String token = createTestToken(1L,0L,10000);
        System.out.println(token);
        String adressJson="{\"consignee\":\"张伟\","+
                "\"regionId\": 123456,\"address\":\"深圳\", \"mobile\":\"112233445566\"}";
        try{
            byte [] responseString = webTestClient.put().uri("/order/orders/{id}",1)
                    .header("authorization",token)
                    .bodyValue(adressJson)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);
            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
     * 买家取消、逻辑删除本人名下订单
     * @author 史韬韬
     * created in 2020/12/6
     */
    @Test
    public void deleteOrder(){
        String token = createTestToken(23L,0L,100);

        try{
            byte [] responseString = webTestClient.delete().uri("/order/orders/{id}",1)
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);
            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
     * 店家查询店内订单完整信息(普通，团购，预售)
     * @author 陈星如
     * @date 2020/12/5 14:55
     */
    @Test
    public void getOrderByShopIdTest(){
        String token = createTestToken(23L,0L,100);

        try{
            byte [] responseString = webTestClient.get().uri("order/shops/1/orders/1")
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
    /**
     * 管理员取消本店铺订单
     * @author 陈星如
     * @date 2020/12/5 15:15
     **/
    @Test
    public void deleteShopOrder(){
        String token = "test";
        String expectStr = "{\"errno\":0,\"errmsg\":\"成功\"}";
        try{
            byte [] responseString = webTestClient.delete().uri("/order/shops/1/orders/1")
                    .header("authorization",token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);
            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
     * 店家对订单标记发货
     * @author 陈星如
     * @date 2020/12/5 15:16
     */
    @Test
    public void shipOrder(){
        String token = createTestToken(1L, 0L, 100);

        String adressJson="{\"consignee\":\"feferkfk\"";
        String expectStr = "{\"errno\":0,\"errmsg\":\"成功\"}";
        try {
            byte [] responseString = webTestClient.put().uri("/order/shops/1/orders/1/deliver")
                    .header("authorization",token)
                    .bodyValue(adressJson)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType("application/json;charset=UTF-8")
                    .expectBody()
                    .returnResult().getResponseBodyContent();
            String responseStr = new String(responseString,"UTF-8");
            System.out.println(responseStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
