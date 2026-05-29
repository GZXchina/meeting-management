package com.meeting.service; // 业务逻辑层接口包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.dto.ParticipantQueryDTO; // 参会人员查询DTO
import com.meeting.entity.Participant; // 参会人员实体

/**
 * 参会人员业务接口
 * 定义参会人员管理的业务操作契约
 */
public interface IParticipantService {

    /**
     * 添加参会人员
     *
     * @param participant 参会人员实体
     * @return 保存后的实体
     */
    Participant addParticipant(Participant participant);

    /**
     * 分页查询参会人员列表（支持多条件筛选）
     *
     * @param queryDTO 查询条件DTO
     * @return 分页结果
     */
    IPage<Participant> getParticipantPage(ParticipantQueryDTO queryDTO);

    /**
     * 根据ID获取参会人员详情
     */
    Participant getParticipantById(Long id);

    /**
     * 更新参会人员信息
     */
    Participant updateParticipant(Participant participant);

    /**
     * 逻辑删除参会人员
     *
     * @param id 参会人员ID
     * @return true-成功
     */
    boolean deleteParticipant(Long id);
}