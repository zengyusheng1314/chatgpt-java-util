package com.sean.chatgpt;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sean.chatgpt.api.ApiInterface;
import com.sean.chatgpt.enums.ChatGptModelEnum;
import com.sean.chatgpt.exception.ChatException;
import com.sean.chatgpt.pojo.chat.ChatCompletionRequest;
import com.sean.chatgpt.pojo.chat.ChatCompletionResponse;
import com.sean.chatgpt.pojo.chat.Message;
import okhttp3.*;
import okhttp3.internal.Util;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import okio.BufferedSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ChatGpt聊天
 *
 * @author zengyusheng
 * @date 2023/04/10 09:39
 */
public class ChatGptStream {

    private static final Logger log = LogManager.getLogger(ChatGptStream.class);

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

    public void streamChatCompletion(ChatCompletionRequest chatCompletion, EventSourceListener eventSourceListener) {
        chatCompletion.setStream(true);
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(new ObjectMapper().writeValueAsString(chatCompletion), MediaType.parse(ContentType.JSON.getValue()));
        } catch (JsonProcessingException e) {
            throw new ChatException("json转换异常！");
        }
        EventSource.Factory factory = EventSources.createFactory(okHttpClient);
        Request request = new Request.Builder()
                .url(apiHost + apiMethod)
                .post(requestBody)
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .build();
        factory.newEventSource(request, eventSourceListener);

    }

    public void streamChatCompletion(List<Message> messages,
                                     EventSourceListener eventSourceListener) {
        ChatCompletionRequest chatCompletion = new ChatCompletionRequest.ChatCompletionBuilder()
                .setMessages(messages)
                .setStream(true)
                .build();
        streamChatCompletion(chatCompletion, eventSourceListener);
    }

    public ChatGptStream(ChatGptBuilder builder) {
        this.apiKey = builder.apiKey;
        this.apiHost = builder.apiHost;
        this.apiMethod = builder.apiMethod;
        this.timeout = builder.timeout;
        this.proxy = builder.proxy;
        this.okHttpClient = builder.okHttpClient;
    }


    public static class ChatGptBuilder {
        private String apiKey;
        private String apiHost = ApiInterface.DEFAULT_API_HOST;
        private String apiMethod = ApiInterface.CHAT_COMPLETION;
        private OkHttpClient okHttpClient;
        private long timeout = 300;
        private Proxy proxy = Proxy.NO_PROXY;

        public ChatGptStream build() {
            if (null == okHttpClient) {
                okHttpClient = new OkHttpClient().newBuilder()
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .proxy(proxy)
                        .build();
            }
            return new ChatGptStream(this);
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

}

