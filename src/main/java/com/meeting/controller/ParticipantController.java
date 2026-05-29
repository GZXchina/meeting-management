package com.meeting.controller; // HTTP接口控制器包

import com.baomidou.mybatisplus.core.metadata.IPage; // 分页结果
import com.meeting.common.Result; // 统一响应封装
import com.meeting.dto.ParticipantQueryDTO; // 参会人员查询DTO
import com.meeting.entity.Participant; // 参会人员实体
import com.meeting.service.IParticipantService; // 参会人员业务接口
import io.swagger.annotations.Api; // Swagger API分组
import io.swagger.annotations.ApiOperation; // Swagger接口描述
import org.springframework.beans.factory.annotation.Autowired; // Spring依赖注入
import org.springframework.web.bind.annotation.*; // REST注解

/**
 * 参会人员管理接口控制器
 * 提供参会人员的增删改查RESTful API
 * 接口前缀: /api/participant
 */
@Api(tags = "参会人员管理接口") // Swagger分组标签
@RestController // 标识为RESTful控制器
@RequestMapping("/api/participant") // 统一接口前缀
public class ParticipantController {

    @Autowired // Spring自动注入
    private IParticipantService participantService; // 参会人员业务层

    /**
     * 新增参会人员
     * POST /api/participant/add
     */
    @ApiOperation("新增参会人员") // Swagger接口说明
    @PostMapping("/add") // POST请求添加
    public Result<Participant> addParticipant(@RequestBody Participant participant) {
        return Result.success(participantService.addParticipant(participant)); // 包装返回
    }

    /**
     * 分页查询参会人员列表
     * POST /api/participant/page
     */
    @ApiOperation("分页查询参会人员列表") // Swagger接口说明
    @PostMapping("/page") // POST请求分页查询
    public Result<IPage<Participant>> getParticipantPage(@RequestBody ParticipantQueryDTO queryDTO) {
        return Result.success(participantService.getParticipantPage(queryDTO)); // 返回分页结果
    }

    /**
     * 根据ID获取参会人员详情
     * GET /api/participant/{id}
     */
    @ApiOperation("根据ID获取参会人员详情") // Swagger接口说明
    @GetMapping("/{id}") // GET请求获取单个参会人员
    public Result<Participant> getParticipantById(@PathVariable Long id) {
        return Result.success(participantService.getParticipantById(id)); // 返回详情
    }

    /**
     * 更新参会人员信息
     * PUT /api/participant/update
     */
    @ApiOperation("更新参会人员") // Swagger接口说明
    @PutMapping("/update") // PUT请求更新
    public Result<Participant> updateParticipant(@RequestBody Participant participant) {
        return Result.success(participantService.updateParticipant(participant)); // 返回更新后数据
    }

    /**
     * 删除参会人员
     * DELETE /api/participant/{id}
     */
    @ApiOperation("删除参会人员") // Swagger接口说明
    @DeleteMapping("/{id}") // DELETE请求删除
    public Result<Boolean> deleteParticipant(@PathVariable Long id) {
        return Result.success(participantService.deleteParticipant(id)); // 返回删除结果
    }
}