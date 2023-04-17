package com.sean.chatgpt;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sean.chatgpt.api.ApiInterface;
import com.sean.chatgpt.enums.ChatGptModelEnum;
import com.sean.chatgpt.exception.ChatException;
import com.sean.chatgpt.pojo.chat.ChatCompletionRequest;
import com.sean.chatgpt.pojo.chat.ChatCompletionResponse;
import com.sean.chatgpt.pojo.chat.ErrorResponse;
import com.sean.chatgpt.pojo.chat.Message;
import okhttp3.*;

import java.io.IOException;
import java.net.Proxy;
import java.util.Arrays;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * ChatGpt聊天
 *
 * @author zengyusheng
 * @date 2023/04/10 09:39
 */
public class ChatGpt {

    private static final Logger log = LogManager.getLogger(ChatGpt.class);

    /**
     * key
     */
    private String apiKey;
    /**
     * api host
     */
    private String apiHost;
    /**
     * api方法
     */
    private String apiMethod;

    private OkHttpClient okHttpClient;
    /**
     * 超时 默认300
     */
    private long timeout;
    /**
     * okhttp 代理
     */
    private Proxy proxy;


    public ChatGpt(ChatGptBuilder builder) {
        this.apiKey = builder.apiKey;
        this.apiHost = builder.apiHost;
        this.apiMethod = builder.apiMethod;
        this.timeout = builder.timeout;
        this.proxy = builder.proxy;
        this.okHttpClient = builder.okHttpClient;
    }

    /**
     * chatgpt 3.5 chat
     *
     * @param message
     * @return
     */
    public String chat(String message) {
        ChatCompletionRequest chatCompletion = new ChatCompletionRequest.ChatCompletionBuilder()
                .setModel(ChatGptModelEnum.GPT_3_5_TURBO.getName())
                .setMessages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = null;
        response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public String chat(ChatCompletionRequest chatCompletion) {
        ChatCompletionResponse response = null;
        response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public static class ChatGptBuilder {
        private String apiKey;
        private String apiHost = ApiInterface.DEFAULT_API_HOST;
        private String apiMethod = ApiInterface.CHAT_COMPLETION;
        private OkHttpClient okHttpClient;
        private long timeout = 300;
        private Proxy proxy = Proxy.NO_PROXY;

        public ChatGpt build() {
            if (null == okHttpClient) {
                okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .proxy(proxy)
                        .build();
            }
            return new ChatGpt(this);
        }

        public ChatGptBuilder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public ChatGptBuilder setApiHost(String apiHost) {
            this.apiHost = apiHost;
            return this;
        }

        public ChatGptBuilder setApiMethod(String apiMethod) {
            this.apiMethod = apiMethod;
            return this;
        }

        public ChatGptBuilder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public ChatGptBuilder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public ChatGptBuilder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }
    }


    private ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletion) {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(chatCompletion);
        } catch (JsonProcessingException e) {
            throw new ChatException("对象转json字符串失败！", e);
        }
        Request request = new Request.Builder()
                .url(apiHost + apiMethod)
                .post(RequestBody.create(requestBody, MediaType.parse(ContentType.JSON.getValue())))
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new ChatException("接口调用失败!", e);
        }
        ResponseBody responseBody = response.body();
        if (!response.isSuccessful()) {
            ErrorResponse errorResponse = null;
            try {
                errorResponse = mapper.readValue(responseBody.string(), ErrorResponse.class);
            } catch (IOException e) {
                throw new ChatException("xiangyresponseBody转String失败", e);
            }
            throw new ChatException(errorResponse.getError().getMessage());
        }
        ChatCompletionResponse chatCompletionResponse = null;
        try {
            chatCompletionResponse = mapper.readValue(responseBody.string(), ChatCompletionResponse.class);
        } catch (IOException e) {
            throw new ChatException("responseBody转String失败", e);
        }
        responseBody.close();
        return chatCompletionResponse;
    }


}

