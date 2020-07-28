package kr.taeu.linebot.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.taeu.linebot.service.LineMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {
  private final LineMessageService lineMessageService;
  
  @PostMapping("/callapi")
  public void callApi(HttpServletRequest request) throws IOException {
    String text = request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
    
    lineMessageService.pushMessage(text);
  }
}
