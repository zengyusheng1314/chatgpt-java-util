package com.sean.chatgpt.exception;

/**
 * chatGpt调用异常类
 *
 * @author zengyusheng
 * @date 2023/04/10 08:48
 */
public class ChatException extends RuntimeException {

    public ChatException(String message) {
        super(message);
    }

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

}
