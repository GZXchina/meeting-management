# Postman 接口测试指南

## 一、环境准备

### 1. 数据库配置
```bash
# 进入 MySQL 命令行
mysql -u root -p123456

# 执行初始化脚本
source sql/init.sql
```

### 2. 启动项目
```bash
# 方式一：Maven
cd c:\Users\DK\Desktop\Meeting-Mangement
mvn spring-boot:run

# 方式二：IDE
运行 MeetingApplication.java
```

**服务地址**: `http://localhost:8080`

---

## 二、Postman 配置

### 2.1 创建环境变量
1. 打开 Postman → 点击右上角 "No Environment" → "Add"
2. 环境名称: `Meeting-Management`
3. 添加变量:

| 变量名 | 值 | 描述 |
|--------|-----|------|
| baseUrl | http://localhost:8080 | 服务器地址 |
| meetingId | 1 | 测试会议ID |
| roomId | 1 | 测试会议室ID |
| participantId | 1 | 测试参会人员ID |

### 2.2 设置 Headers
所有请求添加以下 Header：

| Key | Value |
|-----|-------|
| Content-Type | application/json |

---

## 三、API 测试用例

### 1. 会议管理

#### 1.1 添加会议
- **方法**: `POST`
- **URL**: `http://localhost:8080/api/meeting/add`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "meetingCode": "MT20260004",
    "title": "新项目启动会议",
    "startTime": "2026-07-15 09:00:00",
    "endTime": "2026-07-15 11:00:00",
    "location": "第二会议室",
    "department": "技术研发部",
    "maxParticipants": 20,
    "status": 0
}
```
- **预期响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 4,
        "meetingCode": "MT20260004",
        "title": "新项目启动会议",
        "status": 0
    }
}
```

#### 1.2 查询会议列表
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/meeting/list?pageNum=1&pageSize=10`
- **Headers**: 无需额外设置
- **参数**:
  | 参数 | 值 |
  |------|-----|
  | pageNum | 1 |
  | pageSize | 10 |
- **预期响应**: 返回会议列表分页数据

#### 1.3 获取会议详情
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/meeting/1`
- **Headers**: 无需额外设置
- **预期响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "meetingCode": "MT20260001",
        "title": "年度总结会议"
    }
}
```

#### 1.4 修改会议
- **方法**: `PUT`
- **URL**: `http://localhost:8080/api/meeting/update`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "id": 1,
    "meetingCode": "MT20260001",
    "title": "年度总结会议（更新）",
    "startTime": "2026-06-15 09:00:00",
    "endTime": "2026-06-15 12:00:00",
    "location": "第一会议室",
    "department": "综合管理部",
    "maxParticipants": 50,
    "status": 0
}
```

#### 1.5 删除会议
- **方法**: `DELETE`
- **URL**: `http://localhost:8080/api/meeting/delete/1`
- **Headers**: 无需额外设置
- **预期响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": true
}
```

---

### 2. 会议室管理

#### 2.1 添加会议室
- **方法**: `POST`
- **URL**: `http://localhost:8080/api/meeting-room/add`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "roomCode": "MR004",
    "roomName": "第四会议室",
    "capacity": 15,
    "equipment": "[\"电视\",\"白板\",\"电话\"]",
    "status": 0
}
```

#### 2.2 查询会议室列表
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/meeting-room/list?pageNum=1&pageSize=10`
- **参数**:
  | 参数 | 值 |
  |------|-----|
  | pageNum | 1 |
  | pageSize | 10 |

#### 2.3 获取会议室详情
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/meeting-room/1`

#### 2.4 修改会议室
- **方法**: `PUT`
- **URL**: `http://localhost:8080/api/meeting-room/update`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "id": 1,
    "roomCode": "MR001",
    "roomName": "第一会议室（更新）",
    "capacity": 50,
    "equipment": "[\"投影仪\",\"白板\",\"音响\",\"空调\"]",
    "status": 0
}
```

#### 2.5 删除会议室
- **方法**: `DELETE`
- **URL**: `http://localhost:8080/api/meeting-room/delete/1`

