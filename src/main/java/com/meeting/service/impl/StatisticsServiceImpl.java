package com.meeting.service.impl; // 业务实现类包

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // MyBatisPlus Lambda条件构造器
import com.meeting.common.Constants; // 系统常量
import com.meeting.dto.RoomUsageQueryDTO; // 会议室使用统计DTO
import com.meeting.entity.Meeting; // 会议实体
import com.meeting.entity.MeetingRoomUsage; // 会议室占用实体
import com.meeting.mapper.MeetingMapper; // 会议数据访问
import com.meeting.mapper.MeetingRoomMapper; // 会议室数据访问
import com.meeting.mapper.MeetingRoomUsageMapper; // 会议室占用数据访问
import com.meeting.mapper.ParticipantMapper; // 参会人员数据访问
import com.meeting.service.IStatisticsService; // 统计业务接口
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.stereotype.Service; // Spring Service注解
import java.time.Duration; // 时间段计算
import java.time.LocalDateTime; // 时间类型
import java.util.HashMap; // HashMap集合
import java.util.List; // List集合
import java.util.Map; // Map集合

/**
 * 统计业务实现类
 * 实现会议统计、会议室使用统计、参会率统计三个核心统计功能
 */
@Service // 注册为Spring Bean
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private MeetingMapper meetingMapper; // 会议数据访问

    @Autowired
    private MeetingRoomMapper meetingRoomMapper; // 会议室数据访问

    @Autowired
    private MeetingRoomUsageMapper meetingRoomUsageMapper; // 会议室占用数据访问

    @Autowired
    private ParticipantMapper participantMapper; // 参会人员数据访问

    /**
     * 会议统计
     * 按部门和状态统计会议数量分布（总数/待召开/进行中/已结束）
     */
    @Override
    public Map<String, Object> getMeetingStatistics(String department, Integer status) {
        LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>(); // 创建条件构造器
        if (department != null && !department.isEmpty()) {
            queryWrapper.eq(Meeting::getDepartment, department); // 部门等于指定值
        }
        if (status != null) {
            queryWrapper.eq(Meeting::getStatus, status); // 状态等于指定值
        }

        List<Meeting> meetings = meetingMapper.selectList(queryWrapper); // 查询符合条件的会议

        Map<String, Object> result = new HashMap<>(); // 创建结果Map
        result.put("totalCount", meetings.size()); // 会议总数

        // 使用Stream API按状态分别统计
        long pendingCount = meetings.stream().filter(m -> m.getStatus() == Constants.MeetingStatus.PENDING).count(); // 待召开数量
        long inProgressCount = meetings.stream().filter(m -> m.getStatus() == Constants.MeetingStatus.IN_PROGRESS).count(); // 进行中数量
        long endedCount = meetings.stream().filter(m -> m.getStatus() == Constants.MeetingStatus.ENDED).count(); // 已结束数量

        result.put("pendingCount", pendingCount);         // 待召开
        result.put("inProgressCount", inProgressCount);   // 进行中
        result.put("endedCount", endedCount);             // 已结束

        return result; // 返回统计结果
    }

    /**
     * 会议室使用统计
     * 统计会议室的总使用次数、总时长、日均使用量的分布
     */
    @Override
    public Map<String, Object> getRoomUsageStatistics(RoomUsageQueryDTO queryDTO) {
        LocalDateTime startTime = queryDTO.getStartDate(); // 获取开始日期
        LocalDateTime endTime = queryDTO.getEndDate();     // 获取结束日期

        LambdaQueryWrapper<MeetingRoomUsage> queryWrapper = new LambdaQueryWrapper<>(); // 创建条件构造器
        if (queryDTO.getMeetingRoomId() != null) {
            queryWrapper.eq(MeetingRoomUsage::getMeetingRoomId, queryDTO.getMeetingRoomId()); // 按会议室筛选
        }
        if (startTime != null) {
            queryWrapper.ge(MeetingRoomUsage::getStartTime, startTime); // 开始时间 >= 指定值
        }
        if (endTime != null) {
            queryWrapper.le(MeetingRoomUsage::getEndTime, endTime); // 结束时间 <= 指定值
        }

        List<MeetingRoomUsage> usages = meetingRoomUsageMapper.selectList(queryWrapper); // 查询占用记录

        Map<String, Object> result = new HashMap<>(); // 创建结果Map
        result.put("totalUsageCount", usages.size()); // 总使用次数

        // 计算总使用时长（分钟）
        long totalMinutes = usages.stream()
                .mapToLong(u -> Duration.between(u.getStartTime(), u.getEndTime()).toMinutes()) // 每次占用时长转分钟
                .sum(); // 求和
        result.put("totalUsageMinutes", totalMinutes); // 总时长（分钟）
        result.put("averageUsageMinutes", usages.isEmpty() ? 0 : totalMinutes / usages.size()); // 平均时长

        // 按天统计使用次数
        Map<String, Long> dailyCount = new HashMap<>(); // 日期 -> 次数
        for (MeetingRoomUsage usage : usages) {
            String dateKey = usage.getStartTime().toLocalDate().toString(); // 取日期部分作为键
            dailyCount.put(dateKey, dailyCount.getOrDefault(dateKey, 0L) + 1); // 累加次数
        }
        result.put("dailyCount", dailyCount); // 每日使用次数分布

        return result; // 返回统计结果
    }

    /**
     * 参会率统计
     * 计算指定会议的注册人数、实际参会人数和参会率
     */
    @Override
    public Map<String, Object> getAttendanceRateStatistics(Long meetingId) {
        Meeting meeting = meetingMapper.selectById(meetingId); // 查询会议信息
        if (meeting == null) {
            return null; // 会议不存在返回null
        }

        // 分别统计已报名和已参会的人数
        int registeredCount = participantMapper.countByMeetingIdAndStatus(meetingId, Constants.ParticipantStatus.REGISTERED); // 已报名人数
        int attendedCount = participantMapper.countByMeetingIdAndStatus(meetingId, Constants.ParticipantStatus.ATTENDED); // 已参会人数

        Map<String, Object> result = new HashMap<>(); // 创建结果Map
        result.put("meetingId", meetingId);               // 会议ID
        result.put("meetingTitle", meeting.getTitle());   // 会议标题
        result.put("maxParticipants", meeting.getMaxParticipants()); // 人数上限
        result.put("registeredCount", registeredCount);   // 已报名人数
        result.put("attendedCount", attendedCount);       // 已参会人数
        result.put("attendanceRate", registeredCount == 0 ? 0 : (attendedCount * 100.0 / registeredCount)); // 参会率%

        return result; // 返回统计结果
    }
}