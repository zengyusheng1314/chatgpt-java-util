package com.sean.chatgpt.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 控制台监听打印
 *
 * @author zengyusheng
 * @date 2023/04/11 09:05
 */
public class ConsoleStreamListener extends AbstractStreamListener {
    private static final Logger log = LogManager.getLogger(ConsoleStreamListener.class);

    @Override
    public void onMsg(String message) {
        log.info(message);
    }

    @Override
    public void onError(Throwable throwable, String response) {
        log.info("出问题啦。。。。。。");
    }
}
