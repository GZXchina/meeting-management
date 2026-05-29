package com.meeting.service; // 业务逻辑层接口包

import com.meeting.dto.RoomUsageQueryDTO; // 会议室使用统计查询DTO
import java.util.Map; // Java集合Map

/**
 * 统计业务接口
 * 定义会议统计、会议室使用统计、参会率统计的业务操作契约
 */
public interface IStatisticsService {

    /**
     * 会议统计（按部门和状态统计会议数量分布）
     *
     * @param department 部门筛选（可选）
     * @param status     状态筛选（可选）
     * @return 包含totalCount/pendingCount/inProgressCount/endedCount的Map
     */
    Map<String, Object> getMeetingStatistics(String department, Integer status);

    /**
     * 会议室使用统计
     *
     * @param queryDTO 查询条件（会议室ID、时间范围）
     * @return 包含totalUsageCount/totalUsageMinutes/dailyCount的Map
     */
    Map<String, Object> getRoomUsageStatistics(RoomUsageQueryDTO queryDTO);

    /**
     * 参会率统计
     *
     * @param meetingId 会议ID
     * @return 包含registeredCount/attendedCount/attendanceRate的Map，会议不存在返回null
     */
    Map<String, Object> getAttendanceRateStatistics(Long meetingId);
}