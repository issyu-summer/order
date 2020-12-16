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
public class GenerateAuthorizationToken {

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
     * 创建测试用token
     * @author 王子扬 30320182200071
     * @date 2020/12/9 10:58
     */
    @Test
    public void getTestToken(){
        String token = createTestToken(1L,1L,10000000);
        System.out.println(token);
        String token1 = createTestToken(2L,0L,10000000);
        System.out.println(token1);
        String token2 = createTestToken(3L,-2L,10000000);
        System.out.println(token2);
        String token3 = createTestToken(2356L,-2L,10000000);
        System.out.println(token3);
    }
}
