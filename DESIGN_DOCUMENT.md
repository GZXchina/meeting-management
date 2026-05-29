# 会议管理系统（后台）设计文档

## 一、系统概述

### 1.1 项目简介
本系统为纯后台管理系统，采用Spring Boot + MyBatis-Plus框架开发，专注于会议、人员及会议室数据的存储、管理与统计分析。系统通过RESTful API接口提供服务，支持管理员通过指令调用完成各项管理功能。

### 1.2 技术栈
| 层次 | 技术选型 | 说明 |
|------|---------|------|
| 基础框架 | Spring Boot 2.7.x | 应用框架 |
| ORM框架 | MyBatis-Plus 3.5.x | 简化数据库操作 |
| 数据库 | MySQL 5.5 | 关系型数据库 |
| 构建工具 | Maven | 依赖管理 |
| JDK版本 | JDK 8+ | Java运行环境 |
| API文档 | Swagger2 | 接口文档生成 |

### 1.3 项目目录结构
```
Meeting-Management/
├── pom.xml                          # Maven依赖配置
├── src/
│   └── main/
│       ├── java/com/meeting/
│       │   ├── MeetingApplication.java    # 启动类
│       │   ├── config/                    # 配置类
│       │   │   └── MyBatisPlusConfig.java
│       │   ├── entity/                    # 实体类
│       │   │   ├── Meeting.java
│       │   │   ├── Participant.java
│       │   │   ├── MeetingRoom.java
│       │   │   └── MeetingRoomUsage.java
│       │   ├── mapper/                    # Mapper接口
│       │   │   ├── MeetingMapper.java
│       │   │   ├── ParticipantMapper.java
│       │   │   ├── MeetingRoomMapper.java
│       │   │   └── MeetingRoomUsageMapper.java
│       │   ├── service/                   # 业务接口
│       │   │   ├── IMeetingService.java
│       │   │   ├── IParticipantService.java
│       │   │   ├── IMeetingRoomService.java
│       │   │   └── IStatisticsService.java
│       │   ├── service/impl/             # 业务实现
│       │   │   ├── MeetingServiceImpl.java
│       │   │   ├── ParticipantServiceImpl.java
│       │   │   ├── MeetingRoomServiceImpl.java
│       │   │   └── StatisticsServiceImpl.java
│       │   ├── controller/              # 控制层
│       │   │   ├── MeetingController.java
│       │   │   ├── ParticipantController.java
│       │   │   ├── MeetingRoomController.java
│       │   │   └── StatisticsController.java
│       │   ├── common/                  # 通用类
│       │   │   ├── Result.java          # 统一返回结果
│       │   │   └── Constants.java       # 常量定义
│       │   └── dto/                     # 数据传输对象
│       │       ├── MeetingQueryDTO.java
│       │       ├── ParticipantQueryDTO.java
│       │       └── StatisticsDTO.java
│       └── resources/
│           ├── application.yml          # 应用配置
│           └── mapper/                  # Mapper XML文件
│               ├── MeetingMapper.xml
│               ├── ParticipantMapper.xml
│               └── MeetingRoomMapper.xml
└── README.md
```

---

## 二、数据库设计

### 2.1 ER图设计思路

本系统包含4个核心实体：
- **会议(Meeting)**：核心业务实体
- **参会人员(Participant)**：与会议多对一关联
- **会议室(MeetingRoom)**：独立实体
- **会议室占用记录(MeetingRoomUsage)**：会议室与会议的关联表

**实体关系说明：**
```
会议 (1) ────── (N) 参会人员
会议 (1) ────── (N) 会议室占用记录 ────── (1) 会议室
```

### 2.2 数据库表结构

#### 2.2.1 会议信息表 (meeting)

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键ID |
| meeting_code | VARCHAR(50) | UNIQUE, NOT NULL | 会议编号 |
| title | VARCHAR(200) | NOT NULL | 会议主题 |
| start_time | DATETIME | NOT NULL | 开始时间 |
| end_time | DATETIME | NOT NULL | 结束时间 |
| location | VARCHAR(200) | | 会议地点 |
| department | VARCHAR(100) | | 主办部门 |
| max_participants | INT | DEFAULT 100 | 参会人数上限 |
| status | TINYINT | DEFAULT 0 | 会议状态(0-待召开/1-进行中/2-已结束) |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| deleted | TINYINT | DEFAULT 0 | 逻辑删除标记 |

#### 2.2.2 参会人员表 (participant)

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键ID |
| user_id | VARCHAR(50) | NOT NULL | 用户ID |
| name | VARCHAR(100) | NOT NULL | 姓名 |
| department | VARCHAR(100) | | 部门 |
| phone | VARCHAR(20) | | 联系方式 |
| meeting_id | BIGINT | FK, NOT NULL | 关联会议ID |
| status | TINYINT | DEFAULT 0 | 参会状态(0-已报名/1-未签到/2-已参会) |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| deleted | TINYINT | DEFAULT 0 | 逻辑删除标记 |

