package com.health.controller;

import com.health.common.Result;
import com.health.dto.HealthRecordDTO;
import com.health.entity.HealthRecord;
import com.health.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin
public class HealthController {

    @Autowired
    private HealthRecordService healthRecordService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody HealthRecordDTO dto) {
        healthRecordService.addRecord(dto);
        return Result.success();
    }
    @GetMapping("/trend")
    public List<Map<String, Object>> getTrend(@RequestParam Long userId,
                                              @RequestParam String type) {
        return healthRecordService.getTrend(userId, type);
    }
    @GetMapping("/list")
    public List<HealthRecord> list(@RequestParam Long userId) {
        return healthRecordService.list(userId);
    }
}