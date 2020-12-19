package cn.edu.xmu.oomall.other;

import cn.edu.xmu.ooad.PublicTestApp;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.LoginVo;

import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * 其他模块售后测试用例
 * @Author :   Jintai Wang 24320182203278
 * @Date :   Created in  2020/12/13 15:57
 * @Modified By :
 */
@SpringBootTest(classes = PublicTestApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WangJintaiTest {

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

    private String login(String userName, String password) throws Exception {
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);
        byte[] ret = manageClient.post().uri("/adminusers/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
    }

    private String user_login(String userName, String password) throws Exception{
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);
        byte[] ret = manageClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");
    }

    /**
     * 获取售后单的所有状态
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(1)
    public void getAftersaleStateTest1() throws Exception {
        // userId = 1

        byte[] ret = mallClient.get()
                .uri("/aftersales/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"name\": \"待管理员审核\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 1,\n" +
                "      \"name\": \"待买家发货\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 2,\n" +
                "      \"name\": \"买家已发货\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 3,\n" +
                "      \"name\": \"待店家退款\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 4,\n" +
                "      \"name\": \"待店家发货\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 5,\n" +
                "      \"name\": \"店家已发货\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 6,\n" +
                "      \"name\": \"审核不通过\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 7,\n" +
                "      \"name\": \"已取消\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 8,\n" +
                "      \"name\": \"已结束\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    /**
     * 买家查询所有售后单信息  - 成功 查询第一页
     * userId=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(2)
    public void queryAllReturnOrderTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales?page=1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1,\"list\":[{\"id\":1,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041007412AM\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0},{\"id\":2,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410460306X\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":1},{\"id\":3,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046073SE\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":2},{\"id\":4,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046075QL\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":3},{\"id\":5,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046077RA\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":4},{\"id\":6,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410460789N\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":5},{\"id\":7,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046080ZB\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":6},{\"id\":8,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046101GR\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7},{\"id\":9,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410461033K\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":8},{\"id\":10,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046105H5\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0}]}}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 买家查询所有售后单信息  - 成功 查询第二页
     * userId=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(3)
    public void queryAllReturnOrderTest2() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales?page=2")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":2,\"list\":[{\"id\":11,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046106QD\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":1},{\"id\":12,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046108CZ\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":2},{\"id\":13,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046132F8\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":3},{\"id\":14,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046134BS\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":4},{\"id\":15,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046135RW\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":5},{\"id\":16,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046137BA\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":6},{\"id\":17,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046139YW\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7},{\"id\":18,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"20201204104614065\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":8},{\"id\":19,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410475991S\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0},{\"id\":20,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048001PO\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":1}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }



    /**
     * 买家查询所有售后单信息  - 成功
     * userId=1 按照起止时间查询
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(4)
    public void queryAllReturnOrderTest4() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales?beginTime=2020-12-04 10:07:41&endTime=2020-12-04 10:46:10")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"pageSize\": 10,\n" +
                "    \"page\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041007412AM\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410460306X\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 1\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046073SE\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046075QL\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 3\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046077RA\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 4\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 6,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410460789N\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 5\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 7,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046080ZB\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 6\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 8,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046101GR\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 7\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 9,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410461033K\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 8\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 管理员查询所有售后单信息  - 成功
     * shopId=1 page=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(5)
    public void getAllAftersalesTest1() throws Exception {

        String token = this.login("13088admin","123456");

        byte[] ret = mallClient.get()
                .uri("/shops/1/aftersales?page=1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"pageSize\": 10,\n" +
                "    \"page\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041007412AM\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410460306X\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 1\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046073SE\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046075QL\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 3\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046077RA\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 4\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 6,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410460789N\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 5\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 7,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 2,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046080ZB\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 6\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 8,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046101GR\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 7\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 9,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"2020120410461033K\",\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 8\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 10,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 1,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": \"202012041046105H5\",\n" +
                "        \"type\": 1,\n" +
                "        \"reason\": \"string\",\n" +
                "        \"refund\": 0,\n" +
                "        \"quantity\": 0,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"detail\",\n" +
                "        \"consignee\": \"string\",\n" +
                "        \"mobile\": \"15306987163\",\n" +
                "        \"customerLogSn\": \"\",\n" +
                "        \"shopLogSn\": \"\",\n" +
                "        \"state\": 0\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }


    /**
     * 管理员查询所有售后单信息  - 成功
     * shopId=0(查所有) page=2 pagesize=15
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(6)
    public void getAllAftersalesTest2() throws Exception {

        String token = this.login("13088admin","123456");

        byte[] ret = mallClient.get()
                .uri("/shops/0/aftersales?page=2&pagesize=15")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"pageSize\":15,\"page\":2,\"list\":[{\"id\":16,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046137BA\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":6},{\"id\":17,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046139YW\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7},{\"id\":18,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"20201204104614065\",\"type\":1,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":8},{\"id\":19,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410475991S\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0},{\"id\":20,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048001PO\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":1},{\"id\":21,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048002NM\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":2},{\"id\":22,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410480045A\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":3},{\"id\":23,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"20201204104800602\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":4},{\"id\":24,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048007H6\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":5},{\"id\":25,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410480099R\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":6},{\"id\":26,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048011NA\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7},{\"id\":27,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048012TM\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":8},{\"id\":28,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041048014JB\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0},{\"id\":29,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":2,\"serviceSn\":\"202012041048027JS\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0},{\"id\":30,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":2,\"serviceSn\":\"202012041048029GL\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0}]},\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }
    /**
     * 买家根据售后单id查询售后单信息  - 成功
     * userId=1 售后单Id = 25
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(7)
    public void getOneAllAftersaleOrderTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales/25")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 25,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"2020120410480099R\",\n" +
                "    \"type\": 2,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 6\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }


    /**
     * 买家根据售后单id查询售后单信息  - 失败 customerId和userId不对应
     * userId=2 售后单Id = 1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(8)
    public void getOneAllAftersaleOrderTest2() throws Exception {
        // userId = 2
        String token = this.user_login("36040122840","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":505}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 买家根据售后单id查询售后单信息  - 失败 aftersaleId超出范围
     * userId = 1 售后单id = -1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(9)
    public void getOneAllAftersaleOrderTest3() throws Exception {
        // userId = 2
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.get()
                .uri("/aftersales/-1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":504}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 管理员根据售后单id查询售后单信息  - 成功
     * 售后单id = 1 店铺id = 1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(10)
    public void getAftersaleOrderByIdAdminTest1() throws Exception {
        // admin
        String token = this.login("13088admin","123456");

        byte[] ret = mallClient.get()
                .uri("/shops/1/aftersales/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 1,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041007412AM\",\n" +
                "    \"type\": 0,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 0\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 管理员根据售后单id查询售后单信息  - 失败 shopId和aftersaleId不对应
     * 售后单id = 1 shopId = 2
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(11)
    public void getAftersaleOrderByIdAdminTest2() throws Exception {
        // admin
        String token = this.login("13088admin","123456");

        byte[] ret = mallClient.get()
                .uri("/shops/2/aftersales/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":505}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }


    /**
     * 管理员根据售后单id查询售后单信息  - 失败 aftersaleId 超出范围
     * 售后单id = 1 shopId = 2
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(12)
    public void getAftersaleOrderByIdAdminTest3() throws Exception {
        // admin
        String token = this.login("13088admin","123456");

        byte[] ret = mallClient.get()
                .uri("/shops/1/aftersales/-1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":504}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }


    /**
     * 买家创建售后单测试  - 成功
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(13)
    public void createAftersaleTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"consignee\":\"王金泰\",\"detail\":\"详细地址\",\"mobile\":\"15306987188\",\"quantity\":1,\"reason\":\"七天无理由退货\",\"regionId\":1,\"type\":0}";

        byte[] ret = mallClient.post()
                .uri("/orderItems/1/aftersales")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"type\": 0,\n" +
                "    \"reason\": \"七天无理由退货\",\n" +
                "    \"refund\": null,\n" +
                "    \"quantity\": 1,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"详细地址\",\n" +
                "    \"consignee\": \"王金泰\",\n" +
                "    \"mobile\": \"15306987188\",\n" +
                "    \"customerLogSn\": null,\n" +
                "    \"shopLogSn\": null,\n" +
                "    \"state\": 0\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");
        // 获取id
        int startIndex = temp.indexOf("id");
        int endIndex = temp.indexOf("orderId");
        String id = temp.substring(startIndex + 4, endIndex - 2);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/"+id).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        JSONAssert.assertEquals(new String(ret, "UTF-8"), new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家修改售后单信息  - 成功
     * userId =1 aftersaleId = 1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(14)
    public void changeAftersaleTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"consignee\":\"21世纪\",\"detail\":\"厦门大学\",\"mobile\":\"15306987164\",\"quantity\":1,\"reason\":\"无理由退款\",\"regionId\":1}";

        byte[] ret = mallClient.put()
                .uri("/aftersales/1")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/1").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":1,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041007412AM\",\"type\":0,\"reason\":\"无理由退款\",\"refund\":0,\"quantity\":1,\"regionId\":1,\"detail\":\"厦门大学\",\"consignee\":\"21世纪\",\"mobile\":\"15306987164\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0}}";

        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家修改售后单信息  - 失败
     * userId =1 aftersaleId = 8(state=7 已取消)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(15)
    public void changeAftersaleTest2() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"consignee\":\"21世纪\",\"detail\":\"厦门大学\",\"mobile\":\"15306987164\",\"quantity\":1,\"reason\":\"无理由退款\",\"regionId\":1}";

        byte[] ret = mallClient.put()
                .uri("/aftersales/8")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/8").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":8,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046101GR\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7}}";

        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家修改售后单信息  - 失败 售后单不存在
     * userId =1 aftersaleId = -1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     * Modified in 2020/12/16
     */
    @Test
    @Order(16)
    public void changeAftersaleTest3() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"consignee\":\"21世纪\",\"detail\":\"厦门大学\",\"mobile\":\"15306987164\",\"quantity\":1,\"reason\":\"无理由退款\",\"regionId\":1}";

        byte[] ret = mallClient.put()
                .uri("/aftersales/-1")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":504}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

    }

    /**
     * 买家取消售后单或逻辑删除售后单 - 取消售后单
     * userId =1 aftersaleId = 1(state=0 新建态)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(17)
    public void deleteAftersaleTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.delete()
                .uri("/aftersales/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/1").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":1,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041007412AM\",\"type\":0,\"reason\":\"无理由退款\",\"refund\":0,\"quantity\":1,\"regionId\":1,\"detail\":\"厦门大学\",\"consignee\":\"21世纪\",\"mobile\":\"15306987164\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":7}}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家取消售后单或逻辑删除售后单 - 逻辑删除售后单
     * userId =1 aftersaleId = 9(state=8 已结束)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(18)
    public void deleteAftersaleTest2() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.delete()
                .uri("/aftersales/9")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/9").header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":504}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }


    /**
     * 买家取消售后单或逻辑删除售后单 - 售后单状态禁止
     * userId =1 aftersaleId = 3(state=2 )
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(19)
    public void deleteAftersaleTest3() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.delete()
                .uri("/aftersales/3")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/3").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":3,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"202012041046073SE\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":2}}";

        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家填写售后的运单信息 - 成功
     * userId =1 aftersaleId = 2(state=1 待买家发货)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(20)
    public void sendbackAftersaleTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"logSn\":\"logSn\"}";
        byte[] ret = mallClient.put()
                .uri("/aftersales/2/sendback")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/2").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString ="{\"errno\":0,\"data\":{\"id\":2,\"orderId\":null,\"orderItemId\":2,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410460306X\",\"type\":0,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"logSn\",\"shopLogSn\":\"\",\"state\":2}}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家填写售后的运单信息 - 售后单状态禁止
     * userId =1 aftersaleId = 19(此时 state=0)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(21)
    public void sendbackAftersaleTest2() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");
        String requireJson = "{\"logSn\":\"logSn\"}";
        byte[] ret = mallClient.put()
                .uri("/aftersales/19/sendback")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/19").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":19,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410475991S\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0}}";

        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }


    /**
     * 买家确认售后单结束 - 售后单状态禁止
     * userId =1 aftersaleId = 19(此时 state=0)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(22)
    public void confirmAftersaleTest1() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.put()
                .uri("/aftersales/19/confirm")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/19").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\"errno\":0,\"data\":{\"id\":19,\"orderId\":null,\"orderItemId\":1,\"customerId\":1,\"shopId\":1,\"serviceSn\":\"2020120410475991S\",\"type\":2,\"reason\":\"string\",\"refund\":0,\"quantity\":0,\"regionId\":1,\"detail\":\"detail\",\"consignee\":\"string\",\"mobile\":\"15306987163\",\"customerLogSn\":\"\",\"shopLogSn\":\"\",\"state\":0}}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }
    /**
     * 确认售后单结束 - 成功
     * userId =1 aftersaleId = 13(待店家退款 type为退货单)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(23)
    public void confirmAftersaleTest2() throws Exception {
        // userId = 1
        String token = this.user_login("8606245097","123456");

        byte[] ret = mallClient.put()
                .uri("/aftersales/13/confirm")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/13").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 13,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041046132F8\",\n" +
                "    \"type\": 1,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 8\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 管理员同意（退款，换货，维修） - 成功 修改为退货单
     * userId =1 aftersaleId = 28(新建态,type=0)
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(24)
    public void confirmAftersaleTest3() throws Exception {
        // userId = 1
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"同意\",\"confirm\":true,\"price\":-100,\"type\":1}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/28/confirm")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/28").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 28,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041048014JB\",\n" +
                "    \"type\": 1,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": -100,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 1\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 管理员不同意（退款，换货，维修） - 成功
     * userId =1 aftersaleId = 29(新建态) shopid=2 type=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(25)
    public void confirmAftersaleTest4() throws Exception {
        // userId = 1
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"不符合要求\",\"confirm\":false,\"price\":100,\"type\":1}";
        byte[] ret = mallClient.put()
                .uri("/shops/2/aftersales/29/confirm")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/29").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 29,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 2,\n" +
                "    \"serviceSn\": \"202012041048027JS\",\n" +
                "    \"type\": 0,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 6\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 管理员同意/不同意（退款，换货，维修） - 状态禁止
     * userId =1 aftersaleId = 21(state=2) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(26)
    public void confirmAftersaleTest5() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"不符合要求\",\"confirm\":false,\"price\":100,\"type\":1}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/21/confirm")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/21").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 21,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041048002NM\",\n" +
                "    \"type\": 2,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 2\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家确认收到买家的退货 - 同意成功
     * userId =1 aftersaleId = 3(state=2 type=0 退货单) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(27)
    public void shopConfirmReceiveTest1() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"收到的货物满足条件\",\"confirm\":true}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/3/receive")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/3").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 3,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 2,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041046073SE\",\n" +
                "    \"type\": 0,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 4\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家确认收到买家的退货 - 不同意成功
     * userId =1 aftersaleId = 21(state=2) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(28)
    public void shopConfirmReceiveTest2() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"收到的货物不满足条件\",\"confirm\":false}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/21/receive")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/21").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 21,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041048002NM\",\n" +
                "    \"type\": 2,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 1\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家确认收到买家的退货 - 售后状态禁止
     * userId =1 aftersaleId = 27(state=8) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(29)
    public void shopConfirmReceiveTest3() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"conclusion\":\"收到的货物不满足条件\",\"confirm\":false}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/27/receive")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/27").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString ="{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 27,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041048012TM\",\n" +
                "    \"type\": 2,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 8\n" +
                "  },\n" +
                "  \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家寄出维修好（调换）的货物 - 售后状态禁止
     * userId =1 aftersaleId = 14(type=1) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(30)
    public void deliverAfterServiceTest1() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"logSn\":\"shopLogSn\"}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/14/deliver")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":609}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/14").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 14,\n" +
                "    \"orderId\": null,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041046134BS\",\n" +
                "    \"type\": 1,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 4\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家寄出维修好（调换）的货物 - 成功
     * userId =1 aftersaleId = 5(type=0 状态合法) shopid=1
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(31)
    public void deliverAfterServiceTest2() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"logSn\":\"shopLogSn\"}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/5/deliver")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";

        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/5").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 5,\n" +
                "    \"orderItemId\": 2,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"202012041046077RA\",\n" +
                "    \"type\": 0,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"\",\n" +
                "    \"state\": 5\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 店家寄出维修好（调换）的货物 - 成功
     * userId =1 aftersaleId = 23(type=2 状态合法) shopid=1 会填写logSn
     * @author Jintai Wang 24320182203278
     * Created in 2020/12/13
     */
    @Test
    @Order(32)
    public void deliverAfterServiceTest3() throws Exception {
        //admin
        String token = this.login("13088admin","123456");
        String requireJson = "{\"logSn\":\"shopLogSn\"}";
        byte[] ret = mallClient.put()
                .uri("/shops/1/aftersales/23/deliver")
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0}";

        JSONAssert.assertEquals(expectedResponse, responseString, false);

        String temp = new String(ret, "UTF-8");

        byte[] queryResponseString = mallClient.get().uri("/aftersales/23").header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponseString = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 23,\n" +
                "    \"orderItemId\": 1,\n" +
                "    \"customerId\": 1,\n" +
                "    \"shopId\": 1,\n" +
                "    \"serviceSn\": \"20201204104800602\",\n" +
                "    \"type\": 2,\n" +
                "    \"reason\": \"string\",\n" +
                "    \"refund\": 0,\n" +
                "    \"quantity\": 0,\n" +
                "    \"regionId\": 1,\n" +
                "    \"detail\": \"detail\",\n" +
                "    \"consignee\": \"string\",\n" +
                "    \"mobile\": \"15306987163\",\n" +
                "    \"customerLogSn\": \"\",\n" +
                "    \"shopLogSn\": \"shopLogSn\",\n" +
                "    \"state\": 5\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponseString, new String(queryResponseString, "UTF-8"), false);
    }
}
