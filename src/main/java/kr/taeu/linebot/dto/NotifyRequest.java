package kr.taeu.linebot.dto;

import kr.taeu.linebot.model.LineBotGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyRequest {
    private LineBotGroup lineGroupId;
    private String message;
    
    @Builder
    public NotifyRequest(LineBotGroup lineGroupId, String message) {
        this.lineGroupId = lineGroupId;
        this.message = message;
    }
}
