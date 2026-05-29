package com.meeting.common; // 通用工具类包

/**
 * 系统常量定义类
 * 集中管理会议状态、参会状态、会议室状态等枚举常量
 */
public class Constants {

    /**
     * 会议状态常量
     */
    public static class MeetingStatus {
        public static final int PENDING = 0;     // 待召开
        public static final int IN_PROGRESS = 1;  // 进行中
        public static final int ENDED = 2;        // 已结束
    }

    /**
     * 参会人员状态常量
     */
    public static class ParticipantStatus {
        public static final int REGISTERED = 0;   // 已报名
        public static final int NOT_SIGNED = 1;   // 未签到
        public static final int ATTENDED = 2;     // 已参会
    }

    /**
     * 会议室状态常量
     */
    public static class RoomStatus {
        public static final int FREE = 0;     // 空闲
        public static final int OCCUPIED = 1; // 占用
    }
}