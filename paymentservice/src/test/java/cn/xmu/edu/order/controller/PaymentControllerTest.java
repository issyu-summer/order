package cn.xmu.edu.order.controller;

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
}
