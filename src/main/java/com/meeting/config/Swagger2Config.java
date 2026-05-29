package com.meeting.config; // 配置类包

import org.springframework.context.annotation.Bean; // Spring Bean注解
import org.springframework.context.annotation.Configuration; // Spring配置类注解
import springfox.documentation.builders.ApiInfoBuilder; // Swagger API信息构建器
import springfox.documentation.builders.PathSelectors; // Swagger路径选择器
import springfox.documentation.builders.RequestHandlerSelectors; // Swagger请求处理器选择器
import springfox.documentation.service.ApiInfo; // Swagger API元信息
import springfox.documentation.service.Contact; // Swagger联系人信息
import springfox.documentation.spi.DocumentationType; // Swagger文档类型
import springfox.documentation.spring.web.plugins.Docket; // Swagger核心配置类
import springfox.documentation.swagger2.annotations.EnableSwagger2; // 启用Swagger2注解

/**
 * Swagger2 API文档配置类
 * 启用Swagger自动生成在线接口文档，访问地址: http://localhost:8080/swagger-ui.html
 */
@Configuration // 标识为Spring配置类
@EnableSwagger2 // 启用Swagger2文档功能
public class Swagger2Config {

    /**
     * 创建Swagger Docket Bean
     * 配置接口文档的基本信息和扫描路径
     */
    @Bean // 注册为Spring Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 使用Swagger2文档类型
                .apiInfo(apiInfo())                     // 设置API文档基本信息
                .select()                               // 开始选择接口
                .apis(RequestHandlerSelectors.basePackage("com.meeting.controller")) // 扫描controller包
                .paths(PathSelectors.any())             // 扫描所有路径
                .build();                               // 构建Docket对象
    }

    /**
     * 构建API文档元信息
     * 包含标题、描述、版本、联系人等
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("会议管理系统API文档")                          // 文档标题
                .description("会议、人员、会议室管理及统计分析接口")      // 文档描述
                .version("1.0.0")                                     // 版本号
                .contact(new Contact("会议管理系统", "", ""))           // 联系人信息
                .build();                                             // 构建ApiInfo
    }
}