package com.lpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring boot 启动类
 *
 * @author liupf
 * @date 2020-09-19 9:16
 */

@SpringBootApplication
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"org.n3r.idworker"})
@EnableScheduling       // 开启定时任务
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
