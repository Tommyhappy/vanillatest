public class LineBotTest {
    public static void main(String[] args) {
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
