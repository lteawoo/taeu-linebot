package kr.taeu.linebot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyRequest {
    private String groupId;
    private String message;
    
    @Builder
    public NotifyRequest(String groupId, String message) {
        this.groupId = groupId;
        this.message = message;
    }
}
