package kr.taeu.linebot.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
@RequiredArgsConstructor
public class LineMessageService {
  private final LineMessagingClient lineMessagingClient;
  
  public BotApiResponse pushMessage(String text) {
    // TODO non-blocking 적용?
    CompletableFuture<BotApiResponse> future = lineMessagingClient.pushMessage(new PushMessage("Cf5dc393dbfc047212f20cb10f622baee", Arrays.asList(
        new TextMessage("장애송신!\n"
            + "내용: " + text))));
    log.info("아직 response 없음");
    BotApiResponse response = future.join();
    log.info("response get! " + response);
    
    return response;
  }
}
