package com.sean.chatgpt;

import com.sean.chatgpt.api.ApiInterface;
import com.sean.chatgpt.enums.ChatGptModelEnum;
import com.sean.chatgpt.listener.ConsoleStreamListener;
import com.sean.chatgpt.pojo.chat.ChatCompletionRequest;
import com.sean.chatgpt.pojo.chat.Message;
import com.sean.chatgpt.util.Proxys;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * ChatGpt 聊天流测试
 *
 * @author zengyusheng
 * @date 2023/04/10 20:23
 */
public class ChatGptStreamTest {

    private static final Logger log = LogManager.getLogger(ChatGptTest.class);
    private  ChatGptStream chatGpt;

    @Before
    public void before() {
        Proxy proxy = Proxys.http("127.0.0.1", 10809);
        chatGpt = new ChatGptStream.ChatGptBuilder()
                .setApiKey("sk-*****ekmX0FZJK3UYT1T3BlbkFJv790V6xFFi0Vo97ECCmj")
                .setTimeout(300)
                .setProxy(proxy)
                .setApiHost(ApiInterface.DEFAULT_API_HOST)
                .setApiMethod(ApiInterface.CHAT_COMPLETION)
                .build();
    }

    @Test
    public void chat() throws IOException {
        Message message = Message.of("写一段七言绝句诗");
        ChatCompletionRequest chatCompletion = new ChatCompletionRequest.ChatCompletionBuilder()
                .setModel(ChatGptModelEnum.GPT_3_5_TURBO.getName())
                .setMessages(Arrays.asList(message))
                .build();
        ConsoleStreamListener consoleStreamListener =new ConsoleStreamListener();
        chatGpt.streamChatCompletion(chatCompletion, consoleStreamListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
