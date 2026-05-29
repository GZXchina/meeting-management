package com.meeting.dto; // 数据传输对象包

import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议室使用统计查询DTO
 * 封装会议室使用情况统计的查询参数
 */
@Data // Lombok: 自动生成getter/setter
public class RoomUsageQueryDTO {
    private Long meetingRoomId;      // 会议室ID（可选筛选）
    private LocalDateTime startDate;  // 统计开始日期
    private LocalDateTime endDate;    // 统计结束日期
    private String type;             // 统计类型（如按天/按周等）
}