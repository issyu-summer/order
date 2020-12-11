package cn.edu.xmu.payment.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.payment.PaymentServiceApplication;
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

@SpringBootTest(classes = PaymentServiceApplication.class)
public class PaymentControllerTest {

    private WebTestClient webTestClient;

    public PaymentControllerTest(){
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
     * 买家查询自己售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    @Test
    public void getAfterSalesPayments(){
        //根据数据库aftersaleId不存在返回售后单
        String token = createTestToken(2L,0L,100);
        try{
            byte [] responseString = webTestClient.get().uri("/payment/aftersales/2/payments")
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
     * 管理员查询售后单的支付信息
     * @author 王薪蕾
     * @date 2020/12/9
     */
    @Test
    public void getShopAfterSalesPayments(){
        String token = createTestToken(23L,0L,100);
        try{
            byte [] responseString = webTestClient.get().uri("/payment/shops/2/aftersales/2/payments")
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
     * @author 史韬韬
     * @date 2020/12/9
     * 买家查询自己的支付信息
     */
    @Test
    public void getPaymentById(){
        String token = createTestToken(23L,0L,100);
        String expectedResponse ="{\"errno\":0,\"data\":{\"id\":1,\"orderId\":1"+
                ",\"aftersaleId\":null,\"amount\":0,\"actualAmount\":0,\"payTime\":\"2020-12-01T17:04:55\","+
                "\"paymentPattern\":0,\"state\":0,\"beginTime\":\"2020-12-01T17:04:55\",\"endTime\":"+
                "\"2020-12-01T17:04:55\",\"gmtCreate\":\"2020-12-01T17:04:55\",\"gmtModified\":"+
                "\"2020-12-01T17:04:55\"},\"errmsg\":\"成功\"}";
        try{
            byte [] responseString = webTestClient.get().uri("/payment/orders/{id}/payment",1)
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
     * @author 史韬韬
     * @date 2020/12/10
     * 买家为售后单创建支付单
     */
    @Test
    public void createPaymentForAftersale(){
        String token = createTestToken(23L,0L,100);
        String paymentJson="{\"price\":100,\"paymentPattern\":1}";
        try{
            byte [] responseString = webTestClient.post().uri("/payment/aftersales/{id}/payments",1)
                    .header("authorization",token)
                    .bodyValue(paymentJson)
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
     * @author 史韬韬
     * @date 2020/12/10
     * 管理员查看订单支付信息
     */
    @Test
    public void getPaymentByOrderIdAndShopId(){
        String token = createTestToken(23L,0L,100);
        try{
            byte [] responseString = webTestClient.get().uri("/payment/shops/{shopId}/orders/{id}/payments",1,1)
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
     *管理员查询订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:13
     */
    @Test
    public void getShopsOrdersRefunds(){
        String token = createTestToken(1L,0L,100);
        try{
            byte [] responseString = webTestClient.get().uri("/payment/shops/{shopId}/orders/1/refunds",1)
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
     *管理员查询售后订单的退款信息
     * @author 陈星如
     * @date 2020/12/9 18:10
     */
    @Test
    public void getShopsAftersalesRefunds(){
        String token = createTestToken(1L,0L,100);
        try{
            byte [] responseString = webTestClient.get().uri("/payment/shops/1/aftersales/1/refunds")
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
     *管理员创建退款信息
     * @author 王薪蕾
     * @date 2020/12/11
     */
    @Test
    public void postRefunds(){
        String token = createTestToken(23L,0L,100);
        String amount = "10";
        try{
            byte [] responseString = webTestClient.post().uri("/payment/shops/1/payments/1/refunds",1)
                    .header("authorization",token)
                    .bodyValue(amount)
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
}
