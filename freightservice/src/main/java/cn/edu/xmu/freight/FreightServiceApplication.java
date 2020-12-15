package cn.edu.xmu.freight;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:23
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.freight"})
@MapperScan("cn.edu.xmu.freight.mapper")
public class FreightServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreightServiceApplication.class, args);
    }
}