package com.meeting.config; // 配置类包

import com.baomidou.mybatisplus.annotation.DbType; // 数据库类型枚举
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor; // MyBatisPlus拦截器
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor; // 分页内部拦截器
import org.springframework.context.annotation.Bean; // Spring Bean注解
import org.springframework.context.annotation.Configuration; // Spring配置类注解

/**
 * MyBatis Plus 配置类
 * 注册分页插件等核心扩展功能
 */
@Configuration // 标识为Spring配置类，应用启动时自动加载
public class MyBatisPlusConfig {

    /**
     * 注册 MyBatis Plus 拦截器
     * 主要配置分页插件，支持自动分页查询
     */
    @Bean // 将方法返回值注册为Spring Bean，由容器管理生命周期
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor(); // 创建拦截器实例
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 添加MySQL分页插件
        return interceptor; // 返回配置好的拦截器
    }
}