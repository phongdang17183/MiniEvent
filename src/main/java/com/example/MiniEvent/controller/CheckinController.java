package com.example.MiniEvent.controller;

import com.example.MiniEvent.model.CheckinRequest;
import com.example.MiniEvent.response.ResponseObject;
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
    public ResponseEntity<ResponseObject> checkin(@RequestBody CheckinRequest request) throws Exception {
        ResponseObject result = checkinService.checkin(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
