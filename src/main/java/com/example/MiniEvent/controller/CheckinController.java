package com.example.MiniEvent.controller;

import com.example.MiniEvent.model.CheckinRequest;
import com.example.MiniEvent.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckinController {
    private final CheckinService checkinService;

    @PostMapping("/checkin")
    public ResponseEntity<String> checkin(@RequestBody CheckinRequest request) throws Exception {
        String result = checkinService.checkin(request);
        if (result.contains("thành công")) {
            return ResponseEntity.ok(result);
        } else if (result.contains("không tồn tại")) {
            return ResponseEntity.badRequest().body(result);
        } else {
            return ResponseEntity.status(403).body(result);
        }
    }
}
