package com.meeting.entity; // 实体类包

import com.baomidou.mybatisplus.annotation.*; // MyBatisPlus注解
import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议室占用记录实体类
 * 映射数据库 meeting_room_usage 表，记录会议室的时间占用情况
 */
@Data // Lombok: 自动生成getter/setter
@TableName("meeting_room_usage") // 映射 meeting_room_usage 表
public class MeetingRoomUsage {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id; // 主键ID

    private Long meetingId; // 关联的会议ID

    private Long meetingRoomId; // 关联的会议室ID

    private LocalDateTime startTime; // 占用开始时间

    private LocalDateTime endTime; // 占用结束时间

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createdAt; // 记录创建时间

    @TableLogic // 逻辑删除
    private Integer deleted; // 逻辑删除标记
}