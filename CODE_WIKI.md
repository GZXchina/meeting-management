# Meeting Management System - Code Wiki

## 1. 项目概述

**项目名称**：会议管理系统（Meeting Management System）  
**GroupId**：`com.meeting`  
**ArtifactId**：`meeting-management`  
**版本**：1.0.0  
**技术栈**：Spring Boot 2.7.18 + MyBatis-Plus 3.5.5 + MySQL 5.x + Swagger2 2.9.2  
**JDK 版本**：1.8  
**打包方式**：JAR

本系统是一个基于 Spring Boot 的会议管理后台 API 服务，提供会议信息管理、会议室管理、参会人员管理以及统计分析等核心功能。

---

## 2. 项目整体架构

### 2.1 分层架构

项目采用经典的 **Controller → Service → Mapper** 三层架构，遵循职责单一原则：

```
┌─────────────────────────────────────────────────┐
│                  Controller 层                  │
│        (接收请求、参数校验、响应封装)              │
├─────────────────────────────────────────────────┤
│                   Service 层                     │
│        (业务逻辑、事务管理、数据编排)              │
├─────────────────────────────────────────────────┤
│                   Mapper 层                      │
│        (数据访问、SQL映射、持久化操作)             │
├─────────────────────────────────────────────────┤
│                   MySQL 数据库                  │
│        (meeting_db, 4张核心业务表)               │
└─────────────────────────────────────────────────┘
```

### 2.2 包结构

```
com.meeting
├── MeetingApplication.java          # Spring Boot 启动类
├── common/                          # 公共模块
│   ├── Constants.java               # 系统常量定义
│   └── Result.java                  # 统一响应封装
├── config/                          # 配置模块
│   ├── MyBatisPlusConfig.java       # MyBatis-Plus 配置
│   └── Swagger2Config.java          # Swagger2 API 文档配置
├── controller/                      # 控制器层
│   ├── MeetingController.java       # 会议管理接口
│   ├── MeetingRoomController.java   # 会议室管理接口
│   ├── ParticipantController.java   # 参会人员管理接口
│   └── StatisticsController.java    # 统计查询接口
├── dto/                             # 数据传输对象
│   ├── ConflictCheckDTO.java        # 冲突检测请求对象
│   ├── MeetingQueryDTO.java         # 会议查询条件对象
│   ├── ParticipantQueryDTO.java     # 参会人员查询条件对象
│   └── RoomUsageQueryDTO.java       # 会议室使用统计查询对象
├── entity/                          # 实体类（数据模型）
│   ├── Meeting.java                 # 会议实体
│   ├── MeetingRoom.java             # 会议室实体
│   ├── MeetingRoomUsage.java        # 会议室占用记录实体
│   └── Participant.java             # 参会人员实体
├── mapper/                          # 数据访问层接口
│   ├── MeetingMapper.java           # 会议 Mapper
│   ├── MeetingRoomMapper.java       # 会议室 Mapper
│   ├── MeetingRoomUsageMapper.java  # 会议室占用记录 Mapper
│   └── ParticipantMapper.java       # 参会人员 Mapper
└── service/                         # 服务层
    ├── IMeetingService.java         # 会议服务接口
    ├── IMeetingRoomService.java     # 会议室服务接口
    ├── IParticipantService.java     # 参会人员服务接口
    ├── IStatisticsService.java      # 统计服务接口
    └── impl/                        # 服务实现
        ├── MeetingServiceImpl.java
        ├── MeetingRoomServiceImpl.java
        ├── ParticipantServiceImpl.java
        └── StatisticsServiceImpl.java
```

### 2.3 架构依赖关系图

```
Controller ──依赖──▶ Service接口 ◀──实现── ServiceImpl ──依赖──▶ Mapper接口
    │                                                              │
    │                                                              │
    ▼                                                              ▼
  DTO 对象                                                    Mapper XML
                                                              (SQL映射)
                                                                │
                                                                ▼
                                                           Entity 实体
                                                                │
                                                                ▼
                                                           MySQL 数据库
```

