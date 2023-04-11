package com.sean.chatgpt.pojo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * chat 内容
 *
 * @author zengyusheng
 * @date 2023/04/10 09:23
 */
public class ChatChoice {

    private long index;
    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    private Message delta;
    /**
     * 请求参数stream为false返回是message
     */
    @JsonProperty("message")
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Message getDelta() {
        return delta;
    }

    public void setDelta(Message delta) {
        this.delta = delta;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
