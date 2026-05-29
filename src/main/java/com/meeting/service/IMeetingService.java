package com.meeting.service; // 业务逻辑层接口包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.dto.MeetingQueryDTO; // 会议查询DTO
import com.meeting.entity.Meeting; // 会议实体

/**
 * 会议业务接口
 * 定义会议管理的业务操作契约
 */
public interface IMeetingService {

    /**
     * 添加新会议
     *
     * @param meeting 会议实体（不含id/时间戳）
     * @return 保存后的会议实体（含生成的id和时间戳）
     */
    Meeting addMeeting(Meeting meeting);

    /**
     * 分页查询会议列表（支持多条件筛选）
     *
     * @param queryDTO 查询条件DTO
     * @return 分页结果
     */
    IPage<Meeting> getMeetingPage(MeetingQueryDTO queryDTO);

    /**
     * 根据ID获取会议详情
     *
     * @param id 会议ID
     * @return 会议实体，不存在返回null
     */
    Meeting getMeetingById(Long id);

    /**
     * 更新会议信息
     *
     * @param meeting 包含更新字段的会议实体（必须含id）
     * @return 更新后的会议实体
     */
    Meeting updateMeeting(Meeting meeting);

    /**
     * 级联删除会议（同时删除参会人员和会议室占用记录）
     *
     * @param id 会议ID
     * @return true-成功，false-失败
     */
    boolean deleteMeeting(Long id);
}