---

## 3. 数据库设计

### 3.1 数据库信息

- **数据库名**：`meeting_db`
- **字符集**：`utf8mb4` / `utf8mb4_unicode_ci`
- **兼容版本**：MySQL 5.5+

### 3.2 表结构

#### 3.2.1 meeting（会议信息表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT AUTO_INCREMENT | 主键ID |
| meeting_code | VARCHAR(50) UNIQUE | 会议编号 |
| title | VARCHAR(200) | 会议主题 |
| start_time | DATETIME | 开始时间 |
| end_time | DATETIME | 结束时间 |
| location | VARCHAR(200) | 会议地点 |
| department | VARCHAR(100) | 主办部门 |
| max_participants | INT (默认100) | 参会人数上限 |
| status | TINYINT (默认0) | 会议状态：0-待召开 / 1-进行中 / 2-已结束 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT (默认0) | 逻辑删除标记 |

**索引**：`idx_meeting_code`、`idx_start_time`、`idx_department`、`idx_status`

#### 3.2.2 participant（参会人员表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT AUTO_INCREMENT | 主键ID |
| user_id | VARCHAR(50) | 用户ID |
| name | VARCHAR(100) | 姓名 |
| department | VARCHAR(100) | 部门 |
| phone | VARCHAR(20) | 联系方式 |
| meeting_id | BIGINT | 关联会议ID |
| status | TINYINT (默认0) | 参会状态：0-已报名 / 1-未签到 / 2-已参会 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT (默认0) | 逻辑删除标记 |

**索引**：`idx_meeting_id`、`uk_user_meeting(user_id, meeting_id)`（唯一约束）、`idx_user_id`、`idx_status`

#### 3.2.3 meeting_room（会议室表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT AUTO_INCREMENT | 主键ID |
| room_code | VARCHAR(50) UNIQUE | 会议室编号 |
| room_name | VARCHAR(100) | 会议室名称 |
| capacity | INT | 容量 |
| equipment | VARCHAR(500) | 设备清单（JSON格式） |
| status | TINYINT (默认0) | 使用状态：0-空闲 / 1-占用 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| deleted | TINYINT (默认0) | 逻辑删除标记 |

**索引**：`idx_room_code`、`idx_status`

#### 3.2.4 meeting_room_usage（会议室占用记录表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT AUTO_INCREMENT | 主键ID |
| meeting_id | BIGINT | 关联会议ID |
| meeting_room_id | BIGINT | 关联会议室ID |
| start_time | DATETIME | 占用开始时间 |
| end_time | DATETIME | 占用结束时间 |
| created_at | TIMESTAMP | 创建时间 |
| deleted | TINYINT (默认0) | 逻辑删除标记 |

**索引**：`idx_meeting_room_id`、`idx_meeting_id`、`idx_time_range(meeting_room_id, start_time, end_time)`

### 3.3 表间关系

```
meeting (1) ──────▶ (N) participant          一个会议有多个参会人员
meeting (1) ──────▶ (N) meeting_room_usage   一个会议可占用多个时段
meeting_room (1) ─▶ (N) meeting_room_usage   一个会议室有多条占用记录
```

---

## 4. 核心模块详解

### 4.1 公共模块（common）

#### 4.1.1 Constants — 系统常量

定义系统中使用的状态码常量，采用静态内部类组织：

| 内部类 | 常量名 | 值 | 说明 |
|--------|--------|----|------|
| MeetingStatus | PENDING | 0 | 待召开 |
| MeetingStatus | IN_PROGRESS | 1 | 进行中 |
| MeetingStatus | ENDED | 2 | 已结束 |
| ParticipantStatus | REGISTERED | 0 | 已报名 |
| ParticipantStatus | NOT_SIGNED | 1 | 未签到 |
| ParticipantStatus | ATTENDED | 2 | 已参会 |
| RoomStatus | FREE | 0 | 空闲 |
| RoomStatus | OCCUPIED | 1 | 占用 |

