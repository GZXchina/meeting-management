package com.meeting.mapper; // 数据访问层包

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MyBatisPlus通用Mapper
import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // 分页参数
import com.meeting.entity.MeetingRoom; // 会议室实体
import org.apache.ibatis.annotations.Mapper; // MyBatis Mapper标记
import org.apache.ibatis.annotations.Param; // 参数名映射

/**
 * 会议室数据访问层
 * 继承BaseMapper获得通用CRUD，自定义分页查询
 */
@Mapper // 注册为MyBatis Mapper
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {

    /**
     * 分页查询会议室列表（支持按编号/名称/状态筛选）
     *
     * @param page     分页参数
     * @param roomCode 会议室编号筛选（可选）
     * @param roomName 会议室名称模糊搜索（可选）
     * @param status   使用状态筛选（可选）
     * @return 分页结果
     */
    IPage<MeetingRoom> selectRoomPage(Page<?> page,
                                       @Param("roomCode") String roomCode, // 绑定 #{roomCode}
                                       @Param("roomName") String roomName, // 绑定 #{roomName}
                                       @Param("status") Integer status);   // 绑定 #{status}
}