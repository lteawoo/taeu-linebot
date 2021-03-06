package kr.taeu.linebot.service;

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import kr.taeu.linebot.dto.NotifyRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
@RequiredArgsConstructor
public class LineBotMessageService {
    private final LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }
    
    public BotApiResponse pushMessage(NotifyRequest dto) {
        // TODO non-blocking 적용?
        // Cf5dc393dbfc047212f20cb10f622baee
        CompletableFuture<BotApiResponse> future = lineMessagingClient.pushMessage(
                new PushMessage(dto.getLineBotGroup().getGroupId(),
                        Arrays.asList(new TextMessage("장애송신!\n내용: " + dto.getMessage()))));
        log.info("아직 response 없음");
        BotApiResponse response = future.join();
        log.info("response get! " + response);
        
        return response;
      }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws Exception {
        final String text = content.getText();

        log.info("Got text message from replyToken:{}: text:{}", replyToken, text);
        switch (text) {
        case "profile": {
            log.info("Invoking 'profile' command: source:{}", event.getSource());
            final String userId = event.getSource().getUserId();
            if (userId != null) {
                if (event.getSource() instanceof GroupSource) {
                    lineMessagingClient.getGroupMemberProfile(((GroupSource) event.getSource()).getGroupId(), userId)
                            .whenComplete((profile, throwable) -> {
                                if (throwable != null) {
                                    this.replyText(replyToken, throwable.getMessage());
                                    return;
                                }

                                this.reply(replyToken,
                                        Arrays.asList(new TextMessage("(from group)"),
                                                new TextMessage("Display name: " + profile.getDisplayName()),
                                                new ImageMessage(profile.getPictureUrl(), profile.getPictureUrl())));
                            });
                } else {
                    lineMessagingClient.getProfile(userId).whenComplete((profile, throwable) -> {
                        if (throwable != null) {
                            this.replyText(replyToken, throwable.getMessage());
                            return;
                        }

                        this.reply(replyToken,
                                Arrays.asList(new TextMessage("Display name: " + profile.getDisplayName()),
                                        new TextMessage("Status message: " + profile.getStatusMessage())));
                    });
                }
            } else {
                this.replyText(replyToken, "Bot can't use profile API without user ID");
            }
            break;
        }
        case "디버그": {
            this.replyText(replyToken, "test: " + event.toString());
        }
        default:
            log.info("Returns echo message {}: {}", replyToken, text);
            this.replyText(replyToken, text);
            this.replyText(replyToken, "test: " + event.getSource());
            break;
        }
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages, boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled)).get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }
}
