package com.meeting.controller; // HTTP接口控制器包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.common.Result; // 统一响应封装
import com.meeting.dto.MeetingQueryDTO; // 会议查询DTO
import com.meeting.entity.Meeting; // 会议实体
import com.meeting.service.IMeetingService; // 会议业务接口
import io.swagger.annotations.Api; // Swagger API分组注解
import io.swagger.annotations.ApiOperation; // Swagger接口描述注解
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.web.bind.annotation.*; // REST注解（RestController/RequestMapping等）

/**
 * 会议管理接口控制器
 * 提供会议的增删改查RESTful API接口
 * 接口前缀: /api/meeting
 */
@Api(tags = "会议管理接口") // Swagger分组标签
@RestController // 标识为RESTful控制器，所有方法返回JSON
@RequestMapping("/api/meeting") // 统一设置接口路径前缀
public class MeetingController {

    @Autowired // Spring自动注入
    private IMeetingService meetingService; // 会议业务层

    /**
     * 新增会议
     * POST /api/meeting/add
     */
    @ApiOperation("新增会议") // Swagger接口说明
    @PostMapping("/add") // 映射POST请求到 /api/meeting/add
    public Result<Meeting> addMeeting(@RequestBody Meeting meeting) { // @RequestBody: 从请求体提取JSON
        return Result.success(meetingService.addMeeting(meeting)); // 调用service并包装返回
    }

    /**
     * 分页查询会议列表
     * POST /api/meeting/page
     */
    @ApiOperation("分页查询会议列表") // Swagger接口说明
    @PostMapping("/page") // 映射POST请求到 /api/meeting/page
    public Result<IPage<Meeting>> getMeetingPage(@RequestBody MeetingQueryDTO queryDTO) {
        return Result.success(meetingService.getMeetingPage(queryDTO)); // 返回分页结果
    }

    /**
     * 根据ID获取会议详情
     * GET /api/meeting/{id}
     */
    @ApiOperation("根据ID获取会议详情") // Swagger接口说明
    @GetMapping("/{id}") // 映射GET请求，{id}为路径变量
    public Result<Meeting> getMeetingById(@PathVariable Long id) { // @PathVariable: 提取URL路径变量
        return Result.success(meetingService.getMeetingById(id)); // 返回会议详情
    }

    /**
     * 更新会议信息
     * PUT /api/meeting/update
     */
    @ApiOperation("更新会议") // Swagger接口说明
    @PutMapping("/update") // 映射PUT请求到 /api/meeting/update
    public Result<Meeting> updateMeeting(@RequestBody Meeting meeting) {
        return Result.success(meetingService.updateMeeting(meeting)); // 返回更新后的数据
    }

    /**
     * 删除会议（级联删除参会人员和占用记录）
     * DELETE /api/meeting/{id}
     */
    @ApiOperation("删除会议") // Swagger接口说明
    @DeleteMapping("/{id}") // 映射DELETE请求，{id}为路径变量
    public Result<Boolean> deleteMeeting(@PathVariable Long id) {
        return Result.success(meetingService.deleteMeeting(id)); // 返回删除结果
    }
}