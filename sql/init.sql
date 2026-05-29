-- 会议管理系统数据库初始化脚本
-- 适用于 MySQL 5.5+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS meeting_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE meeting_db;

-- 1. 会议信息表
DROP TABLE IF EXISTS meeting;
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    meeting_code VARCHAR(50) NOT NULL UNIQUE COMMENT '会议编号',
    title VARCHAR(200) NOT NULL COMMENT '会议主题',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    location VARCHAR(200) COMMENT '会议地点',
    department VARCHAR(100) COMMENT '主办部门',
    max_participants INT DEFAULT 100 COMMENT '参会人数上限',
    status TINYINT DEFAULT 0 COMMENT '会议状态(0-待召开/1-进行中/2-已结束)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_meeting_code (meeting_code),
    INDEX idx_start_time (start_time),
    INDEX idx_department (department),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议信息表';

-- 2. 参会人员表
DROP TABLE IF EXISTS participant;
CREATE TABLE participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    department VARCHAR(100) COMMENT '部门',
    phone VARCHAR(20) COMMENT '联系方式',
    meeting_id BIGINT NOT NULL COMMENT '关联会议ID',
    status TINYINT DEFAULT 0 COMMENT '参会状态(0-已报名/1-未签到/2-已参会)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_meeting_id (meeting_id),
    UNIQUE KEY uk_user_meeting (user_id, meeting_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参会人员表';

-- 3. 会议室表
DROP TABLE IF EXISTS meeting_room;
CREATE TABLE meeting_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    room_code VARCHAR(50) NOT NULL UNIQUE COMMENT '会议室编号',
    room_name VARCHAR(100) NOT NULL COMMENT '会议室名称',
    capacity INT NOT NULL COMMENT '容量',
    equipment VARCHAR(500) COMMENT '设备清单(JSON格式)',
    status TINYINT DEFAULT 0 COMMENT '使用状态(0-空闲/1-占用)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_room_code (room_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- 4. 会议室占用记录表
DROP TABLE IF EXISTS meeting_room_usage;
CREATE TABLE meeting_room_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    meeting_id BIGINT NOT NULL COMMENT '关联会议ID',
    meeting_room_id BIGINT NOT NULL COMMENT '关联会议室ID',
    start_time DATETIME NOT NULL COMMENT '占用开始时间',
    end_time DATETIME NOT NULL COMMENT '占用结束时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_meeting_room_id (meeting_room_id),
    INDEX idx_meeting_id (meeting_id),
    INDEX idx_time_range (meeting_room_id, start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室占用记录表';

-- 插入测试数据
-- 测试会议室
INSERT INTO meeting_room (room_code, room_name, capacity, equipment, status) VALUES
('MR001', '第一会议室', 50, '["投影仪","白板","音响","空调"]', 0),
('MR002', '第二会议室', 30, '["投影仪","白板","电视"]', 0),
('MR003', '第三会议室', 20, '["电视","白板"]', 0);

-- 测试会议
INSERT INTO meeting (meeting_code, title, start_time, end_time, location, department, max_participants, status) VALUES
('MT20260001', '年度总结会议', '2026-06-15 09:00:00', '2026-06-15 12:00:00', '第一会议室', '综合管理部', 50, 0),
('MT20260002', '技术交流会', '2026-06-20 14:00:00', '2026-06-20 17:00:00', '第二会议室', '技术研发部', 30, 0),
('MT20260003', '产品发布会', '2026-07-01 10:00:00', '2026-07-01 16:00:00', '第一会议室', '市场部', 100, 0);

-- 测试参会人员
INSERT INTO participant (user_id, name, department, phone, meeting_id, status) VALUES
('U001', '张三', '综合管理部', '13800138001', 1, 2),
('U002', '李四', '技术研发部', '13800138002', 1, 2),
('U003', '王五', '市场部', '13800138003', 1, 1),
('U004', '赵六', '综合管理部', '13800138004', 2, 2),
('U005', '钱七', '技术研发部', '13800138005', 2, 2);

-- 会议室占用记录
INSERT INTO meeting_room_usage (meeting_id, meeting_room_id, start_time, end_time) VALUES
(1, 1, '2026-06-15 09:00:00', '2026-06-15 12:00:00'),
(2, 2, '2026-06-20 14:00:00', '2026-06-20 17:00:00'),
(3, 1, '2026-07-01 10:00:00', '2026-07-01 16:00:00');
