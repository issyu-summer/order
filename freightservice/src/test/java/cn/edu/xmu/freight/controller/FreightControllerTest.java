package cn.edu.xmu.freight.controller;

import cn.edu.xmu.freight.FreightServiceApplication;
import cn.edu.xmu.freight.mapper.FreightModelPoMapper;
import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.po.FreightModelPoExample;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.TimeFormat;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 15:40
 */
@SpringBootTest(classes = FreightServiceApplication.class)
public class FreightControllerTest {

        private WebTestClient webTestClient;

        @Autowired
        private FreightModelPoMapper freightModelPoMapper;

        public FreightControllerTest(){
            //绑定****正在运行的服务器****
            this.webTestClient = WebTestClient.bindToServer()
                    .baseUrl("http://localhost:8083")
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
//        /**
//         * 插入一条运费模板记录
//         * @author issyu 30320182200070
//         * @date 2020/12/3 11:00
//         */
//        private final int insertFreightModelPo(
//                Long shopId,String name,Byte defaultModel,Byte type,Integer unit,String gmtCreate,String gmtModified
//                ) {
//            FreightModelPo freightModelPo = new FreightModelPo();
//            freightModelPo.setShopId(shopId);
//            freightModelPo.setGmtCreate(TimeFormat.stringToDateTime(gmtCreate));
//            freightModelPo.setDefaultModel(defaultModel);
//            freightModelPo.setType(type);
//            freightModelPo.setUnit(unit);
//            freightModelPo.setName(name);
//            freightModelPo.setGmtModified(TimeFormat.stringToDateTime(gmtModified));
//            int ret = freightModelPoMapper.insertSelective(freightModelPo);
//            return ret;
//        }
//
//        @Test
//        public void getFreightModelsInShop(){
//
//
//            FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
//            FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();
//            List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample);
//            //如果数据库为空，则执行这条语句。
//            if(freightModelPos.isEmpty()){
//                int ret=this.insertFreightModelPo(1L,"test", (byte) 0,
//                        (byte) 0,0,"2020-12-01 17:33:10","2020-12-01 17:33:10");
//            }
//            //System.out.println(ret);
//            //???部门Id对应（）???
//            String token = createTestToken(1L,0L,100);
//
//            String expectedResponse =
//                    "{\"errno\":0,\"errmsg\":\"成功\",\"data\":{\"page\":1,\"pageSize\":1,\"total\":1,\"pages\":1,\"list\":[{\"id\":1,\"name\":\"test\",\"type\":0,\"default\":true,\"gmtCreate\":\"2020-12-01T17:33:10\",\"gmtModified\":\"2020-12-01T17:33:10\"}]}}";
//
//            try{
//                byte [] responseString = webTestClient.get().uri("/freight/shops/1/freightmodels")
//                        .header("authorization",token)
//                        .exchange()
//                        .expectStatus().isOk()
//                        .expectHeader().contentType("application/json;charset=UTF-8")
//                        .expectBody()
//                        .returnResult().getResponseBodyContent();
//                String responseStr = new String(responseString,"UTF-8");
//                System.out.println(responseStr);
//                JSONAssert.assertEquals(expectedResponse,responseStr,true);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    /**
//     *
//     * 设置默认运费模板
//     * @author 王薪蕾
//     * @date 2020/12/9
//     **/
//
//    //运费模板不存在  路径错误 -1
//    //管理员无权限  无权限 -2
//    //运费模板不是本店铺  无权限 -3
//    //成功 -4
//    @Test
//    public void postFreightModelsToShop1(){
//        String token = createTestToken(1L,0L,100);
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/10/default")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().is4xxClientError()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postFreightModelsToShop2(){
//        String token = createTestToken(1L,1L,100);
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/1/default")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().is4xxClientError()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postFreightModelsToShop3(){
//        String token = createTestToken(1L,0L,100);
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/10/freightmodels/1/default")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().is4xxClientError()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postFreightModelsToShop4(){
//        String token = createTestToken(1L,0L,100);
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/1/default")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
    /**
     *
     * 增加重量运费模板明细
     * @author 王薪蕾
     * @date 2020/12/9
     **/
    //运费模板不存在  路径错误 已测
    //运费模板不是重量运费模板 路径错误 -1
    //管理员无权限  无权限 已测
    //运费模板不是本店铺  无权限 已测
    //明细的参数错误  参数错误 -2
    //地区码已定义  地区码重复 -3
    //-4
//    @Test
//    public void postWeightItems1(){
//        String token = createTestToken(1L,0L,100);
//
//        String adressJson="{\"firstWeight\":0,\"firstWeightFreight\":0,\"tenPrice\":0,\"fiftyPrice\":0,\"hundredPrice\":0,\"trihunPrice\":0,\"abovePrice\":0,\"regionId\":0}";
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/2/weightItems")
//                    .header("authorization",token)
//                    .bodyValue(adressJson)
//                    .exchange()
//                    .expectStatus().is4xxClientError()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postWeightItems2(){
//        String token = createTestToken(1L,0L,100);
//
//        String adressJson="{\"firstWtWeightFreight\":0,\"tenPrice\":0,\"fiftyPrice\":0,\"hundredPrice\":0,\"trihunPrice\":0,\"abovePrice\":0,\"regionId\":0}";
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/1/weightItems")
//                    .header("authorization",token)
//                    .bodyValue(adressJson)
//                    .exchange()
//                    .expectStatus().is4xxClientError()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postWeightItems3(){
//        String token = createTestToken(1L,0L,100);
//
//        String adressJson="{\"firstWeight\":0,\"firstWeightFreight\":0,\"tenPrice\":0,\"fiftyPrice\":0,\"hundredPrice\":0,\"trihunPrice\":0,\"abovePrice\":0,\"regionId\":0}";
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/1/weightItems")
//                    .header("authorization",token)
//                    .bodyValue(adressJson)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    @Test
//    public void postWeightItems4(){
//        String token = createTestToken(1L,0L,100);
//
//        String adressJson="{\"firstWeight\":0,\"firstWeightFreight\":0,\"tenPrice\":0,\"fiftyPrice\":0,\"hundredPrice\":0,\"trihunPrice\":0,\"abovePrice\":0,\"regionId\":2}";
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freightmodels/1/weightItems")
//                    .header("authorization",token)
//                    .bodyValue(adressJson)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    /**
     *
     * 删除运费模板
     * @author 王薪蕾
     * @date 2020/12/9
     **/
    //运费模板不存在  路径错误 已测
    //管理员无权限  无权限 已测
    //运费模板不是本店铺  无权限 已测
    //
    @Test
    public void deleteFreightModel(){
        String token = createTestToken(1L,0L,100);
        try{
            byte [] responseString = webTestClient.delete().uri("/freight/shops/1/freightmodels/1")
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
//    /*
//     * @author 史韬韬
//     * @date 202012/8
//     * 获取运费模板概要
//     */
//    @Test
//    public void getFreightModelSimpleInfo(){
//        String token = createTestToken(1L,0L,100);
//        String expectedResponse;
//        try{
//            byte [] responseString = webTestClient.get().uri("/freight/freightmodels/{id}",2,1)
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    /*
//     * @author 史韬韬
//     * @Date 2020/12/9
//     * 管理员修改运费模板
//     */
//    @Test
//    public void changeFreightModel(){
//        String token = createTestToken(1L,0L,100);
//        String infoString="{\"name\":\"test\",\"unit\":500}";
//        try{
//            byte [] responseString = webTestClient.put().uri("/freight/shops/{shopId}/freightmodels/{id}",2,1)
//                    .header("authorization",token)
//                    .bodyValue(infoString)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    /*
//     * @author 史韬韬
//     * @Date 2020/12/9
//     * 管理员克隆运费模板（此测试未通过，应该是数据库查询的问题，我之后再修改一下）
//     */
//    @Test
//    public void cloneFreightModel(){
//        String token = createTestToken(1L,0L,100);
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/{shopId}/freightmodels/{id}/clone",2,1)
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//            //JSONAssert.assertEquals(expectedResponse,responseStr,true);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 店家或管理员查询运费模板明细
//     * @author 陈星如
//     * @date 2020/12/8 13:33
//     */
//    @Test
//    public void getFreightModelsWeightItems(){
//        String token = createTestToken(1L,0L,100);
//             try{
//            byte [] responseString = webTestClient.get().uri("/freight/shops/9/freightmodels/4/weightItems")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 管理员定义件数模板明细
//     * @author 陈星如
//     * @date 2020/12/9 9:13
//     */
//    @Test
//    public void postPieceItems(){
//        String token = createTestToken(1L,0L,100);
//
//        String adressJson="{\"regionId\":0,\"firstItem\":0,\"firstItemPrice\":0,\"additionalItems\":0,\"additionalItemsPrice\":0}";
//        try{
//            byte [] responseString = webTestClient.post().uri("/freight/shops/9/freightmodels/4/pieceItems")
//                    .header("authorization",token)
//                    .bodyValue(adressJson)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    /*
//     * 店家或管理员查询件数运费模板的明细
//     * @author 陈星如
//     * @date 2020/12/8 14:13
//     */
//    @Test
//    public void getFreightModelsPieceItems(){
//        String token = createTestToken(1L,0L,100);
//    try{
//            byte [] responseString = webTestClient.get().uri("/freight/shops/9/freightmodels/1/pieceItems")
//                    .header("authorization",token)
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectHeader().contentType("application/json;charset=UTF-8")
//                    .expectBody()
//                    .returnResult().getResponseBodyContent();
//            String responseStr = new String(responseString,"UTF-8");
//            System.out.println(responseStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
