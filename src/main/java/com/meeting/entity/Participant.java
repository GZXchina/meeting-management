package com.meeting.entity; // 实体类包

import com.baomidou.mybatisplus.annotation.*; // MyBatisPlus注解
import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 参会人员实体类
 * 映射数据库 participant 表，记录会议与人员的关联关系
 */
@Data // Lombok: 自动生成getter/setter
@TableName("participant") // 映射 participant 表
public class Participant {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id; // 主键ID

    private String userId; // 用户ID，业务系统的用户唯一标识

    private String name; // 参会人员姓名

    private String department; // 所属部门

    private String phone; // 联系电话

    private Long meetingId; // 关联的会议ID（外键）

    private Integer status; // 参会状态：0-已报名，1-未签到，2-已参会

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    private LocalDateTime updatedAt; // 更新时间

    @TableLogic // 逻辑删除
    private Integer deleted; // 逻辑删除标记
}