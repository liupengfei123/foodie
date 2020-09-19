package com.lpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * spring boot 启动类
 *
 * @author liupf
 * @date 2020-09-19 9:16
 */

@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.lpf.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.lpf"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
