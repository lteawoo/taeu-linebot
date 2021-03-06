package kr.taeu.linebot.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.taeu.linebot.dto.NotifyRequest;
import kr.taeu.linebot.service.LineBotMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LineBotMessageController {
  private final LineBotMessageService lineBotMessageService;
  
  @PostMapping("/notify")
  public void notify(@RequestBody final NotifyRequest dto) throws IOException {
    log.info("notify request: " + dto);
    
    lineBotMessageService.pushMessage(dto);
  }
}
