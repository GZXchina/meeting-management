package com.meeting.service.impl; // 业务实现类包

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // MyBatisPlus Lambda条件构造器
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页参数
import com.meeting.dto.ConflictCheckDTO; // 冲突检测DTO
import com.meeting.entity.MeetingRoom; // 会议室实体
import com.meeting.entity.MeetingRoomUsage; // 会议室占用实体
import com.meeting.mapper.MeetingRoomMapper; // 会议室数据访问层
import com.meeting.mapper.MeetingRoomUsageMapper; // 会议室占用数据访问层
import com.meeting.service.IMeetingRoomService; // 会议室业务接口
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.stereotype.Service; // Spring Service注解
import org.springframework.transaction.annotation.Transactional; // Spring事务注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议室业务实现类
 * 实现会议室管理的核心业务逻辑，包含时间冲突检测
 */
@Service // 注册为Spring Bean
public class MeetingRoomServiceImpl implements IMeetingRoomService {

    @Autowired // 自动注入
    private MeetingRoomMapper meetingRoomMapper; // 会议室数据访问

    @Autowired
    private MeetingRoomUsageMapper meetingRoomUsageMapper; // 占用记录数据访问

    /**
     * 添加新会议室
     * 自动设置时间戳和默认状态
     */
    @Override
    public MeetingRoom addMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoom.setCreatedAt(LocalDateTime.now()); // 设置创建时间
        meetingRoom.setUpdatedAt(LocalDateTime.now()); // 设置更新时间
        if (meetingRoom.getStatus() == null) {
            meetingRoom.setStatus(0); // 默认状态为"空闲"
        }
        meetingRoomMapper.insert(meetingRoom); // 执行插入
        return meetingRoom; // 返回含生成id的实体
    }

    /**
     * 分页查询会议室列表
     */
    @Override
    public IPage<MeetingRoom> getMeetingRoomPage(String roomCode, String roomName, Integer status, Integer pageNum, Integer pageSize) {
        Page<MeetingRoom> page = new Page<>(pageNum, pageSize); // 创建分页
        return meetingRoomMapper.selectRoomPage(page, roomCode, roomName, status); // 执行自定义查询
    }

    /**
     * 根据ID查询会议室详情
     */
    @Override
    public MeetingRoom getMeetingRoomById(Long id) {
        return meetingRoomMapper.selectById(id); // MyBatisPlus内置方法
    }

    /**
     * 更新会议室信息
     */
    @Override
    public MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoom.setUpdatedAt(LocalDateTime.now()); // 刷新更新时间
        meetingRoomMapper.updateById(meetingRoom); // 执行更新
        return meetingRoomMapper.selectById(meetingRoom.getId()); // 查询最新数据
    }

    /**
     * 删除会议室（同时清理占用记录）
     * 事务保护确保一致性
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 声明事务
    public boolean deleteMeetingRoom(Long id) {
        // 清理该会议室的所有占用记录
        MeetingRoomUsage usage = new MeetingRoomUsage(); // 创建查询条件
        usage.setMeetingRoomId(id); // 设置会议室ID
        meetingRoomUsageMapper.delete(new LambdaQueryWrapper<MeetingRoomUsage>()
                .eq(MeetingRoomUsage::getMeetingRoomId, id)); // WHERE meeting_room_id = ?
        // 逻辑删除会议室
        return meetingRoomMapper.deleteById(id) > 0; // 返回是否成功
    }

    /**
     * 检测会议室时间冲突
     * 调用Mapper的countConflict方法统计冲突记录
     */
    @Override
    public boolean checkConflict(ConflictCheckDTO conflictCheckDTO) {
        // 统计与指定时段冲突的记录数
        int count = meetingRoomUsageMapper.countConflict(
                conflictCheckDTO.getMeetingRoomId(),     // 会议室ID
                conflictCheckDTO.getStartTime(),         // 开始时间
                conflictCheckDTO.getEndTime(),           // 结束时间
                conflictCheckDTO.getExcludeMeetingId()); // 排除的会议ID
        return count > 0; // count>0表示存在冲突
    }
}