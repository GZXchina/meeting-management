-- ============================================
-- 会议管理系统 - 恢复初始数据脚本
-- 执行前请确保已连接到 MySQL 数据库
-- ============================================

USE meeting_db;

-- 清空所有表（注意顺序：子表先清空）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE meeting_room_usage;
TRUNCATE TABLE participant;
TRUNCATE TABLE meeting;
TRUNCATE TABLE meeting_room;
SET FOREIGN_KEY_CHECKS = 1;

-- 插入测试会议室
INSERT INTO meeting_room (room_code, room_name, capacity, equipment, status) VALUES
('MR001', '第一会议室', 50, '["投影仪","白板","音响","空调"]', 0),
('MR002', '第二会议室', 30, '["投影仪","白板","电视"]', 0),
('MR003', '第三会议室', 20, '["电视","白板"]', 0);

-- 插入测试会议
INSERT INTO meeting (meeting_code, title, start_time, end_time, location, department, max_participants, status) VALUES
('MT20260001', '年度总结会议', '2026-06-15 09:00:00', '2026-06-15 12:00:00', '第一会议室', '综合管理部', 50, 0),
('MT20260002', '技术交流会', '2026-06-20 14:00:00', '2026-06-20 17:00:00', '第二会议室', '技术研发部', 30, 0),
('MT20260003', '产品发布会', '2026-07-01 10:00:00', '2026-07-01 16:00:00', '第一会议室', '市场部', 100, 0);

-- 插入测试参会人员
INSERT INTO participant (user_id, name, department, phone, meeting_id, status) VALUES
('U001', '张三', '综合管理部', '13800138001', 1, 2),
('U002', '李四', '技术研发部', '13800138002', 1, 2),
('U003', '王五', '市场部', '13800138003', 1, 1),
('U004', '赵六', '综合管理部', '13800138004', 2, 2),
('U005', '钱七', '技术研发部', '13800138005', 2, 2);

-- 插入会议室占用记录
INSERT INTO meeting_room_usage (meeting_id, meeting_room_id, start_time, end_time) VALUES
(1, 1, '2026-06-15 09:00:00', '2026-06-15 12:00:00'),
(2, 2, '2026-06-20 14:00:00', '2026-06-20 17:00:00'),
(3, 1, '2026-07-01 10:00:00', '2026-07-01 16:00:00');

-- 验证数据
SELECT '会议室数据:' AS info;
SELECT * FROM meeting_room;

SELECT '会议数据:' AS info;
SELECT * FROM meeting;

SELECT '参会人员数据:' AS info;
SELECT * FROM participant;

SELECT '会议室占用记录:' AS info;
SELECT * FROM meeting_room_usage;

SELECT '数据恢复完成！' AS result;
