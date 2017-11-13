package jp.linebot;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {
    private String replyToken;
    private String type;
    private Long timestamp;
    private Source source;
    private Message message;    
}
