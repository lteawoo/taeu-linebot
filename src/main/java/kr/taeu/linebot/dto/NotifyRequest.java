package kr.taeu.linebot.dto;

import kr.taeu.linebot.model.LineBotGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyRequest {
    private LineBotGroup lineBotGroup;
    private String message;
    
    @Builder
    public NotifyRequest(LineBotGroup lineBotGroup, String message) {
        this.lineBotGroup = lineBotGroup;
        this.message = message;
    }
}
