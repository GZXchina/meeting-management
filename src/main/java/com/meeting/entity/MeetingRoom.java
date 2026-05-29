package com.meeting.entity; // 实体类包

import com.baomidou.mybatisplus.annotation.*; // MyBatisPlus注解
import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议室实体类
 * 映射数据库 meeting_room 表，存储会议室资源和设备信息
 */
@Data // Lombok: 自动生成字段的getter/setter
@TableName("meeting_room") // 映射 meeting_room 表
public class MeetingRoom {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id; // 主键ID

    private String roomCode; // 会议室编号（如MR001）

    private String roomName; // 会议室名称（如第一会议室）

    private Integer capacity; // 容量（可容纳人数）

    private String equipment; // 设备清单，JSON格式存储（如["投影仪","音响"]）

    private Integer status; // 使用状态：0-空闲，1-占用

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    private LocalDateTime updatedAt; // 更新时间

    @TableLogic // 逻辑删除
    private Integer deleted; // 逻辑删除标记
}