#### 4.1.2 Result\<T\> — 统一响应封装

泛型响应包装类，所有 API 接口统一返回此格式：

```java
{
    "code": 200,        // 状态码：200-成功，500-失败
    "message": "success", // 提示信息
    "data": {}           // 业务数据（泛型）
}
```

**核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `Result.success()` | 返回无数据的成功响应 |
| `Result.success(T data)` | 返回携带数据的成功响应 |
| `Result.error(String message)` | 返回错误响应（code=500） |
| `Result.error(Integer code, String message)` | 返回自定义错误码的错误响应 |

### 4.2 配置模块（config）

#### 4.2.1 MyBatisPlusConfig

注册 MyBatis-Plus 分页插件（`PaginationInnerInterceptor`），指定数据库类型为 MySQL，使 `IPage` 分页查询生效。

#### 4.2.2 Swagger2Config

配置 Swagger2 API 文档，扫描 `com.meeting.controller` 包下的所有接口，提供在线 API 文档界面。访问地址：`http://localhost:8080/swagger-ui.html`

### 4.3 实体模块（entity）

所有实体类均使用 Lombok `@Data` 注解自动生成 getter/setter，并使用 MyBatis-Plus 注解进行 ORM 映射：

| 实体类 | 表名 | 主键策略 | 逻辑删除 | 自动填充 |
|--------|------|----------|----------|----------|
| Meeting | meeting | AUTO（自增） | deleted 字段 | createdAt(INSERT)、updatedAt(INSERT_UPDATE) |
| MeetingRoom | meeting_room | AUTO | deleted 字段 | createdAt(INSERT)、updatedAt(INSERT_UPDATE) |
| MeetingRoomUsage | meeting_room_usage | AUTO | deleted 字段 | createdAt(INSERT) |
| Participant | participant | AUTO | deleted 字段 | createdAt(INSERT)、updatedAt(INSERT_UPDATE) |

**通用特征**：
- `@TableId(type = IdType.AUTO)`：主键自增
- `@TableLogic`：逻辑删除字段（0-未删除，1-已删除）
- `@TableField(fill = FieldFill.INSERT)` / `FieldFill.INSERT_UPDATE`：自动填充时间字段
- `@JsonFormat`：日期格式化为 `yyyy-MM-dd HH:mm:ss`，时区 `GMT+8`

### 4.4 DTO 模块（dto）

数据传输对象，用于接收前端请求参数：

| DTO 类 | 用途 | 关键字段 |
|--------|------|----------|
| MeetingQueryDTO | 会议分页查询条件 | meetingCode, title, department, status, startTimeFrom, startTimeTo, pageNum, pageSize |
| ParticipantQueryDTO | 参会人员分页查询条件 | userId, name, department, meetingId, status, pageNum, pageSize |
| ConflictCheckDTO | 会议室时间冲突检测 | meetingRoomId, startTime, endTime, excludeMeetingId |
| RoomUsageQueryDTO | 会议室使用统计查询 | meetingRoomId, startDate, endDate, type |

### 4.5 控制器模块（controller）

所有控制器均使用 `@RestController` + `@RequestMapping`，返回 `Result<T>` 统一响应格式，并使用 Swagger `@Api` / `@ApiOperation` 注解生成 API 文档。

#### 4.5.1 MeetingController — 会议管理

**基础路径**：`/api/meeting`

| HTTP 方法 | 路径 | 方法名 | 说明 |
|-----------|------|--------|------|
| POST | `/add` | addMeeting | 添加会议 |
| GET | `/list` | getMeetingList | 查询会议列表（分页） |
| GET | `/{id}` | getMeetingById | 获取会议详情 |
| PUT | `/update` | updateMeeting | 修改会议信息 |
| DELETE | `/delete/{id}` | deleteMeeting | 删除会议（级联删除参会人员和占用记录） |
| GET | `/query` | queryMeeting | 条件查询会议（分页） |

