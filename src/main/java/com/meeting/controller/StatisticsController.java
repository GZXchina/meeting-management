package com.meeting.controller; // HTTP接口控制器包

import com.meeting.common.Result; // 统一响应封装
import com.meeting.dto.RoomUsageQueryDTO; // 会议室使用统计DTO
import com.meeting.service.IStatisticsService; // 统计业务接口
import io.swagger.annotations.Api; // Swagger API分组
import io.swagger.annotations.ApiOperation; // Swagger接口描述
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.web.bind.annotation.*; // REST注解
import java.util.Map; // Map集合

/**
 * 统计接口控制器
 * 提供会议统计、会议室使用统计、参会率统计三个统计维度的API
 * 接口前缀: /api/statistics
 */
@Api(tags = "统计接口") // Swagger分组标签
@RestController // 标识为RESTful控制器
@RequestMapping("/api/statistics") // 统一接口前缀
public class StatisticsController {

    @Autowired // Spring自动注入
    private IStatisticsService statisticsService; // 统计业务层

    /**
     * 会议统计
     * GET /api/statistics/meeting?department=&status=
     */
    @ApiOperation("会议统计（按部门和状态统计会议数量）") // Swagger接口说明
    @GetMapping("/meeting") // GET请求统计
    public Result<Map<String, Object>> getMeetingStatistics(@RequestParam(required = false) String department, // 部门筛选（可选）
                                                            @RequestParam(required = false) Integer status) {   // 状态筛选（可选）
        return Result.success(statisticsService.getMeetingStatistics(department, status)); // 返回统计数据
    }

    /**
     * 会议室使用统计
     * POST /api/statistics/room-usage
     */
    @ApiOperation("会议室使用统计") // Swagger接口说明
    @PostMapping("/room-usage") // POST请求统计
    public Result<Map<String, Object>> getRoomUsageStatistics(@RequestBody RoomUsageQueryDTO queryDTO) {
        return Result.success(statisticsService.getRoomUsageStatistics(queryDTO)); // 返回使用统计数据
    }

    /**
     * 参会率统计
     * GET /api/statistics/attendance/{meetingId}
     */
    @ApiOperation("参会率统计") // Swagger接口说明
    @GetMapping("/attendance/{meetingId}") // GET请求参会率
    public Result<Map<String, Object>> getAttendanceRateStatistics(@PathVariable Long meetingId) { // 从路径提取会议ID
        return Result.success(statisticsService.getAttendanceRateStatistics(meetingId)); // 返回参会率数据
    }
}