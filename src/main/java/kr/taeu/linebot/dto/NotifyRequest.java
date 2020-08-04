package kr.taeu.linebot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyRequest {
    private String lineGroupId;
    private String message;
    
    @Builder
    public NotifyRequest(String lineGroupId, String message) {
        this.lineGroupId = lineGroupId;
        this.message = message;
    }
}
