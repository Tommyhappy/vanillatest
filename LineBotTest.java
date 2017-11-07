public class LineBotTest {
    public static void main(String[] args) {
        TextMessage textMessage = new TextMessage("hello");
        ReplyMessage replyMessage = new ReplyMessage(
             "<replyToken>",
        textMessage
);
Response<BotApiResponse> response =
        LineMessagingServiceBuilder
                .create("D/Pq3PwT2g+59tNO65CQwbiPzhRw0ooDw+7FKXbvisQhDUm3xCCJZyeFHHTwfTYlDEG+XKROeda2HuTQ+R88DVvNwQ0TWicMcNKv6zRTGldeTneGs3UtlIS91gt/THMJg4+geBIEPvluQbr55wG8EwdB04t89/1O/w1cDnyilFU=")
                .build()
                .replyMessage(replyMessage)
                .execute();
        System.out.println(response.code() + " " + response.message());
        SpringApplication.run(LineBotTest.class, args);
    }

    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        return new TextMessage(event.getMessage().getText());
    }

    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