#### 2.6 检测时间冲突
- **方法**: `POST`
- **URL**: `http://localhost:8080/api/meeting-room/check-conflict`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "meetingRoomId": 1,
    "startTime": "2026-06-15 10:00:00",
    "endTime": "2026-06-15 11:00:00"
}
```

---

### 3. 参会人员管理

#### 3.1 添加参会人员
- **方法**: `POST`
- **URL**: `http://localhost:8080/api/participant/add`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "userId": "U006",
    "name": "孙八",
    "department": "市场部",
    "phone": "13800138006",
    "meetingId": 1,
    "status": 0
}
```

#### 3.2 查询参会人员列表
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/participant/list?pageNum=1&pageSize=10`
- **参数**:
  | 参数 | 值 |
  |------|-----|
  | pageNum | 1 |
  | pageSize | 10 |

#### 3.3 获取参会人员详情
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/participant/1`

#### 3.4 修改参会人员
- **方法**: `PUT`
- **URL**: `http://localhost:8080/api/participant/update`
- **Headers**: `Content-Type: application/json`
- **Body** (raw/JSON):
```json
{
    "id": 1,
    "userId": "U001",
    "name": "张三（更新）",
    "department": "综合管理部",
    "phone": "13800138001",
    "meetingId": 1,
    "status": 2
}
```

#### 3.5 删除参会人员
- **方法**: `DELETE`
- **URL**: `http://localhost:8080/api/participant/delete/1`

---

### 4. 统计查询

#### 4.1 会议统计
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/statistics/meeting`
- **可选参数**:
  | 参数 | 值 |
  |------|-----|
  | department | 综合管理部 |
  | status | 0 |

#### 4.2 会议室使用统计
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/statistics/room-usage`
- **可选参数**:
  | 参数 | 值 |
  |------|-----|
  | meetingRoomId | 1 |
  | startDate | 2026-06-01T00:00:00 |
  | endDate | 2026-06-30T23:59:59 |

#### 4.3 参会率统计
- **方法**: `GET`
- **URL**: `http://localhost:8080/api/statistics/attendance-rate?meetingId=1`

---

## 四、响应格式说明

### 成功响应
```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

### 错误响应
```json
{
    "code": 500,
    "message": "error message",
    "data": null
}
```

### 状态码说明
| 状态码 | 含义 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 五、测试流程

1. ✅ **启动数据库**: 确保 MySQL 服务运行，执行 `sql/init.sql`
2. ✅ **启动项目**: 运行 `mvn spring-boot:run` 或 IDE 启动
3. ✅ **创建 Postman 请求**: 选择方法，输入完整 URL
4. ✅ **设置 Headers**: 添加 `Content-Type: application/json`
5. ✅ **填写参数/Body**: 根据接口要求填写
6. ✅ **发送请求**: 点击 Send 按钮
7. ✅ **验证响应**: 检查 code=200 和 data 内容

---

## 六、快速测试清单

| 序号 | 测试项 | 方法 | URL | 预期结果 |
|------|--------|------|-----|----------|
| 1 | 添加会议 | POST | /api/meeting/add | 返回新会议数据 |
| 2 | 查询会议列表 | GET | /api/meeting/list | 返回分页列表 |
| 3 | 获取会议详情 | GET | /api/meeting/1 | 返回ID=1的会议 |
| 4 | 添加会议室 | POST | /api/meeting-room/add | 返回新会议室数据 |
| 5 | 添加参会人员 | POST | /api/participant/add | 返回新参会人员 |
| 6 | 会议统计 | GET | /api/statistics/meeting | 返回统计数据 |

---

## 七、注意事项

1. **URL 格式**: 确保 URL 正确，不要重复路径（如 `/api/meeting/add/add` 是错误的）
2. **Content-Type**: POST/PUT 请求必须设置 `Content-Type: application/json`
3. **日期格式**: 时间字段使用 `yyyy-MM-dd HH:mm:ss` 格式
4. **参数类型**: 数字类型不要加引号，字符串类型必须加引号
5. **服务器状态**: 确保项目已启动，端口为 8080