package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.PublicTestApp;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.LoginVo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.JsonPathAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = PublicTestApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MingQiuTest {
    @Value("${public-test.managementgate}")
    private String managementGate;

    @Value("${public-test.mallgate}")
    private String mallGate;

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setUp(){

        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://"+managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://"+mallGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

    }


    //卖家登录
    private String userLogin(String userName, String password){
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);

        byte[] responseString = mallClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseObject(new String(responseString), "data", String.class);
    }

    private String adminLogin(String userName, String password) throws Exception {
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);
        byte[] ret = manageClient.post().uri("/adminusers/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
        //endregion
    }
    @Test
    @Order(1)
    public void shareTest1(){
        String token = this.userLogin("19769355952", "123456");

        byte[] responseString = mallClient.get().uri("/share/1/skus/517")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String data = JacksonUtil.parseString(new String(responseString),"data");
        Integer id = JacksonUtil.parseInteger(data, "id");
        assertEquals(517, id);
        Integer originalPrice = JacksonUtil.parseInteger(data, "originalPrice");
        assertEquals(219, originalPrice);
        Integer price = JacksonUtil.parseInteger(data, "originalPrice");
        assertEquals(200, price);
        Integer inventory = JacksonUtil.parseInteger(data, "inventory");
        assertEquals(9997, inventory);
        Integer weight = JacksonUtil.parseInteger(data, "weight");
        assertEquals(1000, weight);
    }

    @Test
    @Order(2)
    public void beShareTest1(){
        String token = this.userLogin("17857289610", "123456");

        byte[] responseString = mallClient.get().uri("/beshared?skuid=517")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        String data = JacksonUtil.parseString(new String(responseString),"data");
        List<String> list = JacksonUtil.parseObjectList(data, "list", String.class);
        assertEquals(1, list.size());
        String beshared = list.get(0);
        Integer shareId = JacksonUtil.parseInteger(beshared, "shareId");
        assertEquals(1, shareId);
        Integer customerId = JacksonUtil.parseInteger(beshared, "customerId");
        assertEquals(9, customerId);
    }

    @Test
    public void cartTest1(){
        String token = this.userLogin("17857289610", "123456");
        Map<String, Object> cart = new HashMap<>();
        cart.put("goodsSkuId", 517);
        cart.put("quantity",1);
        String requireJson = JacksonUtil.toJson(cart);

        byte[] responseString = mallClient.post().uri("/carts")
                .header("authorization",token).bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        cart = new HashMap<>();
        cart.put("goodsSkuId", 518);
        cart.put("quantity",1);
        requireJson = JacksonUtil.toJson(cart);

        responseString = mallClient.post().uri("/carts")
                .header("authorization",token).bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();

        responseString = mallClient.get().uri("/carts")
                .header("authorization",token).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data.list").isArray()
                .jsonPath("$..list[?(@.goodsSkuId == 518)].couponActivity[?(@.id ==12159)]").exists()
                .jsonPath("$..list[?(@.goodsSkuId == 517)].couponActivity[?(@.id ==12158)]").exists()
                .returnResult()
                .getResponseBodyContent();
    }
}
