package com.meeting.dto; // 数据传输对象包

import lombok.Data; // Lombok注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议查询请求DTO
 * 封装会议列表查询时的多条件筛选参数和分页信息
 */
@Data // Lombok: 自动生成getter/setter
public class MeetingQueryDTO {
    private String meetingCode;     // 会议编号筛选条件
    private String title;            // 会议主题模糊搜索
    private String department;       // 部门筛选
    private Integer status;          // 状态筛选
    private LocalDateTime startTimeFrom; // 开始时间范围起始
    private LocalDateTime startTimeTo;   // 开始时间范围结束
    private Integer pageNum = 1;     // 当前页码，默认第1页
    private Integer pageSize = 10;   // 每页记录数，默认10条
}