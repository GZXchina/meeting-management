package com.meeting.service.impl; // 业务实现类包

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // 条件构造器
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页参数
import com.meeting.dto.ParticipantQueryDTO; // 参会人员查询DTO
import com.meeting.entity.Participant; // 参会人员实体
import com.meeting.mapper.ParticipantMapper; // 参会人员数据访问层
import com.meeting.service.IParticipantService; // 参会人员业务接口
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.stereotype.Service; // Spring Service注解
import java.time.LocalDateTime; // 时间类型

/**
 * 参会人员业务实现类
 * 实现参会人员管理的核心业务逻辑
 */
@Service // 注册为Spring Bean
public class ParticipantServiceImpl implements IParticipantService {

    @Autowired // 自动注入
    private ParticipantMapper participantMapper; // 参会人员数据访问

    /**
     * 添加参会人员
     * 自动设置时间戳和默认状态
     */
    @Override
    public Participant addParticipant(Participant participant) {
        participant.setCreatedAt(LocalDateTime.now()); // 设置创建时间
        participant.setUpdatedAt(LocalDateTime.now()); // 设置更新时间
        if (participant.getStatus() == null) {
            participant.setStatus(0); // 默认状态为"已报名"
        }
        participantMapper.insert(participant); // 执行插入
        return participant; // 返回含生成id的实体
    }

    /**
     * 分页查询参会人员列表
     */
    @Override
    public IPage<Participant> getParticipantPage(ParticipantQueryDTO queryDTO) {
        Page<Participant> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()); // 创建分页
        return participantMapper.selectParticipantPage(page,
                queryDTO.getUserId(),      // 用户ID筛选
                queryDTO.getName(),        // 姓名模糊搜索
                queryDTO.getDepartment(),  // 部门筛选
                queryDTO.getMeetingId(),   // 会议ID筛选
                queryDTO.getStatus());     // 状态筛选
    }

    /**
     * 根据ID查询参会人员详情
     */
    @Override
    public Participant getParticipantById(Long id) {
        return participantMapper.selectById(id); // MyBatisPlus内置方法
    }

    /**
     * 更新参会人员信息
     */
    @Override
    public Participant updateParticipant(Participant participant) {
        participant.setUpdatedAt(LocalDateTime.now()); // 刷新更新时间
        participantMapper.updateById(participant); // 执行更新
        return participantMapper.selectById(participant.getId()); // 查询最新数据
    }

    /**
     * 逻辑删除参会人员
     */
    @Override
    public boolean deleteParticipant(Long id) {
        return participantMapper.deleteById(id) > 0; // MyBatisPlus自动添加deleted条件
    }
}