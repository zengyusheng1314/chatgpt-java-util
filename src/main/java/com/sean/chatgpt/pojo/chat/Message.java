package com.sean.chatgpt.pojo.chat;

/**
 * message
 * 其中每个对象都有一个角色(“系统”、“用户”或“助手”)和内容(消息的内容)。
 *
 * @author zengyusheng
 * @date 2023/04/09
 */
public class Message {

    private String role;
    private String content;

    public static Message of(String content) {

        return new Message(Role.USER.getValue(), content);
    }

    public static Message ofSystem(String content) {

        return new Message(Role.SYSTEM.getValue(), content);
    }

    public static Message ofAssistant(String content) {

        return new Message(Role.ASSISTANT.getValue(), content);
    }

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public enum Role {

        /**
         * 系统
         */
        SYSTEM("system"),
        /**
         * 用户
         */
        USER("user"),
        ASSISTANT("assistant"),
        ;

        /**
         * 助手
         */
        private String value;

        /**
         * 角色(“系统”、“用户”或“助手”)
         *
         * @param value
         */
        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
