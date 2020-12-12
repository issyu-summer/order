package cn.edu.xmu.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 21:14
 */

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.external","cn.edu.xmu.test"})
@MapperScan("cn.edu.xmu.payment.mapper")
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
