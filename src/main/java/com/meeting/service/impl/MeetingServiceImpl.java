package com.meeting.service.impl; // 业务实现类包

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // MyBatisPlus Lambda条件构造器
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页参数
import com.meeting.dto.MeetingQueryDTO; // 会议查询DTO
import com.meeting.entity.Meeting; // 会议实体
import com.meeting.mapper.MeetingMapper; // 会议数据访问层
import com.meeting.mapper.MeetingRoomUsageMapper; // 会议室占用数据访问层
import com.meeting.mapper.ParticipantMapper; // 参会人员数据访问层
import com.meeting.service.IMeetingService; // 会议业务接口
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.stereotype.Service; // Spring Service注解
import org.springframework.transaction.annotation.Transactional; // Spring事务注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议业务实现类
 * 实现会议管理的核心业务逻辑，包含数据校验、默认值设置、级联操作等
 */
@Service // 注册为Spring Service Bean
public class MeetingServiceImpl implements IMeetingService {

    @Autowired // Spring自动注入
    private MeetingMapper meetingMapper; // 会议数据访问

    @Autowired
    private ParticipantMapper participantMapper; // 参会人员数据访问

    @Autowired
    private MeetingRoomUsageMapper meetingRoomUsageMapper; // 会议室占用数据访问

    /**
     * 添加新会议
     * 自动设置时间戳、默认状态和默认人数上限
     */
    @Override
    public Meeting addMeeting(Meeting meeting) {
        meeting.setCreatedAt(LocalDateTime.now()); // 设置创建时间为当前时间
        meeting.setUpdatedAt(LocalDateTime.now()); // 设置更新时间为当前时间
        if (meeting.getStatus() == null) {
            meeting.setStatus(0); // 默认状态为"待召开"
        }
        if (meeting.getMaxParticipants() == null) {
            meeting.setMaxParticipants(100); // 默认参会人数上限为100
        }
        meetingMapper.insert(meeting); // 执行数据库插入
        return meeting; // 返回含生成id的实体
    }

    /**
     * 分页查询会议列表
     * 将DTO参数传递给Mapper的自定义查询方法
     */
    @Override
    public IPage<Meeting> getMeetingPage(MeetingQueryDTO queryDTO) {
        Page<Meeting> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()); // 创建分页对象
        return meetingMapper.selectMeetingPage(page,
                queryDTO.getMeetingCode(),     // 传递会议编号
                queryDTO.getTitle(),           // 传递会议主题
                queryDTO.getDepartment(),      // 传递部门
                queryDTO.getStatus(),          // 传递状态
                queryDTO.getStartTimeFrom(),   // 传递开始时间下限
                queryDTO.getStartTimeTo());    // 传递开始时间上限
    }

    /**
     * 根据ID查询会议详情
     */
    @Override
    public Meeting getMeetingById(Long id) {
        return meetingMapper.selectById(id); // MyBatisPlus内置方法，自动过滤deleted=1的记录
    }

    /**
     * 更新会议信息
     * 自动刷新更新时间戳
     */
    @Override
    public Meeting updateMeeting(Meeting meeting) {
        meeting.setUpdatedAt(LocalDateTime.now()); // 更新时间为当前时间
        meetingMapper.updateById(meeting); // 执行数据库更新
        return meetingMapper.selectById(meeting.getId()); // 查询并返回最新数据
    }

    /**
     * 级联删除会议
     * 事务保护：先删参会人员，再删会议室占用，最后删会议本身
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 声明事务，任何异常自动回滚
    public boolean deleteMeeting(Long id) {
        // 步骤1：删除该会议下的所有参会人员（Lambda条件构造器，类型安全）
        participantMapper.delete(new LambdaQueryWrapper<com.meeting.entity.Participant>()
                .eq(com.meeting.entity.Participant::getMeetingId, id)); // WHERE meeting_id = ?
        // 步骤2：删除该会议的会议室占用记录
        meetingRoomUsageMapper.deleteByMeetingId(id);
        // 步骤3：逻辑删除会议本身（MyBatisPlus自动添加deleted条件）
        return meetingMapper.deleteById(id) > 0; // deleteById返回影响行数，>0表示成功
    }
}