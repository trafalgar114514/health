package com.health.service;

import com.health.dto.HealthRecordDTO;
import com.health.entity.HealthRecord;
import com.health.mapper.HealthRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class HealthRecordService {

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    public void addRecord(HealthRecordDTO dto) {
        HealthRecord record = new HealthRecord();
        boolean isBloodPressure = "血压".equals(dto.getType());

        if (isBloodPressure && (dto.getSystolic() == null || dto.getDiastolic() == null)) {
            throw new IllegalArgumentException("血压记录必须包含收缩压和舒张压");
        }

        record.setUserId(dto.getUserId());
        record.setType(dto.getType());
        record.setValue(isBloodPressure ? dto.getSystolic() : dto.getValue());
        record.setSystolic(dto.getSystolic());
        record.setDiastolic(dto.getDiastolic());
        record.setUnit(dto.getUnit());
        record.setRecordDate(LocalDate.parse(dto.getRecordDate()));
        record.setRemark(dto.getRemark());

        HealthRecord oldRecord = healthRecordMapper.selectOneByUserIdAndTypeAndDate(
                record.getUserId(),
                record.getType(),
                record.getRecordDate()
        );

        if (oldRecord != null) {
            healthRecordMapper.updateByUserIdAndTypeAndDate(record);
        } else {
            healthRecordMapper.insert(record);
        }
    }

    public List<Map<String, Object>> getTrend(Long userId, String type) {
        return healthRecordMapper.selectTrend(userId, type);
    }

    public List<HealthRecord> list(Long userId) {
        return healthRecordMapper.selectByUserId(userId);
    }
}
