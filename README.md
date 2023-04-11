# chatgpt-java-util
## ChatGpt Java Api 
### 等待响应全部输出
    @Before
    public void before() {
        Proxy proxy = Proxys.http("127.0.0.1", 10809);
        chatGpt = new ChatGpt.ChatGptBuilder()
                .setApiKey("sk-*****ekmX0FZJK3UYT1T3BlbkFJv790V6xFFi0Vo97ECCmj")
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
        Message message = Message.of("写一首赞美的词");
        ChatCompletionRequest chatCompletion = new ChatCompletionRequest.ChatCompletionBuilder()
                .setModel(ChatGptModelEnum.GPT_3_5_TURBO.getName())
                .setMessages(Arrays.asList(message))
                .build();
        String resultMessage =  chatGpt.chat(chatCompletion);

        log.info("\n"+resultMessage);
    }
### 流式监听输出

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
