package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.user.UserServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = UserServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 获得所有的用户状态
     * @throws Exception
     */
    @Test
    public void getAllUserState1()throws Exception{
        String token= getToken(1L);
    }



    /**
     * 创建测试用token
     */
    private String getToken(Long userId) {
        String token = new JwtHelper().createToken(userId, -2L, 99999);
        log.info(token);
        return token;
    }
}
