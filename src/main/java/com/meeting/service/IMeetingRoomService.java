package com.meeting.service; // 业务逻辑层接口包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.dto.ConflictCheckDTO; // 冲突检测DTO
import com.meeting.entity.MeetingRoom; // 会议室实体

/**
 * 会议室业务接口
 * 定义会议室管理的业务操作契约
 */
public interface IMeetingRoomService {

    /**
     * 添加新会议室
     *
     * @param meetingRoom 会议室实体
     * @return 保存后的会议室实体
     */
    MeetingRoom addMeetingRoom(MeetingRoom meetingRoom);

    /**
     * 分页查询会议室列表（支持多条件筛选）
     */
    IPage<MeetingRoom> getMeetingRoomPage(String roomCode, String roomName, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 根据ID获取会议室详情
     */
    MeetingRoom getMeetingRoomById(Long id);

    /**
     * 更新会议室信息
     */
    MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom);

    /**
     * 删除会议室（同时清理占用记录）
     *
     * @param id 会议室ID
     * @return true-成功
     */
    boolean deleteMeetingRoom(Long id);

    /**
     * 检测会议室在指定时段是否存在时间冲突
     *
     * @param conflictCheckDTO 检测参数
     * @return true-有冲突，false-无冲突
     */
    boolean checkConflict(ConflictCheckDTO conflictCheckDTO);
}