#### 4.5.2 MeetingRoomController — 会议室管理

**基础路径**：`/api/meeting-room`

| HTTP 方法 | 路径 | 方法名 | 说明 |
|-----------|------|--------|------|
| POST | `/add` | addMeetingRoom | 添加会议室 |
| GET | `/list` | getMeetingRoomList | 查询会议室列表（分页） |
| GET | `/{id}` | getMeetingRoomById | 获取会议室详情 |
| PUT | `/update` | updateMeetingRoom | 修改会议室信息 |
| DELETE | `/delete/{id}` | deleteMeetingRoom | 删除会议室（级联删除占用记录） |
| POST | `/check-conflict` | checkConflict | 检测会议室时间冲突 |

#### 4.5.3 ParticipantController — 参会人员管理

**基础路径**：`/api/participant`

| HTTP 方法 | 路径 | 方法名 | 说明 |
|-----------|------|--------|------|
| POST | `/add` | addParticipant | 添加参会人员 |
| GET | `/list` | getParticipantList | 查询参会人员列表（分页） |
| GET | `/{id}` | getParticipantById | 获取参会人员详情 |
| PUT | `/update` | updateParticipant | 修改参会人员信息 |
| DELETE | `/delete/{id}` | deleteParticipant | 删除参会人员 |
| GET | `/query` | queryParticipant | 条件查询参会人员（分页） |

#### 4.5.4 StatisticsController — 统计查询

**基础路径**：`/api/statistics`

| HTTP 方法 | 路径 | 方法名 | 说明 |
|-----------|------|--------|------|
| GET | `/meeting` | getMeetingStatistics | 会议统计（按部门/状态筛选） |
| GET | `/room-usage` | getRoomUsageStatistics | 会议室使用统计 |
| GET | `/attendance-rate` | getAttendanceRateStatistics | 参会率统计 |

### 4.6 服务模块（service）

#### 4.6.1 IMeetingService / MeetingServiceImpl

会议管理核心业务逻辑：

| 方法 | 说明 | 关键逻辑 |
|------|------|----------|
| `addMeeting(Meeting)` | 添加会议 | 自动设置 createdAt/updatedAt，status 默认 0，maxParticipants 默认 100 |
| `getMeetingPage(MeetingQueryDTO)` | 分页查询 | 委托 Mapper 自定义 SQL，支持按编号/标题/部门/状态/时间范围筛选 |
| `getMeetingById(Long)` | 按ID查询 | 直接调用 MyBatis-Plus selectById |
| `updateMeeting(Meeting)` | 更新会议 | 更新 updatedAt 后调用 updateById，再查询返回最新数据 |
| `deleteMeeting(Long)` | 删除会议 | **事务操作**：先删除关联的参会人员，再删除占用记录，最后删除会议本身 |

#### 4.6.2 IMeetingRoomService / MeetingRoomServiceImpl

会议室管理核心业务逻辑：

| 方法 | 说明 | 关键逻辑 |
|------|------|----------|
| `addMeetingRoom(MeetingRoom)` | 添加会议室 | 自动设置 createdAt/updatedAt，status 默认 0 |
| `getMeetingRoomPage(...)` | 分页查询 | 委托 Mapper 自定义 SQL，支持按编号/名称/状态筛选 |
| `getMeetingRoomById(Long)` | 按ID查询 | 直接调用 selectById |
| `updateMeetingRoom(MeetingRoom)` | 更新会议室 | 更新 updatedAt 后调用 updateById |
| `deleteMeetingRoom(Long)` | 删除会议室 | **事务操作**：先删除关联的占用记录，再删除会议室 |
| `checkConflict(ConflictCheckDTO)` | 冲突检测 | 委托 Mapper 查询时间重叠的占用记录数量 |

