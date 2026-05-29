package com.meeting.dto; // 数据传输对象包，用于接收前端请求参数

import com.fasterxml.jackson.annotation.JsonFormat; // Jackson日期格式化注解
import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 时间冲突检测请求DTO
 * 接收前端提交的会议室时段检测参数
 */
@Data // Lombok: 自动生成getter/setter
public class ConflictCheckDTO {
    private Long meetingRoomId; // 要检测的会议室ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 反序列化日期格式
    private LocalDateTime startTime; // 检测的开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 反序列化日期格式
    private LocalDateTime endTime; // 检测的结束时间

    private Long excludeMeetingId; // 排除的会议ID（更新会议时排除自身）
}