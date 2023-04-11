package com.sean.chatgpt.pojo.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sean.chatgpt.exception.ChatException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Open AI chat/completions Request body
 *
 * @author zengyusheng
 * @date 2023/04/09 20:00
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionRequest implements Serializable {

    /**
     * 使用的模型的ID，必填项。
     */
    private String model;

    /**
     * messages参数。消息必须是消息对象的数组，必填项。
     */
    private List<Message> messages;

    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守，默认值为1，推荐0-1之间。
     * <p>
     * 不要同时改这个和topP
     */

    private double temperature;

    /**
     * 一种温度采样的替代方法，默认值为1。
     * 0-1
     * <p>
     * 不要同时改这个和temperature
     */
    @JsonProperty("top_p")
    private double topP;

    /**
     * 结果数，默认值为1
     */
    private Integer n;

    /**
     * 是否流式输出.
     * 默认值为false
     */
    private boolean stream;
    /**
     * 停用词，默认值为null
     */
    private List<String> stop;
    /**
     * 3.5 最大支持4096
     * 4.0 最大32k
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 默认值为0,介于-2.0和2.0之间的数字。
     */
    @JsonProperty("presence_penalty")
    private double presencePenalty;

    /**
     * 默认值为0,介于-2.0和2.0之间的数字。
     */
    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 代表终端用户的唯一标识符，可以帮助OpenAI监视和检测滥用。
     */
    private String user;

    public ChatCompletionRequest(ChatCompletionBuilder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.temperature = builder.temperature;
        this.topP = builder.topP;
        this.n = builder.n;
        this.stream = builder.stream;
        this.stop = builder.stop;
        this.maxTokens = builder.maxTokens;
        this.presencePenalty = builder.presencePenalty;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.logitBias = builder.logitBias;
        this.user = builder.user;
    }

    public static class ChatCompletionBuilder {

        private static final double DEFAULT_TEMPERATURE = 1;

        private static final double DEFAULT_TOP_P = 1;

        private static final int DEFAULT_N = 1;

        private static final boolean DEFAULT_STREAM = false;

        private String model;
        private List<Message> messages;
        private double temperature = DEFAULT_TEMPERATURE;
        private double topP = DEFAULT_TOP_P;
        private Integer n =DEFAULT_N;
        private boolean stream =DEFAULT_STREAM;
        private List<String> stop;
        private Integer maxTokens;
        private double presencePenalty;
        private double frequencyPenalty;
        private Map logitBias;
        private String user;

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(this);
        }

        public ChatCompletionBuilder setModel(String model) {
            if (null == model) {
                throw new ChatException("model 参数不能为空");
            }
            this.model = model;
            return this;
        }

        public ChatCompletionBuilder setMessages(List<Message> messages) {
            if (null == messages) {
                throw new ChatException("messages 参数不能为空");
            }
            this.messages = messages;
            return this;
        }

        public ChatCompletionBuilder setTemperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public ChatCompletionBuilder setTopP(double topP) {
            this.topP = topP;
            return this;
        }

        public ChatCompletionBuilder setN(Integer n) {
            this.n = n;
            return this;
        }

        public ChatCompletionBuilder setStream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public ChatCompletionBuilder setStop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public ChatCompletionBuilder setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public ChatCompletionBuilder setPresencePenalty(double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public ChatCompletionBuilder setFrequencyPenalty(double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public ChatCompletionBuilder setLogitBias(Map logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public ChatCompletionBuilder setUser(String user) {
            this.user = user;
            return this;
        }

    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTopP() {
        return topP;
    }

    public Integer getN() {
        return n;
    }

    public boolean isStream() {
        return stream;
    }

    public List<String> getStop() {
        return stop;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public double getPresencePenalty() {
        return presencePenalty;
    }

    public double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public Map getLogitBias() {
        return logitBias;
    }

    public String getUser() {
        return user;
    }
}
