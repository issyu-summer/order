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
         * 插入一条运费模板记录
         * @author issyu 30320182200070
         * @date 2020/12/3 11:00
         */
        private final int insertFreightModelPo(
                Long shopId,String name,Byte defaultModel,Byte type,Integer unit,String gmtCreate,String gmtModified
                ) {
            FreightModelPo freightModelPo = new FreightModelPo();
            freightModelPo.setShopId(shopId);
            freightModelPo.setGmtCreate(TimeFormat.stringToDateTime(gmtCreate));
            freightModelPo.setDefaultModel(defaultModel);
            freightModelPo.setType(type);
            freightModelPo.setUnit(unit);
            freightModelPo.setName(name);
            freightModelPo.setGmtModified(TimeFormat.stringToDateTime(gmtModified));
            int ret = freightModelPoMapper.insertSelective(freightModelPo);
            return ret;
        }

        @Test
        public void getFreightModelsInShop(){


            FreightModelPoExample freightModelPoExample = new FreightModelPoExample();
            FreightModelPoExample.Criteria criteria = freightModelPoExample.createCriteria();
            List<FreightModelPo> freightModelPos = freightModelPoMapper.selectByExample(freightModelPoExample);
            //如果数据库为空，则执行这条语句。
            if(freightModelPos.isEmpty()){
                int ret=this.insertFreightModelPo(1L,"test", (byte) 0,
                        (byte) 0,0,"2020-12-01 17:33:10","2020-12-01 17:33:10");
            }
            //System.out.println(ret);
            //???部门Id对应（）???
            String token = createTestToken(1L,0L,100);

            String expectedResponse =
                    "{\"errno\":0,\"errmsg\":\"成功\",\"data\":{\"page\":1,\"pageSize\":1,\"total\":1,\"pages\":1,\"list\":[{\"id\":1,\"name\":\"test\",\"type\":0,\"default\":true,\"gmtCreate\":\"2020-12-01T17:33:10\",\"gmtModified\":\"2020-12-01T17:33:10\"}]}}";

            try{
                byte [] responseString = webTestClient.get().uri("/freight/shops/1/freightmodels")
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
    @Test
    public void postFreightModelsToShop(){
        String token = createTestToken(1L,0L,100);
        try{
            byte [] responseString = webTestClient.post().uri("/freight/shops/2/freight_models/1/default")
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
