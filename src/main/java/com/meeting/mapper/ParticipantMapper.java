package com.meeting.mapper; // 数据访问层包

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MyBatisPlus通用Mapper
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页参数
import com.meeting.entity.Participant; // 参会人员实体
import org.apache.ibatis.annotations.Mapper; // MyBatis Mapper标记
import org.apache.ibatis.annotations.Param; // 参数名映射

/**
 * 参会人员数据访问层
 * 负责参会人员信息的增删改查和统计分析
 */
@Mapper // 注册为MyBatis Mapper
public interface ParticipantMapper extends BaseMapper<Participant> {

    /**
     * 分页查询参会人员列表（支持多条件筛选）
     *
     * @param page       分页参数
     * @param userId     用户ID筛选（可选）
     * @param name       姓名模糊搜索（可选）
     * @param department 部门筛选（可选）
     * @param meetingId  会议ID筛选（可选）
     * @param status     参会状态筛选（可选）
     * @return 分页结果
     */
    IPage<Participant> selectParticipantPage(Page<?> page,
                                             @Param("userId") String userId,
                                             @Param("name") String name,
                                             @Param("department") String department,
                                             @Param("meetingId") Long meetingId,
                                             @Param("status") Integer status);

    /**
     * 按会议ID和状态统计参会人员数量
     * 用于计算参会率等统计指标
     *
     * @param meetingId 会议ID
     * @param status    参会状态
     * @return 符合条件的记录数
     */
    int countByMeetingIdAndStatus(@Param("meetingId") Long meetingId, @Param("status") Integer status);
}