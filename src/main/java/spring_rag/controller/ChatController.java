package spring_rag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring_rag.dto.Answer;
import spring_rag.dto.Question;
import spring_rag.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping(path = "/ask", produces = "application/json")
    public ResponseEntity<Answer> ask(
            @RequestBody Question question
    ) {
        return ResponseEntity.ok(chatService.ask(question));
    }
}