#### 4.6.3 IParticipantService / ParticipantServiceImpl

参会人员管理核心业务逻辑：

| 方法 | 说明 | 关键逻辑 |
|------|------|----------|
| `addParticipant(Participant)` | 添加参会人员 | 自动设置 createdAt/updatedAt，status 默认 0 |
| `getParticipantPage(ParticipantQueryDTO)` | 分页查询 | 委托 Mapper 自定义 SQL，支持按用户ID/姓名/部门/会议ID/状态筛选 |
| `getParticipantById(Long)` | 按ID查询 | 直接调用 selectById |
| `updateParticipant(Participant)` | 更新参会人员 | 更新 updatedAt 后调用 updateById |
| `deleteParticipant(Long)` | 删除参会人员 | 直接调用 deleteById |

#### 4.6.4 IStatisticsService / StatisticsServiceImpl

统计分析核心业务逻辑：

| 方法 | 说明 | 返回数据 |
|------|------|----------|
| `getMeetingStatistics(department, status)` | 会议统计 | totalCount, pendingCount, inProgressCount, endedCount |
| `getRoomUsageStatistics(RoomUsageQueryDTO)` | 会议室使用统计 | totalUsageCount, totalUsageMinutes, averageUsageMinutes, dailyCount（按日期分组） |
| `getAttendanceRateStatistics(meetingId)` | 参会率统计 | meetingId, meetingTitle, maxParticipants, registeredCount, attendedCount, attendanceRate |

### 4.7 数据访问模块（mapper）

所有 Mapper 接口均继承 `BaseMapper<T>`，获得 MyBatis-Plus 提供的通用 CRUD 方法，同时通过 XML 定义自定义 SQL。

#### 4.7.1 MeetingMapper

| 方法 | 类型 | 说明 |
|------|------|------|
| `selectMeetingPage(page, meetingCode, title, department, status, startTimeFrom, startTimeTo)` | 自定义 | 多条件分页查询，支持模糊搜索标题、精确匹配编号/部门/状态、时间范围过滤 |
| `deleteMeetingCascade(id)` | 自定义 | 级联删除会议（XML 中定义但实际未使用，Service 层自行处理级联） |

#### 4.7.2 MeetingRoomMapper

| 方法 | 类型 | 说明 |
|------|------|------|
| `selectRoomPage(page, roomCode, roomName, status)` | 自定义 | 多条件分页查询，支持模糊搜索名称、精确匹配编号/状态 |

#### 4.7.3 MeetingRoomUsageMapper

| 方法 | 类型 | 说明 |
|------|------|------|
| `countConflict(meetingRoomId, startTime, endTime, excludeMeetingId)` | 自定义 | 冲突检测：查询指定会议室在给定时间段内是否有重叠的占用记录 |
| `deleteByMeetingId(meetingId)` | 自定义 | 按会议ID删除占用记录 |

**冲突检测 SQL 逻辑**（三种重叠情况）：
1. 已有记录的开始时间 ≤ 新开始时间，且结束时间 > 新开始时间
2. 已有记录的开始时间 < 新结束时间，且结束时间 ≥ 新结束时间
3. 已有记录完全包含在新时间段内

#### 4.7.4 ParticipantMapper

| 方法 | 类型 | 说明 |
|------|------|------|
| `selectParticipantPage(page, userId, name, department, meetingId, status)` | 自定义 | 多条件分页查询，支持模糊搜索姓名、精确匹配用户ID/部门/会议ID/状态 |
| `countByMeetingIdAndStatus(meetingId, status)` | 自定义 | 按会议ID和状态统计参会人员数量 |

---

## 5. 依赖关系

### 5.1 Maven 依赖

