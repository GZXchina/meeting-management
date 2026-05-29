package com.meeting.mapper; // 数据访问层包

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MyBatisPlus通用Mapper，内置增删改查
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果对象
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页查询参数
import com.meeting.entity.Meeting; // 会议实体
import org.apache.ibatis.annotations.Mapper; // MyBatis Mapper标记注解
import org.apache.ibatis.annotations.Param; // MyBatis参数名映射注解
import java.time.LocalDateTime; // 时间类型

/**
 * 会议数据访问层
 * 继承BaseMapper获得通用CRUD方法，自定义复杂查询方法
 */
@Mapper // 标识为MyBatis Mapper接口，Spring自动扫描注册
public interface MeetingMapper extends BaseMapper<Meeting> {

    /**
     * 分页查询会议列表（支持多条件筛选）
     *
     * @param page          分页参数（当前页/每页条数）
     * @param meetingCode   会议编号筛选（可选）
     * @param title         会议主题模糊搜索（可选）
     * @param department    部门筛选（可选）
     * @param status        状态筛选（可选）
     * @param startTimeFrom 开始时间范围起始（可选）
     * @param startTimeTo   开始时间范围结束（可选）
     * @return 分页结果
     */
    IPage<Meeting> selectMeetingPage(Page<?> page,
                                     @Param("meetingCode") String meetingCode, // 绑定XML中的 #{meetingCode}
                                     @Param("title") String title,
                                     @Param("department") String department,
                                     @Param("status") Integer status,
                                     @Param("startTimeFrom") LocalDateTime startTimeFrom,
                                     @Param("startTimeTo") LocalDateTime startTimeTo);

    /**
     * 级联删除会议（物理删除）
     * 同时删除关联的参会人员和会议室占用记录后调用
     *
     * @param id 会议ID
     * @return 删除的行数
     */
    int deleteMeetingCascade(@Param("id") Long id); // 绑定XML中的 #{id}
}