#### 2.2.3 会议室表 (meeting_room)

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键ID |
| room_code | VARCHAR(50) | UNIQUE, NOT NULL | 会议室编号 |
| room_name | VARCHAR(100) | NOT NULL | 会议室名称 |
| capacity | INT | NOT NULL | 容量 |
| equipment | VARCHAR(500) | | 设备清单(JSON格式) |
| status | TINYINT | DEFAULT 0 | 使用状态(0-空闲/1-占用) |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME | ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| deleted | TINYINT | DEFAULT 0 | 逻辑删除标记 |

#### 2.2.4 会议室占用记录表 (meeting_room_usage)

| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键ID |
| meeting_id | BIGINT | FK, NOT NULL | 关联会议ID |
| meeting_room_id | BIGINT | FK, NOT NULL | 关联会议室ID |
| start_time | DATETIME | NOT NULL | 占用开始时间 |
| end_time | DATETIME | NOT NULL | 占用结束时间 |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| deleted | TINYINT | DEFAULT 0 | 逻辑删除标记 |

### 2.3 索引设计

| 表名 | 索引字段 | 索引类型 | 说明 |
|------|----------|----------|------|
| meeting | meeting_code | UNIQUE | 会议编号唯一索引 |
| meeting | start_time, department | INDEX | 按时间和部门查询 |
| meeting | status | INDEX | 按状态查询 |
| participant | meeting_id | INDEX | 关联会议查询 |
| participant | user_id, meeting_id | UNIQUE | 防止重复报名 |
| meeting_room | room_code | UNIQUE | 会议室编号唯一索引 |
| meeting_room_usage | meeting_room_id, start_time, end_time | INDEX | 冲突检测查询 |

---

## 三、API接口设计

### 3.1 会议管理接口

| 接口路径 | 请求方式 | 说明 |
|----------|----------|------|
| /api/meeting/add | POST | 添加会议 |
| /api/meeting/list | GET | 查询会议列表(分页) |
| /api/meeting/{id} | GET | 获取会议详情 |
| /api/meeting/update | PUT | 修改会议信息 |
| /api/meeting/delete/{id} | DELETE | 删除会议 |
| /api/meeting/query | GET | 条件查询会议 |

### 3.2 参会人员管理接口

| 接口路径 | 请求方式 | 说明 |
|----------|----------|------|
| /api/participant/add | POST | 添加参会人员 |
| /api/participant/list | GET | 查询参会人员列表 |
| /api/participant/update | PUT | 修改参会人员状态 |
| /api/participant/delete/{id} | DELETE | 删除参会人员 |
| /api/participant/query | GET | 条件查询参会人员 |

### 3.3 会议室管理接口

| 接口路径 | 请求方式 | 说明 |
|----------|----------|------|
| /api/meeting-room/add | POST | 添加会议室 |
| /api/meeting-room/list | GET | 查询会议室列表 |
| /api/meeting-room/update | PUT | 修改会议室信息 |
| /api/meeting-room/delete/{id} | DELETE | 删除会议室 |
| /api/meeting-room/check-conflict | POST | 检测时间冲突 |

### 3.4 统计查询接口

| 接口路径 | 请求方式 | 说明 |
|----------|----------|------|
| /api/statistics/meeting | GET | 会议统计 |
| /api/statistics/room-usage | GET | 会议室使用统计 |
| /api/statistics/attendance-rate | GET | 参会率统计 |

---

## 四、核心业务逻辑

### 4.1 会议状态流转

```
[待召开(0)] ──→ [进行中(1)] ──→ [已结束(2)]
     │              │              │
     └──手动修改─────┴──自动定时任务──┘
```

### 4.2 参会状态流转

```
[已报名(0)] → [未签到(1)] → [已参会(2)]
                │              │
                └─────修改─────┘
```

### 4.3 会议室冲突检测算法

```sql
-- 检测时间冲突的SQL逻辑
SELECT COUNT(*) FROM meeting_room_usage
WHERE meeting_room_id = ?
AND deleted = 0
AND (
    (start_time <= ? AND end_time > ?) OR
    (start_time < ? AND end_time >= ?) OR
    (start_time >= ? AND end_time <= ?)
)
```

### 4.4 删除会议级联处理

删除会议时，自动处理以下级联操作：
1. 删除关联的参会人员记录
2. 删除会议室占用记录
3. 释放会议室状态

---

## 五、配置文件说明

### 5.1 application.yml 主要配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/meeting_db
    username: root
    password: your_password

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mapper/*.xml
```

### 5.2 分页配置

采用MyBatis-Plus内置分页插件，配置PageHelper实现物理分页。

---

## 六、扩展性设计

### 6.1 预留扩展接口
- 邮件/短信通知接口
- 日志审计接口
- 数据导出接口

### 6.2 性能优化考虑
- 使用Redis缓存热点数据
- 异步处理统计计算
- 数据库读写分离

---

## 七、部署说明

### 7.1 环境要求
- JDK 1.8+
- MySQL 5.7+/8.0+
- Maven 3.6+

### 7.2 打包部署
```bash
mvn clean package
java -jar target/meeting-management-1.0.0.jar
```

### 7.3 API访问
启动后访问：http://localhost:8080/swagger-ui.html 查看API文档
