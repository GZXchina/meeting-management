package com.meeting; // 应用根包

import org.mybatis.spring.annotation.MapperScan; // MyBatis Mapper包扫描
import org.springframework.boot.SpringApplication; // Spring Boot应用启动器
import org.springframework.boot.autoconfigure.SpringBootApplication; // Spring Boot自动配置

/**
 * 会议管理系统 - Spring Boot 启动类
 * 应用入口，负责初始化Spring容器和自动配置
 */
@SpringBootApplication // 复合注解：@Configuration + @EnableAutoConfiguration + @ComponentScan
@MapperScan("com.meeting.mapper") // 自动扫描并注册mapper包下的MyBatis接口
public class MeetingApplication {

    /**
     * 应用主入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args); // 启动Spring Boot应用
    }
}