| 依赖 | 版本 | 作用 |
|------|------|------|
| spring-boot-starter-web | 2.7.18 | Spring MVC Web 框架 |
| mybatis-plus-boot-starter | 3.5.5 | MyBatis-Plus ORM 框架 |
| mysql-connector-java | 5.1.49 | MySQL JDBC 驱动 |
| lombok | Spring Boot 管理 | 编译期代码生成（getter/setter等） |
| springfox-swagger2 | 2.9.2 | API 文档生成 |
| springfox-swagger-ui | 2.9.2 | API 文档 UI 界面 |
| fastjson | 2.0.43 | JSON 序列化/反序列化 |
| spring-boot-starter-validation | Spring Boot 管理 | 参数校验框架 |
| spring-boot-starter-test | Spring Boot 管理 | 测试框架 |
| mybatis-plus-generator | 3.5.5 (provided) | MyBatis-Plus 代码生成器 |
| velocity-engine-core | 2.3 (provided) | 代码生成器模板引擎 |

### 5.2 模块间依赖关系

```
MeetingController ──▶ IMeetingService ──▶ MeetingServiceImpl
                                              ├──▶ MeetingMapper
                                              ├──▶ ParticipantMapper
                                              └──▶ MeetingRoomUsageMapper

MeetingRoomController ──▶ IMeetingRoomService ──▶ MeetingRoomServiceImpl
                                                        ├──▶ MeetingRoomMapper
                                                        └──▶ MeetingRoomUsageMapper

ParticipantController ──▶ IParticipantService ──▶ ParticipantServiceImpl
                                                        └──▶ ParticipantMapper

StatisticsController ──▶ IStatisticsService ──▶ StatisticsServiceImpl
                                                      ├──▶ MeetingMapper
                                                      ├──▶ MeetingRoomMapper
                                                      ├──▶ MeetingRoomUsageMapper
                                                      └──▶ ParticipantMapper
```

### 5.3 关键设计特点

- **逻辑删除**：所有表均使用 `deleted` 字段实现逻辑删除，MyBatis-Plus 全局配置自动过滤已删除数据
- **分页查询**：通过 MyBatis-Plus `PaginationInnerInterceptor` 实现物理分页
- **事务管理**：删除操作使用 `@Transactional(rollbackFor = Exception.class)` 保证级联删除的原子性
- **下划线转驼峰**：MyBatis-Plus 配置 `map-underscore-to-camel-case: true`，自动映射数据库下划线字段到 Java 驼峰属性
- **统一响应**：所有接口返回 `Result<T>` 格式，包含 code、message、data 三个字段

---

## 6. API 接口总览

### 6.1 会议管理 `/api/meeting`

| 方法 | 路径 | 请求体/参数 | 响应类型 |
|------|------|-------------|----------|
| POST | `/add` | Meeting JSON | `Result<Meeting>` |
| GET | `/list` | MeetingQueryDTO 查询参数 | `Result<IPage<Meeting>>` |
| GET | `/{id}` | 路径参数 id | `Result<Meeting>` |
| PUT | `/update` | Meeting JSON | `Result<Meeting>` |
| DELETE | `/delete/{id}` | 路径参数 id | `Result<Boolean>` |
| GET | `/query` | MeetingQueryDTO 查询参数 | `Result<IPage<Meeting>>` |

### 6.2 会议室管理 `/api/meeting-room`

| 方法 | 路径 | 请求体/参数 | 响应类型 |
|------|------|-------------|----------|
| POST | `/add` | MeetingRoom JSON | `Result<MeetingRoom>` |
| GET | `/list` | roomCode, roomName, status, pageNum, pageSize | `Result<IPage<MeetingRoom>>` |
| GET | `/{id}` | 路径参数 id | `Result<MeetingRoom>` |
| PUT | `/update` | MeetingRoom JSON | `Result<MeetingRoom>` |
| DELETE | `/delete/{id}` | 路径参数 id | `Result<Boolean>` |
| POST | `/check-conflict` | ConflictCheckDTO JSON | `Result<Boolean>` |

### 6.3 参会人员管理 `/api/participant`

