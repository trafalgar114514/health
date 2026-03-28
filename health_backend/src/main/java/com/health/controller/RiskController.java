package com.health.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    @PostMapping("/evaluate")
    public Map<String,Object> evaluate(@RequestBody Map<String,Object> param){

        int age = Integer.parseInt(param.get("age").toString());
        double height = Double.parseDouble(param.get("height").toString());
        double weight = Double.parseDouble(param.get("weight").toString());
        int sbp = Integer.parseInt(param.get("sbp").toString());

        double bmi = weight / Math.pow(height/100,2);

        String level;
        String advice;

        if(bmi > 28 || sbp > 140){
            level = "高风险";
            advice = "建议控制饮食，增加运动，并定期监测血压";
        }else if(bmi > 24){
            level = "中风险";
            advice = "注意饮食结构，适量运动";
        }else{
            level = "低风险";
            advice = "继续保持健康生活方式";
        }

        Map<String,Object> res = new HashMap<>();
        res.put("level",level);
        res.put("advice",advice);

        return res;
    }
}