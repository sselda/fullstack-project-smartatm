package com.smartatm.controller;

import com.smartatm.dto.Message;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock/chat")
public class MockChatController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> mockChatResponse(@RequestBody Map<String, Object> request) {
        Map<String, Object> message = Map.of(
                "role", "assistant",
                "content", "Mock answer: Hi, How can I help you?"
        );

        Map<String, Object> choice = Map.of("message", message);

        Map<String, Object> response = Map.of(
                "id", "mock-123",
                "object", "chat.completion",
                "created", System.currentTimeMillis() / 1000,
                "model", request.get("model"),
                "choices", List.of(choice)
        );
        return ResponseEntity.ok(response);
    }
}
