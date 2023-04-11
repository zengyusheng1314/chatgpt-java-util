package com.sean.chatgpt;

import com.sean.chatgpt.api.ApiInterface;
import com.sean.chatgpt.enums.ChatGptModelEnum;
import com.sean.chatgpt.pojo.chat.ChatCompletionRequest;
import com.sean.chatgpt.pojo.chat.Message;
import com.sean.chatgpt.util.Proxys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


import java.net.Proxy;
import java.util.Arrays;



/**
 * 测试
 *
 * @author zengyusheng
 * @date 2023/04/10 13:46
 */


public class ChatGptTest {

    private static final Logger log = LogManager.getLogger(ChatGptTest.class);
    private  ChatGpt chatGpt;

    @Before
    public void before() {
        Proxy proxy = Proxys.http("127.0.0.1", 10809);
        chatGpt = new ChatGpt.ChatGptBuilder()
                .setApiKey("sk-*****FZJK3UYT1T3BlbkFJv790V6xFFi0Vo97ECCmj")
                .setTimeout(300)
                .setProxy(proxy)
                .setApiHost(ApiInterface.DEFAULT_API_HOST)
                .setApiMethod(ApiInterface.CHAT_COMPLETION)
                .build();
    }

    @Test
    public void chat() {
      String message =  chatGpt.chat("写一首赞美的词");
        log.info("\n"+message);
    }

    @Test
    public void chat2() {
        Message message = Message.of("写一首赞美的词！");
        ChatCompletionRequest chatCompletion = new ChatCompletionRequest.ChatCompletionBuilder()
                .setModel(ChatGptModelEnum.GPT_3_5_TURBO.getName())
                .setMessages(Arrays.asList(message))
                .build();
        String resultMessage =  chatGpt.chat(chatCompletion);

        log.info("\n"+resultMessage);
    }

}
