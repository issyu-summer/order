package cn.edu.xmu.oomall.other;


import cn.edu.xmu.ooad.Application;
import cn.edu.xmu.ooad.PublicTestApp;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.LoginVo;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;

/**
 * 陈芸衣
 * 24320182203182
 */
@SpringBootTest(classes = PublicTestApp.class)

public class ChenyunyiTest {


    @Value("${public-test.managementgate}")
    private String managementGate;

    @Value("${public-test.mallgate}")
    private String mallGate;

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    @BeforeEach
    public void setup(){
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl("http://"+managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://"+mallGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

    }

    private String Userlogin(String userName, String password) throws Exception {
        LoginVo vo = new LoginVo();
        vo.setUserName(userName);
        vo.setPassword(password);
        String requireJson = JacksonUtil.toJson(vo);

        byte[] ret = manageClient.post().uri("/users/login").bodyValue(requireJson).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        return JacksonUtil.parseString(new String(ret, "UTF-8"), "data");

    }

    /**
     * 获得售后单所有状态
     * @throws Exception
     */
    @Test
    public void getSaleStateTest1() throws Exception{

        byte[] responseString=mallClient.get().uri("/aftersales/states")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBodyContent();
        String expected="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"code\": 0,\n" +
                "            \"name\": \"待管理员审核\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 1,\n" +
                "            \"name\": \"待买家发货\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 2,\n" +
                "            \"name\": \"买家已发货\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 3,\n" +
                "            \"name\": \"待店家退款\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 4,\n" +
                "            \"name\": \"待店家发货\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 5,\n" +
                "            \"name\": \"店家已发货\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 6,\n" +
                "            \"name\": \"审核不通过\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 7,\n" +
                "            \"name\": \"已取消\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": 8,\n" +
                "            \"name\": \"已结束\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), true);
    }


    /**
     * 买家提交售后单
     * 售后类型为空
     */
    @Test
    public void newSaleTest2() throws Exception {
        String token=this.Userlogin("8606245097","123456");
        String requireJson="{ \"type\": null, \"quantity\": 0, \"reason\": \"七天无理由\", \"regionId\": 1, \"detail\": \"厦大学生公寓\", \"consignee\": \"小陈\", \"mobile\": \"18912345678\"}";
        byte[] ret=mallClient.post().uri( "/orderItems/{id}/aftersales",39)
                .header("authorization",token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
//                .jsonPath("$.errmsg").isEqualTo("售后类型不能为空;")
                .returnResult()
                .getRequestBodyContent();
    }

    /**
     * 买家提交售后单
     * orderItemId不存在
     */
    @Test
    public void newSaleTest3() throws Exception {
        String token=this.Userlogin("8606245097","123456");
        String requireJson="{ \"type\": 2, \"quantity\": 2, \"reason\": \"七天无理由\", \"regionId\": 1, \"detail\": \"厦大学生公寓\", \"consignee\": \"小陈\", \"mobile\": \"18912345678\"}";

        byte[] ret=mallClient.post().uri( "/orderItems/{id}/aftersales",65498)
                .bodyValue(requireJson)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 买家根据售后单状态和类型查询售后单
     * 查询page=1,pagesize=3 售后类型为0
     * @return
     */
    @Test
    public void userGetSaleTest() throws Exception {
        String token=this.Userlogin("8606245097","123456");

        byte[] responseString = mallClient.get().uri(uriBuilder -> uriBuilder.path("aftersales")
                .queryParam("page",1)
                .queryParam("pageSize",3)
                .queryParam("type",0)
                .build())
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();


        String expected=" {\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"total\": 3,\n" +
                "        \"pages\": 1,\n" +
                "        \"pageSize\": 3,\n" +
                "        \"page\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"id\": 1,\n" +
                "                \"orderId\": null,\n" +
                "                \"orderItemId\": 1,\n" +
                "                \"customerId\": 1,\n" +
                "                \"shopId\": 1,\n" +
                "                \"serviceSn\": \"202012041007412AM\",\n" +
                "                \"type\": 0,\n" +
                "                \"reason\": \"string\",\n" +
                "                \"refund\": 0,\n" +
                "                \"quantity\": 0,\n" +
                "                \"regionId\": 1,\n" +
                "                \"detail\": \"detail\",\n" +
                "                \"consignee\": \"string\",\n" +
                "                \"mobile\": \"15306987163\",\n" +
                "                \"customerLogSn\": \"\",\n" +
                "                \"shopLogSn\": \"\",\n" +
                "                \"state\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 2,\n" +
                "                \"orderId\": null,\n" +
                "                \"orderItemId\": 2,\n" +
                "                \"customerId\": 1,\n" +
                "                \"shopId\": 1,\n" +
                "                \"serviceSn\": \"2020120410460306X\",\n" +
                "                \"type\": 0,\n" +
                "                \"reason\": \"string\",\n" +
                "                \"refund\": 0,\n" +
                "                \"quantity\": 0,\n" +
                "                \"regionId\": 1,\n" +
                "                \"detail\": \"detail\",\n" +
                "                \"consignee\": \"string\",\n" +
                "                \"mobile\": \"15306987163\",\n" +
                "                \"customerLogSn\": \"\",\n" +
                "                \"shopLogSn\": \"\",\n" +
                "                \"state\": 1\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 3,\n" +
                "                \"orderId\": null,\n" +
                "                \"orderItemId\": 2,\n" +
                "                \"customerId\": 1,\n" +
                "                \"shopId\": 1,\n" +
                "                \"serviceSn\": \"202012041046073SE\",\n" +
                "                \"type\": 0,\n" +
                "                \"reason\": \"string\",\n" +
                "                \"refund\": 0,\n" +
                "                \"quantity\": 0,\n" +
                "                \"regionId\": 1,\n" +
                "                \"detail\": \"detail\",\n" +
                "                \"consignee\": \"string\",\n" +
                "                \"mobile\": \"15306987163\",\n" +
                "                \"customerLogSn\": \"\",\n" +
                "                \"shopLogSn\": \"\",\n" +
                "                \"state\": 2\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);

    }

    /**
     * 买家根据售后单状态和类型查询售后单
     * 查询售后状态为0 page=1 pagesize=3 customerId=2
     * @return
     */
    @Test
    public void userGetSaleTest2() throws Exception {

        String token=this.Userlogin("36040122840","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.get().uri(uriBuilder -> uriBuilder.path("aftersales")
                .queryParam("page",1)
                .queryParam("pageSize",3)
                .queryParam("state",0)
                .build())
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected=" {\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"total\": 1,\n" +
                "        \"pages\": 1,\n" +
                "        \"pageSize\": 3,\n" +
                "        \"page\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"id\": 54,\n" +
                "                \"orderId\": null,\n" +
                "                \"orderItemId\": 39,\n" +
                "                \"customerId\": 2,\n" +
                "                \"shopId\": 1,\n" +
                "                \"serviceSn\": null,\n" +
                "                \"type\": 0,\n" +
                "                \"reason\": \"七天无理由\",\n" +
                "                \"refund\": null,\n" +
                "                \"quantity\": 1,\n" +
                "                \"regionId\": 1,\n" +
                "                \"detail\": \"厦大学生公寓\",\n" +
                "                \"consignee\": \"Chen\",\n" +
                "                \"mobile\": \"12345678900\",\n" +
                "                \"customerLogSn\": null,\n" +
                "                \"shopLogSn\": null,\n" +
                "                \"state\": 0\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }


    /**
     * 买家根据售后单id查询售后单信息
     * @return
     */
    @Test
    public void findByIdTest1() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.get().uri("/aftersales/{id}",51)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"id\": 51,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 39,\n" +
                "        \"skuId\": 341,\n" +
                "        \"skuName\": null,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\": 1,\n" +
                "        \"serviceSn\": null,\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"七天无理由\",\n" +
                "        \"refund\": 10,\n" +
                "        \"quantity\": 1,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"厦大学生公寓\",\n" +
                "        \"consignee\": \"Chen\",\n" +
                "        \"mobile\": \"12345678900\",\n" +
                "        \"customerLogSn\": null,\n" +
                "        \"shopLogSn\": null,\n" +
                "        \"state\": 0\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家根据售后单id查询售后单信息
     * 售后单Id不存在
     * @return
     */
    @Test
    public void findByIdTest2() throws Exception {
        String token=this.Userlogin("8606245097","123456");
        byte[] responseString = mallClient.get().uri("/aftersales/{id}",987456)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();


    }

    /**
     * 买家根据售后单id查询售后单信息
     * 查询不是自己的售后单
     * @return
     */
    @Test
    public void findByIdTest3() throws Exception {

        String token=this.Userlogin("36040122840","123456");
        //String token=new JwtHelper().createToken(2L,1L,1);
        byte[] responseString = mallClient.get().uri("/aftersales/{id}",51)
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 505\n" +
//                "    \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }


    /**
     * 买家根据售后单id查询售后单信息
     * 查询已经逻辑删除的售后单
     * @return
     */
    @Test
    public void findByIdTest4() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);
        byte[] responseString = mallClient.get().uri("/aftersales/{id}",58)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家修改售后单信息
     */
    @Test
    public void modifySaleTest1() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,100);
        String requireJson="{\n" +
                "  \"quantity\": 1,\n" +
                "  \"reason\": \"修改理由\",\n" +
                "  \"regionId\": 0,\n" +
                "  \"detail\": \"修改地址\",\n" +
                "  \"consignee\": \"修改联系人\",\n" +
                "  \"mobile\": \"12345678900\"\n" +
                "}";
        byte[] responseString = mallClient.put().uri("aftersales/{id}",52)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();
        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",52).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        String ep2="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"reason\": \"修改理由\",\n" +
                "        \"quantity\": 1,\n" +
                "        \"regionId\": 0,\n" +
                "        \"detail\": \"修改地址\",\n" +
                "        \"consignee\": \"修改联系人\",\n" +
                "        \"mobile\": \"12345678900\",\n" +
                "        \"state\": 0\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(ep2,new String(queryResponseString, "UTF-8"), false);
    }

    /**
     * 买家修改售后单信息
     * 在买家已经发货状态修改售后单 失败
     */
    @Test
    public void modifySaleTest2() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);
        String requireJson="{\n" +
                "  \"quantity\": 1,\n" +
                "  \"reason\": \"修改理由\",\n" +
                "  \"regionId\": 0,\n" +
                "  \"detail\": \"修改地址\",\n" +
                "  \"consignee\": \"修改联系人\",\n" +
                "  \"mobile\": \"12345678900\"\n" +
                "}";
        byte[] responseString = mallClient.put().uri("aftersales/{id}",56)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 609\n" +
//                "    \"errmsg\": \"售后单状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 买家修改售后单信息
     * 修改不是自己的售后单
     */
    @Test
    public void modifySaleTest3() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);
        String requireJson="{\n" +
                "  \"quantity\": 1,\n" +
                "  \"reason\": \"修改理由\",\n" +
                "  \"regionId\": 0,\n" +
                "  \"detail\": \"修改地址\",\n" +
                "  \"consignee\": \"修改联系人\",\n" +
                "  \"mobile\": \"12345678900\"\n" +
                "}";
        byte[] responseString = mallClient.put().uri("aftersales/{id}",54)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 505\n" +
//                "    \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 买家修改售后单信息
     * 售后单不存在
     */
    @Test
    public void modifySaleTest4() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);
        String requireJson="{\n" +
                "  \"quantity\": 1,\n" +
                "  \"reason\": \"修改理由\",\n" +
                "  \"regionId\": 0,\n" +
                "  \"detail\": \"修改地址\",\n" +
                "  \"consignee\": \"修改联系人\",\n" +
                "  \"mobile\": \"12345678900\"\n" +
                "}";
        byte[] responseString = mallClient.put().uri("aftersales/{id}",54321)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);

    }

    /**
     * 买家取消未完成的售后单
     */
    @Test
    public void cancelSaleTest1() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.delete().uri("aftersales/{id}",53)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",53).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"id\": 53,\n" +
                "        \"orderId\": null,\n" +
                "        \"orderItemId\": 39,\n" +
                "        \"skuId\": 341,\n" +
                "        \"skuName\": null,\n" +
                "        \"customerId\": 1,\n" +
                "        \"shopId\":1,\n" +
                "        \"serviceSn\": null,\n" +
                "        \"type\": 0,\n" +
                "        \"reason\": \"七天无理由\",\n" +
                "        \"refund\": null,\n" +
                "        \"quantity\": 1,\n" +
                "        \"regionId\": 1,\n" +
                "        \"detail\": \"厦大学生公寓\",\n" +
                "        \"consignee\": \"Chen\",\n" +
                "        \"mobile\": \"12345678900\",\n" +
                "        \"customerLogSn\": null,\n" +
                "        \"shopLogSn\": null,\n" +
                "        \"state\": 7\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家删除已经完成的售后单
     */
    @Test
    public void cancelSaleTest2() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.delete().uri("aftersales/{id}",64)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",64).header("authorization",token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家取消已经删除的售后单 失败
     */
    @Test
    public void cancelSaleTest3() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.delete().uri("aftersales/{id}",58)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);

    }
    /**
     * 买家取消不是自己的售后单
     */
    @Test
    public void cancelSaleTest4() throws Exception {

        String token=this.Userlogin("36040122840","123456");
        //String token=new JwtHelper().createToken(2L,1L,1);

        byte[] responseString = mallClient.delete().uri("aftersales/{id}",57)
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 505\n" +
//                "    \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 售后单Id不存在
     */
    @Test
    public void cancelSaleTest5() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.delete().uri("aftersales/{id}",65432)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家填写售后的运单信息 （同时需要修改售后单状态）
     */
    @Test
    public void customerLogSnTest1() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,100);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",55)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);


        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",55).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        String ep2="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"customerLogSn\": \"456456\",\n" +
                "        \"state\": 2\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(ep2,new String(queryResponseString, "UTF-8"), false);

    }

    /**
     * 买家填写售后的运单信息 不是自己的售后单
     */
    @Test
    public void customerLogSnTest2() throws Exception {

        String token=this.Userlogin("36040122840","123456");
        //String token=new JwtHelper().createToken(2L,1L,1);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",51)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 505\n" +
//                "    \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 买家填写售后的运单信息 售后单Id不存在
     */
    @Test
    public void customerLogSnTest3() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",65432)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);

    }


    /**
     * 买家填写售后的运单信息 运单信息为空
     */
    @Test
    public void customerLogSnTest4() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        String requireJson="{}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",51)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                //.jsonPath("$.errmsg").isEqualTo("运单信息不能为空;")
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 503\n" +
//                "    \"errmsg\": \"运单信息不能为空;\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);

    }

    /**
     * 买家填写售后的运单信息 此时状态为不支持填写
     */
    @Test
    public void customerLogSnTest5() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",62)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 609\n" +
