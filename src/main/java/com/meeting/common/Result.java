package com.meeting.common; // 通用工具类包

import lombok.Data; // Lombok注解，自动生成getter/setter/toString等

/**
 * 统一API响应结果封装类
 * 所有Controller接口返回此对象，确保前端接收格式统一
 *
 * @param <T> 响应数据的泛型类型
 */
@Data // Lombok: 自动生成getter/setter/equals/hashCode/toString
public class Result<T> {
    private Integer code;    // 状态码：200-成功，500-服务器错误
    private String message;   // 响应消息
    private T data;          // 响应数据，泛型类型

    /**
     * 返回成功结果（无数据）
     */
    public static <T> Result<T> success() {
        return success(null); // 委托调用带数据的success方法
    }

    /**
     * 返回成功结果（带数据）
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(); // 创建Result实例
        result.setCode(200);               // 设置成功状态码
        result.setMessage("success");       // 设置成功消息
        result.setData(data);              // 设置响应数据
        return result;                     // 返回结果
    }

    /**
     * 返回错误结果（默认500状态码）
     *
     * @param message 错误消息
     */
    public static <T> Result<T> error(String message) {
        return error(500, message); // 委托调用带状态码的error方法
    }

    /**
     * 返回错误结果（自定义状态码）
     *
     * @param code    状态码
     * @param message 错误消息
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>(); // 创建Result实例
        result.setCode(code);             // 设置错误状态码
        result.setMessage(message);        // 设置错误消息
        return result;                     // 返回结果（data为null）
    }
}