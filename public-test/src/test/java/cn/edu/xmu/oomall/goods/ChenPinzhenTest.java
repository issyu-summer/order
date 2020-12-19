package cn.edu.xmu.oomall.goods;

import cn.edu.xmu.ooad.PublicTestApp;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Pinzhen Chen 24320182203173
 * @Date 2020/12/13 20:40
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = PublicTestApp.class)
public class ChenPinzhenTest {

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
        System.out.println(mallGate);

    }


    private String userLogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = mallClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }

    private String adminLogin(String userName, String password) throws Exception{

        JSONObject body = new JSONObject();
        body.put("userName", userName);
        body.put("password", password);
        String requireJson = body.toJSONString();
        byte[] responseString = manageClient.post().uri("/privilege/adminusers/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        return JSONObject.parseObject(new String(responseString, StandardCharsets.UTF_8)).getString("data");
    }

    /**
     * 查看一条spu详细信息（无需登录）
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/13 20:44
     */

    //200: 成功
    @Test
    @Order(0)
    public void getSpuByIdTest1() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/spus/273")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 273,\n" +
                "    \"name\": \"金和汇景•戴荣华•古彩洛神赋瓷瓶\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 71,\n" +
                "      \"name\": \"戴荣华\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 123,\n" +
                "      \"name\": \"大师原作\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"drh-d0001\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "    \"gmtModified\": \"2020-12-10T22:36:01\",\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 273,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 980000,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "        \"inventory\": 1,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 980000\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //404: spuId不存在
    @Test
    @Order(1)
    public void getSpuByIdTest2() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/spus/2")
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
     * 店家新建商品SPU
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/13 21:21
     */
    //200: 成功
    @Test
    @Order(2)
    public void insertSpu1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"decription\": \"string\",\n" +
                "  \"name\": \"string\",\n" +
                "  \"specs\": \"string\"\n" +
                "}";
        byte[] ret = manageClient.post()
                .uri("/shops/1/spus")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"name\": \"string\",\n" +
                "    \"brand\": null,\n" +
                "    \"category\": null,\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"detail\": \"string\",\n" +
                "    \"imageUrl\": null,\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": []\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //505: departId与shopId不匹配
    @Test
    @Order(3)
    public void insertSpu2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"decription\": \"test\",\n" +
                "  \"name\": \"test\",\n" +
                "  \"specs\": \"test\"\n" +
                "}";
        byte[] ret = manageClient.post()
                .uri("/shops/20001/spus")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 505\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    /**
     * 店家修改商品SPU 273
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/13 23:27
     */
    //200: 成功
    @Test
    @Order(4)
    public void updateSpu1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"decription\": \"string\",\n" +
                "  \"name\": \"string\",\n" +
                "  \"specs\": \"string\"\n" +
                "}";
        byte[] ret = manageClient.put()
                .uri("/shops/1/spus/273")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/273")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 273,\n" +
                "    \"name\": \"string\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 71,\n" +
                "      \"name\": \"戴荣华\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 123,\n" +
                "      \"name\": \"大师原作\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"drh-d0001\",\n" +
                "    \"detail\": \"string\",\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"gmtCreate\": \"2020-12-10T22:36:01\",\n" +
                "    \"gmtModified\": \"2020-12-10T22:36:01\",\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 273,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 980000,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\n" +
                "        \"inventory\": 1,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 980000\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    //504: spuId不存在
    @Test
    @Order(5)
    public void updateSpu2() throws Exception {

        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"decription\": \"string\",\n" +
                "  \"name\": \"string\",\n" +
                "  \"specs\": \"string\"\n" +
                "}";
        byte[] ret = manageClient.put()
                .uri("/shops/1/spus/2")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 504\n" +
                "}";
    }


    /**
     * 逻辑删除spu 274
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/13 23:50
     */

    //200: 成功
    @Test
    @Order(6)
    public void deleteSpu1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/spus/273")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/273")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 504\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 店家商品下架 275
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/14 9:25
     */
    //200: 成功
    @Test
    @Order(7)
    public void offshelfSku1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.put()
                .uri("/shops/1/skus/275/offshelves")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" + "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/skus/275")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 275,\n" +
                "    \"skuSn\": null,\n" +
                "    \"name\": \"+\",\n" +
                "    \"detail\": null,\n" +
                "    \"originalPrice\": 4028,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5861d65fa056a.jpg\",\n" +
                "    \"inventory\": 10,\n" +
                "    \"disable\": false,\n" +
                "    \"price\": 4028,\n" +
                "    \"configuration\": null,\n" +
                "    \"weight\": 3,\n" +
                "    \"spu\": {\n" +
                "      \"id\": 275,\n" +
                "      \"name\": \"金和汇景•宝瓷林蓝地扒花开光5头茶具\",\n" +
                "      \"brand\": null,\n" +
                "      \"category\": null,\n" +
                "      \"shop\": null,\n" +
                "      \"goodsSn\": \"bcl-b0002\",\n" +
                "      \"detail\": null,\n" +
                "      \"imageUrl\": \"http://47.52.88.176/file/images/201612/file_5861d65fa056a.jpg\",\n" +
                "      \"spec\": null,\n" +
                "      \"disable\": 0,\n" +
                "      \"skuList\": null\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }

    //504: shopId和departId不匹配
    @Test
    @Order(8)
    public void offshelfSku2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.put()
                .uri("/shops/1/skus/275/offshelves")
                .header("authorization", token)
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
     * 店家商品上架
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/14 9:12
     */

    //200: 成功
    @Test
    @Order(9)
    public void onshelfSku1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.put()
                .uri("/shops/1/skus/275/onshelves")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
    }

    //504: sku状态不合格
    @Test
    @Order(10)
    public void onshelfSku2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.put()
                .uri("/shops/1/skus/275/onshelves")
                .header("authorization", token)
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
     * 管理员新增商品价格浮动
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/14 9:35
     */

    //200: 成功
    @Test
    @Order(11)
    public void insertFloatPrice1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime begin = LocalDateTime.parse("2021-12-14 03:00:52",df);
        LocalDateTime end = LocalDateTime.parse("2021-12-24 03:00:52",df);
        String json = "{\n" +
                "  \"activityPrice\": 10,\n" +
                "  \"beginTime\": \""+begin+"\",\n" +
                "  \"endTime\": \""+end+"\",\n" +
                "  \"quantity\": 1\n" +
                "}";
        byte[] ret = manageClient.post()
                .uri("/shops/1/skus/273/floatPrices")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    //902: 浮动价格时间冲突
    @Test
    @Order(12)
    public void insertFloatPrice2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"activityPrice\": 10,\n" +
                "  \"beginTime\": \"2021-12-12T02:23:01\",\n" +
                "  \"endTime\": \"2021-12-16T02:23:01\",\n" +
                "  \"quantity\": 1\n" +
                "}";
        byte[] ret = manageClient.post()
                .uri("/shops/1/skus/273/floatPrices")
                .header("authorization", token)
                .bodyValue(json)
                .exchange()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.SKUPRICE_CONFLICT.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 902\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    //404: skuId不存在
    @Test
    @Order(13)
    public void insertFloatPrice3() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        String json = "{\n" +
                "  \"activityPrice\": 10,\n" +
                "  \"beginTime\": \"2022-12-12T02:23:01\",\n" +
                "  \"endTime\": \"2022-12-16T02:23:01\",\n" +
                "  \"quantity\": 1\n" +
                "}";
        byte[] ret = manageClient.post()
                .uri("/shops/1/skus/2/floatPrices")
                .header("authorization", token)
                .bodyValue(json)
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
     * 管理员失效商品价格浮动
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/14 9:35
     */

    //200: 成功
    @Test
    @Order(14)
    public void deleteFloatPrice1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/floatPrices/828")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    //404: skuId不存在或浮动价格不存在
    @Test
    @Order(15)
    public void deleteFloatPrice3() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/0/floatPrices/20010")
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
     * 将spu加入品牌
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/15 1:44
     */

    //200: 成功
    @Test
    @Order(16)
    public void insertSpuToBrand1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.post()
                .uri("/shops/1/spus/278/brands/73")
                .header("authorization", token)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" + "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/278")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 278,\n" +
                "    \"name\": \"《大吉大利》A(赠四方联）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 73,\n" +
                "      \"name\": \"黄卖九\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 126,\n" +
                "      \"name\": \"邮品\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0001\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 278,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "        \"inventory\": 46100,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }

    //404: 品牌不存在
    @Test
    @Order(17)
    public void insertSpuToBrand2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.post()
                .uri("/shops/1/spus/278/brands/6666")
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 504\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/278")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 278,\n" +
                "    \"name\": \"《大吉大利》A(赠四方联）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 73,\n" +
                "      \"name\": \"黄卖九\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 126,\n" +
                "      \"name\": \"邮品\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0001\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 278,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "        \"inventory\": 46100,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 将spu移出品牌
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/15 1:44
     */

    //200: 成功
    @Test
    @Order(18)
    public void deleteSpuFromBrand1() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/spus/278/brands/73")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" + "  \"errno\": 0\n" + "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/278")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 278,\n" +
                "    \"name\": \"《大吉大利》A(赠四方联）\",\n" +
                "    \"brand\": null,\n" +
                "    \"category\": {\n" +
                "      \"id\": 126,\n" +
                "      \"name\": \"邮品\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0001\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 278,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "        \"inventory\": 46100,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }

    //404: 品牌不存在
    @Test
    @Order(19)
    public void deleteSpuFromBrand2() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/spus/278/brands/0")
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 504\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/278")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 278,\n" +
                "    \"name\": \"《大吉大利》A(赠四方联）\",\n" +
                "    \"brand\": null,\n" +
                "    \"category\": {\n" +
                "      \"id\": 126,\n" +
                "      \"name\": \"邮品\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0001\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 278,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfb485e1df.jpg\",\n" +
                "        \"inventory\": 46100,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 将spu加入分类 279
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/15 1:44
     */

    //200: 成功
    @Test
    @Order(20)
    public void insertSpuToCategory1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.post()
                .uri("/shops/1/spus/279/categories/123")
                .header("authorization", token)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" + "  \"errno\": 0\n" + "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/279")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 279,\n" +
                "    \"name\": \"《大吉大利》B（赠3轮纸票）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 104,\n" +
                "      \"name\": \"中国集邮总公司\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 123,\n" +
                "      \"name\": \"大师原作\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0002\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 279,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "        \"inventory\": 500,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }

    //404: 分类不存在
    @Test
    @Order(21)
    public void insertSpuToCategory2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.post()
                .uri("/shops/1/spus/279/categories/0")
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 504\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/279")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 279,\n" +
                "    \"name\": \"《大吉大利》B（赠3轮纸票）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 104,\n" +
                "      \"name\": \"中国集邮总公司\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": {\n" +
                "      \"id\": 123,\n" +
                "      \"name\": \"大师原作\"\n" +
                "    },\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0002\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 279,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "        \"inventory\": 500,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 将spu移出分类 279
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/15 1:44
     */

    //200: 成功
    @Test
    @Order(22)
    public void deleteSpuFromCategory1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/spus/279/categories/123")
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/279")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 279,\n" +
                "    \"name\": \"《大吉大利》B（赠3轮纸票）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 104,\n" +
                "      \"name\": \"中国集邮总公司\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": null,\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0002\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 279,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "        \"inventory\": 500,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }

    //404: 分类不存在
    @Test
    @Order(23)
    public void deleteSpuFromCategory2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = manageClient.delete()
                .uri("/shops/1/spus/279/categories/0")
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 504\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
        byte[] queryResponseString = mallClient.get().uri("/spus/279")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse1 = "{\n" +
                "  \"errno\": 0,\n" +
                "  \"data\": {\n" +
                "    \"id\": 279,\n" +
                "    \"name\": \"《大吉大利》B（赠3轮纸票）\",\n" +
                "    \"brand\": {\n" +
                "      \"id\": 104,\n" +
                "      \"name\": \"中国集邮总公司\",\n" +
                "      \"imageUrl\": null\n" +
                "    },\n" +
                "    \"category\": null,\n" +
                "    \"shop\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Nike\"\n" +
                "    },\n" +
                "    \"goodsSn\": \"djdl-a0002\",\n" +
                "    \"detail\": null,\n" +
                "    \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "    \"spec\": null,\n" +
                "    \"disable\": 0,\n" +
                "    \"skuList\": [\n" +
                "      {\n" +
                "        \"id\": 279,\n" +
                "        \"skuSn\": null,\n" +
                "        \"name\": \"+\",\n" +
                "        \"originalPrice\": 1199,\n" +
                "        \"imageUrl\": \"http://47.52.88.176/file/images/201610/file_580cfc4323959.jpg\",\n" +
                "        \"inventory\": 500,\n" +
                "        \"disable\": false,\n" +
                "        \"price\": 1199\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse1, new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 获得优惠券的所有状态
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/15 1:48
     */
    //200: 成功
    @Test
    @Order(24)
    public void getAllCouponStates() throws Exception {
        byte[] ret = mallClient.get()
                .uri("/coupons/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未领取\",\"code\":0},{\"name\":\"已领取\",\"code\":1},{\"name\":\"已使用\",\"code\":2},{\"name\":\"已失效\",\"code\":3}]}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    /**
     * 买家查看自己的优惠券列表
     *
     * @author 24320182203173 Chen Pinzhen
     * @date: 2020/12/16 3:19
     */
    //200: 第一页
    @Test
    @Order(25)
    public void getCouponList1() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = mallClient.get()
                .uri("/coupons?page=1&pageSize=3&state=1")
                .header("authorization", token)
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
                "    \"total\": 3,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 20001,\n" +
                "        \"name\": \"cpz1\",\n" +
                "        \"couponSn\": null,\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 1,\n" +
                "          \"name\": \"string\",\n" +
                "          \"beginTime\": \"2022-12-13T08:02:14\",\n" +
                "          \"endTime\": \"2024-12-13T08:02:14\",\n" +
                "          \"couponTime\": \"2022-12-13T08:02:14\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 0\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 20002,\n" +
                "        \"name\": \"cpz2\",\n" +
                "        \"couponSn\": null,\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 20001,\n" +
                "          \"name\": \"cpz1\",\n" +
                "          \"beginTime\": \"2020-11-01T03:22:30\",\n" +
                "          \"endTime\": \"2021-01-31T03:22:39\",\n" +
                "          \"couponTime\": \"2020-12-01T03:22:52\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 100\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 20003,\n" +
                "        \"name\": \"cpz3\",\n" +
                "        \"couponSn\": null,\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 20002,\n" +
                "          \"name\": \"cpz2\",\n" +
                "          \"beginTime\": \"2020-11-01T03:31:24\",\n" +
                "          \"endTime\": \"2021-01-31T03:31:38\",\n" +
                "          \"couponTime\": \"2020-12-01T03:31:44\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 200\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"pageNum\": 1,\n" +
                "    \"pageSize\": 3\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //200: 第二页
    @Test
    @Order(26)
    public void getCouponList2() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = mallClient.get()
                .uri("/coupons?page=2&pageSize=3&state=1")
                .header("authorization", token)
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
                "    \"total\": 3,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 20004,\n" +
                "        \"name\": \"cpz4\",\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 20002,\n" +
                "          \"name\": \"cpz2\",\n" +
                "          \"beginTime\": \"2020-11-01T03:31:24\",\n" +
                "          \"endTime\": \"2021-01-31T03:31:38\",\n" +
                "          \"couponTime\": \"2020-12-01T03:31:44\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 200\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 20005,\n" +
                "        \"name\": \"cpz5\",\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 20002,\n" +
                "          \"name\": \"cpz2\",\n" +
                "          \"beginTime\": \"2020-11-01T03:31:24\",\n" +
                "          \"endTime\": \"2021-01-31T03:31:38\",\n" +
                "          \"couponTime\": \"2020-12-01T03:31:44\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 200\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 20006,\n" +
                "        \"name\": \"cpz6\",\n" +
                "        \"couponActivitySimpleRetVo\": {\n" +
                "          \"id\": 20002,\n" +
                "          \"name\": \"cpz2\",\n" +
                "          \"beginTime\": \"2020-11-01T03:31:24\",\n" +
                "          \"endTime\": \"2021-01-31T03:31:38\",\n" +
                "          \"couponTime\": \"2020-12-01T03:31:44\",\n" +
                "          \"imageUrl\": null,\n" +
                "          \"quantity\": 200\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


    /**
         * 领取优惠券
         * @author 24320182203173 Chen Pinzhen
         * @date: 2020/12/16 5:43
         */

    //200: 成功
    @Test
    @Order(27)
    public void receiveCoupon1() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20003/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 0\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //905: 优惠券状态禁止--不允许领自己店内的优惠券
    @Test
    @Order(28)
    public void receiveCoupon2() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20001/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.COUPON_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 905\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //909: 未到领取优惠券时间
    @Test
    @Order(29)
    public void receiveCoupon3() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20004/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.COUPON_NOTBEGIN.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 909\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //910: 优惠券领罄
    @Test
    @Order(30)
    public void receiveCoupon4() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20005/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.COUPON_FINISH.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 910\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //911: 优惠卷活动终止
    @Test
    @Order(31)
    public void receiveCoupon5() throws Exception {
        String token = this.adminLogin("13088admin", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20006/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.COUPON_END.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 911\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }

    //905: 优惠券状态禁止--已经领过此活动优惠券
    @Test
    @Order(32)
    public void receiveCoupon6() throws Exception {
        String token = this.adminLogin("537300010", "123456");
        byte[] ret = mallClient.post()
                .uri("/couponactivities/20001/usercoupons")
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.COUPON_STATENOTALLOW.getCode())
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\n" +
                "  \"errno\": 905\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }


}
