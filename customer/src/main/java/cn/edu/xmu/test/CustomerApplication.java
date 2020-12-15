package cn.edu.xmu.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author issyu 30320182200070
 * @date 2020/12/12 21:14
 */

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
