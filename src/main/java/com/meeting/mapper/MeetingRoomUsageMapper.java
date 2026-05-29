package com.meeting.mapper; // 数据访问层包

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MyBatisPlus通用Mapper
import com.meeting.entity.MeetingRoomUsage; // 会议室占用实体
import org.apache.ibatis.annotations.Mapper; // MyBatis Mapper标记
import org.apache.ibatis.annotations.Param; // 参数名映射
import java.time.LocalDateTime; // 时间类型

/**
 * 会议室占用记录数据访问层
 * 负责时间冲突检测和关联记录清理
 */
@Mapper // 注册为MyBatis Mapper
public interface MeetingRoomUsageMapper extends BaseMapper<MeetingRoomUsage> {

    /**
     * 统计指定时间段内的冲突记录数
     * 用于检测新会议时间段是否与已有占用冲突
     *
     * @param meetingRoomId    要检测的会议室ID
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param excludeMeetingId 排除的会议ID（更新时排除自身）
     * @return 冲突记录数（>0表示有冲突）
     */
    int countConflict(@Param("meetingRoomId") Long meetingRoomId,
                      @Param("startTime") LocalDateTime startTime,
                      @Param("endTime") LocalDateTime endTime,
                      @Param("excludeMeetingId") Long excludeMeetingId);

    /**
     * 按会议ID删除所有关联的占用记录
     * 删除会议时清理该会议的所有会议室占用
     *
     * @param meetingId 会议ID
     * @return 删除的行数
     */
    int deleteByMeetingId(@Param("meetingId") Long meetingId);
}