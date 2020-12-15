package cn.edu.xmu.payment;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author issyu 30320182200070
 * @date 2020/12/2 22:44
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.payment","cn.edu.xmu.inner","cn.edu.xmu.outer"})
@MapperScan("cn.edu.xmu.payment.mapper")
@EnableDubbo(scanBasePackages = {"cn.edu.xmu.payment.service.impl"})
@EnableDiscoveryClient
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
