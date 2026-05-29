package com.meeting.controller; // HTTP接口控制器包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.common.Result; // 统一响应封装
import com.meeting.dto.ConflictCheckDTO; // 冲突检测DTO
import com.meeting.entity.MeetingRoom; // 会议室实体
import com.meeting.service.IMeetingRoomService; // 会议室业务接口
import io.swagger.annotations.Api; // Swagger API分组
import io.swagger.annotations.ApiOperation; // Swagger接口描述
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.web.bind.annotation.*; // REST注解

/**
 * 会议室管理接口控制器
 * 提供会议室的增删改查和时间冲突检测API
 * 接口前缀: /api/room
 */
@Api(tags = "会议室管理接口") // Swagger分组标签
@RestController // 标识为RESTful控制器
@RequestMapping("/api/room") // 统一接口前缀
public class MeetingRoomController {

    @Autowired // Spring自动注入
    private IMeetingRoomService meetingRoomService; // 会议室业务层

    /**
     * 新增会议室
     * POST /api/room/add
     */
    @ApiOperation("新增会议室") // Swagger接口说明
    @PostMapping("/add") // POST请求添加会议室
    public Result<MeetingRoom> addMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        return Result.success(meetingRoomService.addMeetingRoom(meetingRoom)); // 包装返回
    }

    /**
     * 分页查询会议室列表
     * POST /api/room/page
     */
    @ApiOperation("分页查询会议室列表") // Swagger接口说明
    @PostMapping("/page") // POST请求分页查询
    public Result<IPage<MeetingRoom>> getMeetingRoomPage(@RequestParam(required = false) String roomCode,   // 编号筛选（可选）
                                                         @RequestParam(required = false) String roomName,   // 名称筛选（可选）
                                                         @RequestParam(required = false) Integer status,    // 状态筛选（可选）
                                                         @RequestParam(defaultValue = "1") Integer pageNum,  // 页码，默认1
                                                         @RequestParam(defaultValue = "10") Integer pageSize) { // 每页条数，默认10
        return Result.success(meetingRoomService.getMeetingRoomPage(roomCode, roomName, status, pageNum, pageSize));
    }

    /**
     * 根据ID获取会议室详情
     * GET /api/room/{id}
     */
    @ApiOperation("根据ID获取会议室详情") // Swagger接口说明
    @GetMapping("/{id}") // GET请求获取单个会议室
    public Result<MeetingRoom> getMeetingRoomById(@PathVariable Long id) {
        return Result.success(meetingRoomService.getMeetingRoomById(id)); // 返回会议室详情
    }

    /**
     * 更新会议室信息
     * PUT /api/room/update
     */
    @ApiOperation("更新会议室") // Swagger接口说明
    @PutMapping("/update") // PUT请求更新
    public Result<MeetingRoom> updateMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        return Result.success(meetingRoomService.updateMeetingRoom(meetingRoom)); // 返回更新后数据
    }

    /**
     * 删除会议室
     * DELETE /api/room/{id}
     */
    @ApiOperation("删除会议室") // Swagger接口说明
    @DeleteMapping("/{id}") // DELETE请求删除
    public Result<Boolean> deleteMeetingRoom(@PathVariable Long id) {
        return Result.success(meetingRoomService.deleteMeetingRoom(id)); // 返回删除结果
    }

    /**
     * 检测会议室时间冲突
     * POST /api/room/check-conflict
     */
    @ApiOperation("检测会议室时间冲突") // Swagger接口说明
    @PostMapping("/check-conflict") // POST请求检测冲突
    public Result<Boolean> checkConflict(@RequestBody ConflictCheckDTO conflictCheckDTO) {
        return Result.success(meetingRoomService.checkConflict(conflictCheckDTO)); // 返回是否有冲突
    }
}