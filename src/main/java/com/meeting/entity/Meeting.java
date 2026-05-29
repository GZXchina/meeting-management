package com.meeting.entity; // 实体类包，与数据库表一一对应

import com.baomidou.mybatisplus.annotation.*; // MyBatisPlus注解：主键、自动填充、逻辑删除等
import com.fasterxml.jackson.annotation.JsonFormat; // Jackson日期格式化注解
import lombok.Data; // Lombok: 自动生成getter/setter/equals/hashCode/toString
import java.time.LocalDateTime; // Java8时间API

/**
 * 会议实体类
 * 映射数据库 meeting 表，存储会议的基本信息
 */
@Data // Lombok: 自动生成所有字段的getter/setter
@TableName("meeting") // MyBatisPlus: 指定映射的数据库表名
public class Meeting {

    @TableId(type = IdType.AUTO) // 主键自增策略
    private Long id; // 主键ID，自动递增

    private String meetingCode; // 会议编号，业务唯一标识（如MT20260001）

    private String title; // 会议主题/标题

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // JSON序列化/反序列化日期格式
    private LocalDateTime startTime; // 会议开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // JSON序列化/反序列化日期格式
    private LocalDateTime endTime; // 会议结束时间

    private String location; // 会议地点

    private String department; // 主办部门

    private Integer maxParticipants; // 参会人数上限，默认100

    private Integer status; // 会议状态：0-待召开，1-进行中，2-已结束

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充当前时间
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    private LocalDateTime updatedAt; // 更新时间

    @TableLogic // 逻辑删除标记：0-未删除，1-已删除
    private Integer deleted; // 逻辑删除标记
}