package com.sean.chatgpt.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sean.chatgpt.ChatGptStream;
import com.sean.chatgpt.exception.ChatException;
import com.sean.chatgpt.pojo.chat.ChatChoice;
import com.sean.chatgpt.pojo.chat.ChatCompletionResponse;
import com.sean.chatgpt.pojo.chat.Message;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * chat流式输出监听抽象类
 *
 * @author zengyusheng
 * @date 2023/04/11 08:57
 */
public abstract class AbstractStreamListener extends EventSourceListener {

    private static final Logger log = LogManager.getLogger(AbstractStreamListener.class);

    private static final String FINISHED ="[DONE]";

    /**
     * Called when a new message is received.
     * 收到消息 单个字
     *
     * @param message the new message
     */
    public abstract void onMsg(String message);

    /**
     * Called when an error occurs.
     * 出错时调用
     *
     * @param throwable the throwable that caused the error
     * @param response  the response associated with the error, if any
     */
    public abstract void onError(Throwable throwable, String response);

    @Override
    public void onOpen(EventSource eventSource, Response response) {
    }

    @Override
    public void onClosed(EventSource eventSource) {
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals(FINISHED)) {
            return;
        }
        ObjectMapper objectMapper =new ObjectMapper();
        ChatCompletionResponse response = null;
        try {
            response = objectMapper.readValue(data, ChatCompletionResponse.class);
        } catch (JsonProcessingException e) {
            throw new ChatException("json解析异常!",e);
        }
        // 读取Json
        List<ChatChoice> choices = response.getChoices();
        if (choices == null || choices.isEmpty()) {
            return;
        }
        Message delta = choices.get(0).getDelta();
        String text = delta.getContent();

        if (text != null) {
            onMsg(text);
        }

    }


    @Override
    public void onFailure(EventSource eventSource, Throwable throwable, Response response) {

        try {
            log.error("Stream connection error: {}", throwable);

            String responseText = "";

            if (Objects.nonNull(response)) {
                responseText = response.body().string();
            }

            log.error("response：{}", responseText);

            String forbiddenText = "Your access was terminated due to violation of our policies";

            if (StrUtil.contains(responseText, forbiddenText)) {
                log.error("Chat session has been terminated due to policy violation");
                log.error("检测到号被封了");
            }

            String overloadedText = "That model is currently overloaded with other requests.";

            if (StrUtil.contains(responseText, overloadedText)) {
                log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
            }

            this.onError(throwable, responseText);

        } catch (Exception e) {
            log.warn("onFailure error:{}", e);
            // do nothing

        } finally {
            eventSource.cancel();
        }
    }
}