//                "    \"errmsg\": \"售后单状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }
    /**
     * 买家填写售后的运单信息 当前售后单已经取消
     */
    @Test
    public void customerLogSnTest6() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,1);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",53)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 609\n" +
//                "    \"errmsg\": \"售后单状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

    /**
     * 买家填写售后的运单信息 当前售后单已经删除
     */
    @Test
    public void customerLogSnTest7() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,1);

        String requireJson="{\n" +
                "    \"logSn\":\"456456\"\n" +
                "}";

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/sendback",58)
                .header("authorization", token)
                .bodyValue(requireJson)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家确认售后结束 此时状态为店家已发货
     */
    @Test
    public void confirmOverTest1() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",61)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",61).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        String ep2="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"state\": 8\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(ep2,new String(queryResponseString, "UTF-8"), false);


    }

    /**
     * 买家确认售后结束 此时状态为待店家退款
     */
    @Test
    public void confirmOverTest2() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",57)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), true);

        byte[] queryResponseString = mallClient.get().uri("/aftersales/{id}",57).header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo(ResponseCode.OK.getMessage())
                .jsonPath("$.data").exists()
                .returnResult()
                .getResponseBodyContent();

        String ep2="{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"state\": 8\n" +
                "    },\n" +
                "    \"errmsg\": \"成功\"\n" +
                "}";

        JSONAssert.assertEquals(ep2,new String(queryResponseString, "UTF-8"), false);
    }


    /**
     * 买家确认售后结束 售后Id不存在
     */
    @Test
    public void confirmOverTest3() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",65432)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家确认售后结束 售后单不是自己的对象
     */
    @Test
    public void confirmOverTest4() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",67)
                .header("authorization", token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 505\n" +
//                "    \"errmsg\": \"操作的资源id不是自己的对象\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }


    /**
     * 买家确认售后结束 售后未完成 不能确认结束
     */
    @Test
    public void confirmOverTest5() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString =mallClient.put().uri("/aftersales/{id}/confirm",66)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 609\n" +
//                "    \"errmsg\": \"售后单状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);
    }

    /**
     * 买家确认售后结束 售后单已经删除
     */
    @Test
    public void confirmOverTest6() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        // String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",58)
                .header("authorization", token)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expected="{\n" +
                "    \"errno\": 504\n" +
//                "    \"errmsg\": \"操作的资源id不存在\"\n" +
                "}";
        JSONAssert.assertEquals(expected, new String(responseString, "UTF-8"), false);
    }

    /**
     * 买家确认售后结束 售后单已经取消
     */
    @Test
    public void confirmOverTest7() throws Exception {

        String token=this.Userlogin("8606245097","123456");
        //String token=new JwtHelper().createToken(1L,1L,1);

        byte[] responseString = mallClient.put().uri("/aftersales/{id}/confirm",63)
                .header("authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getCode())
//                .jsonPath("$.errmsg").isEqualTo(ResponseCode.AFTERSALE_STATENOTALLOW.getMessage())
                .returnResult()
                .getResponseBodyContent();

        String expectedResponse = "{\n" +
                "    \"errno\": 609\n" +
//                "    \"errmsg\": \"售后单状态禁止\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, new String(responseString, StandardCharsets.UTF_8), false);

    }

}