| 方法 | 路径 | 请求体/参数 | 响应类型 |
|------|------|-------------|----------|
| POST | `/add` | Participant JSON | `Result<Participant>` |
| GET | `/list` | ParticipantQueryDTO 查询参数 | `Result<IPage<Participant>>` |
| GET | `/{id}` | 路径参数 id | `Result<Participant>` |
| PUT | `/update` | Participant JSON | `Result<Participant>` |
| DELETE | `/delete/{id}` | 路径参数 id | `Result<Boolean>` |
| GET | `/query` | ParticipantQueryDTO 查询参数 | `Result<IPage<Participant>>` |

### 6.4 统计查询 `/api/statistics`

| 方法 | 路径 | 请求参数 | 响应类型 |
|------|------|----------|----------|
| GET | `/meeting` | department, status | `Result<Map<String, Object>>` |
| GET | `/room-usage` | meetingRoomId, startDate, endDate, type | `Result<Map<String, Object>>` |
| GET | `/attendance-rate` | meetingId | `Result<Map<String, Object>>` |

---

## 7. 项目运行方式

### 7.1 环境要求

- **JDK**：1.8+
- **Maven**：3.6+
- **MySQL**：5.5+（推荐 5.7）
- **IDE**：IntelliJ IDEA（推荐）或 Eclipse

### 7.2 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本（创建数据库、建表、插入测试数据）
source sql/init.sql;
```

如需重置数据，可执行：

```bash
source restore_data.sql;
```

### 7.3 配置修改

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/meeting_db?useUnicode=true&characterEncoding=utf8&useOldDriver=true&allowPublicKeyRetrieval=true
    username: root
    password: 123456   # 修改为实际密码
```

### 7.4 编译与运行

**方式一：Maven 命令行**

```bash
# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/meeting-management-1.0.0.jar
```

**方式二：IDE 直接运行**

在 IntelliJ IDEA 中打开项目，右键运行 `MeetingApplication.java` 的 `main` 方法。

**方式三：使用 run.bat**

双击项目根目录下的 `run.bat`（需要已安装 Maven 或 Maven Wrapper）。

### 7.5 验证运行

- 服务端口：`8080`
- Swagger 文档：`http://localhost:8080/swagger-ui.html`
- API 前缀：`/api/*`

---

## 8. CI/CD 流程

项目使用 GitHub Actions 进行持续集成/部署，配置文件为 `.github/workflows/maven.yml`。

### 8.1 构建流程（build job）

1. **触发条件**：push 或 PR 到 `main` 分支
2. **运行环境**：`ubuntu-latest`
3. **服务容器**：MySQL 5.7（用于集成测试）
4. **步骤**：
   - Checkout 代码
   - 配置 JDK 1.8
   - 缓存 Maven 依赖
   - 执行 `mvn clean package -DskipTests` 构建
   - 初始化 MySQL 数据库
   - 执行 `mvn test` 运行测试
   - 上传构建产物（JAR 文件，保留 7 天）

### 8.2 Docker 流程（docker job）

1. **依赖**：build job 成功后执行
2. **步骤**：
   - 配置 Docker Buildx
   - 登录 Docker Hub（使用 Secrets 凭证）
   - 构建 Docker 镜像
   - 推送到 Docker Hub

---

## 9. 项目文件索引

| 文件路径 | 说明 |
|----------|------|
| `pom.xml` | Maven 项目配置 |
| `src/main/resources/application.yml` | Spring Boot 应用配置 |
| `src/main/java/com/meeting/MeetingApplication.java` | 应用启动入口 |
| `sql/init.sql` | 数据库初始化脚本 |
| `restore_data.sql` | 数据重置脚本 |
| `run.bat` | Windows 快速启动脚本 |
| `.github/workflows/maven.yml` | CI/CD 配置 |
| `.gitignore` | Git 忽略规则 |
| `src/main/resources/mapper/*.xml` | MyBatis SQL 映射文件 |
