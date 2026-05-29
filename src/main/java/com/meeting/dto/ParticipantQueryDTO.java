package com.meeting.dto; // 数据传输对象包

import lombok.Data; // Lombok注解

/**
 * 参会人员查询请求DTO
 * 封装参会人员列表查询时的多条件筛选参数和分页信息
 */
@Data // Lombok: 自动生成getter/setter
public class ParticipantQueryDTO {
    private String userId;       // 用户ID筛选
    private String name;         // 姓名模糊搜索
    private String department;   // 部门筛选
    private Long meetingId;      // 关联会议ID筛选
    private Integer status;      // 参会状态筛选
    private Integer pageNum = 1; // 当前页码，默认第1页
    private Integer pageSize = 10; // 每页记录数，默认10条
}