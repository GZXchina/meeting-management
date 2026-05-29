# 会议管理系统（后台）项目演示说明

## 一、项目简介

本系统是基于Spring Boot + MyBatis-Plus开发的纯后台会议管理系统，提供会议、人员、会议室的管理功能以及统计分析接口。

**技术栈：**
- 后端框架：Spring Boot 2.7.18
- ORM框架：MyBatis-Plus 3.5.5
- 数据库：MySQL 5.5+
- API文档：Swagger2
- 构建工具：Maven

---

## 二、项目导入与运行

### 2.1 环境要求
- JDK 1.8+
- MySQL 5.5+
- Maven 3.6+

### 2.2 数据库初始化

1. 登录MySQL
```bash
mysql -u root -p
```

2. 执行初始化脚本
```sql
source c:/Users/DK/Desktop/Meeting-Mangement/sql/init.sql
```

或直接执行：
```bash
mysql -u root -p < c:/Users/DK/Desktop/Meeting-Mangement/sql/init.sql
```

### 2.3 配置数据库连接

修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/meeting_db?useUnicode=true&characterEncoding=utf8&useOldDriver=true&allowPublicKeyRetrieval=true
    username: root
    password: your_password  # 修改为你的MySQL密码
```

### 2.4 编译运行

在项目根目录下执行：
```bash
mvn clean compile
mvn spring-boot:run
```

或打包后运行：
```bash
mvn clean package
java -jar target/meeting-management-1.0.0.jar
```

### 2.5 访问API文档

启动成功后，访问 Swagger UI：
```
http://localhost:8080/swagger-ui.html
```

---

## 三、功能演示

### 3.1 会议管理

#### 添加会议
- **接口**: `POST /api/meeting/add`
- **示例请求**:
```json
{
    "meetingCode": "MT20260004",
    "title": "项目评审会议",
    "startTime": "2026-06-25 09:00:00",
    "endTime": "2026-06-25 12:00:00",
    "location": "第三会议室",
    "department": "技术研发部",
    "maxParticipants": 20,
    "status": 0
}
```

#### 查询会议列表
- **接口**: `GET /api/meeting/list?pageNum=1&pageSize=10`
- **条件查询**: `GET /api/meeting/query?department=技术研发部&status=0`

#### 修改会议
- **接口**: `PUT /api/meeting/update`
- **示例请求**:
```json
{
    "id": 1,
    "status": 1
}
```

#### 删除会议
- **接口**: `DELETE /api/meeting/delete/{id}`
- **说明**: 删除会议时会自动删除关联的参会人员和会议室占用记录

---

### 3.2 参会人员管理

#### 添加参会人员
- **接口**: `POST /api/participant/add`
- **示例请求**:
```json
{
    "userId": "U006",
    "name": "孙八",
    "department": "技术研发部",
    "phone": "13800138006",
    "meetingId": 1,
    "status": 0
}
```

#### 查询参会人员
- **接口**: `GET /api/participant/list?meetingId=1`
- **条件查询**: `GET /api/participant/query?department=技术研发部&status=2`

#### 修改参会状态
- **接口**: `PUT /api/participant/update`
- **示例请求**:
```json
{
    "id": 1,
    "status": 2
}
```

---

### 3.3 会议室管理

#### 添加会议室
- **接口**: `POST /api/meeting-room/add`
- **示例请求**:
```json
{
    "roomCode": "MR004",
    "roomName": "第四会议室",
    "capacity": 15,
    "equipment": "[\"投影仪\",\"白板\"]",
    "status": 0
}
```

#### 查询会议室
- **接口**: `GET /api/meeting-room/list?status=0`

#### 检测时间冲突
- **接口**: `POST /api/meeting-room/check-conflict`
- **示例请求**:
```json
{
    "meetingRoomId": 1,
    "startTime": "2026-06-15 10:00:00",
    "endTime": "2026-06-15 11:00:00"
}
```
- **返回**: 如果有冲突返回错误信息，无冲突返回 `{"code":200,"message":"success","data":false}`

---

### 3.4 统计查询

#### 会议统计
- **接口**: `GET /api/statistics/meeting?department=技术研发部`
- **返回示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "totalCount": 10,
        "pendingCount": 5,
        "inProgressCount": 2,
        "endedCount": 3
    }
}
```

#### 会议室使用统计
- **接口**: `GET /api/statistics/room-usage?startDate=2026-06-01&endDate=2026-06-30`
- **返回示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "totalUsageCount": 15,
        "totalUsageMinutes": 2400,
        "averageUsageMinutes": 160,
        "dailyCount": {
            "2026-06-15": 3,
            "2026-06-20": 2
        }
    }
}
```

#### 参会率统计
- **接口**: `GET /api/statistics/attendance-rate?meetingId=1`
- **返回示例**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "meetingId": 1,
        "meetingTitle": "年度总结会议",
        "maxParticipants": 50,
        "registeredCount": 3,
        "attendedCount": 2,
        "attendanceRate": 66.67
    }
}
```

---

## 四、接口状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 500 | 服务器内部错误 |

---

## 五、数据字典

### 会议状态 (meeting.status)
| 值 | 说明 |
|----|------|
| 0 | 待召开 |
| 1 | 进行中 |
| 2 | 已结束 |

### 参会状态 (participant.status)
| 值 | 说明 |
|----|------|
| 0 | 已报名 |
| 1 | 未签到 |
| 2 | 已参会 |

### 会议室状态 (meeting_room.status)
| 值 | 说明 |
|----|------|
| 0 | 空闲 |
| 1 | 占用 |

---

## 六、项目结构

```
Meeting-Management/
├── pom.xml                           # Maven依赖配置
├── sql/
│   └── init.sql                      # 数据库初始化脚本
├── src/main/java/com/meeting/
│   ├── MeetingApplication.java       # 启动类
│   ├── config/
│   │   ├── MyBatisPlusConfig.java    # MyBatis-Plus配置
│   │   └── Swagger2Config.java      # Swagger配置
│   ├── entity/                       # 实体类
│   │   ├── Meeting.java
│   │   ├── Participant.java
│   │   ├── MeetingRoom.java
│   │   └── MeetingRoomUsage.java
│   ├── mapper/                       # Mapper接口
│   ├── service/                       # 业务接口
│   ├── service/impl/                 # 业务实现
│   ├── controller/                   # 控制层
│   ├── common/                       # 通用类
│   └── dto/                          # 数据传输对象
└── src/main/resources/
    ├── application.yml               # 应用配置
    └── mapper/                       # Mapper XML文件
```

---

## 七、常见问题

### Q1: 启动报错"Communications link failure"
检查MySQL服务是否启动，或数据库连接配置是否正确。

### Q2: Swagger页面无法访问
确认已正确配置Swagger2Config.java，且springfox.documentation.enabled=true

### Q3: 分页不生效
确保引入了MybatisPlusInterceptor分页插件配置。

---

## 八、联系方式

如有问题，请联系系统管理员。
