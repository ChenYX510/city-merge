package com.ruoyi.web.controller.infection;

import com.ruoyi.infection.domain.LockSimulationTimeRecord;
import com.ruoyi.infection.domain.SimulationRequest;
import com.ruoyi.infection.service.ILockSimulationTimeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class LockSimulationTimeRecordController {

    @Autowired
    private ILockSimulationTimeRecordService lockSimulationTimeRecordService;

    @PostMapping("/get_lock_simulation_start_time")
    public String getLockSimulationStartTime(
            @RequestParam("city") String city,
            @RequestParam("start_time") String startTime,
            @RequestParam("userId") String userId) {
        
        lockSimulationTimeRecordService.addLockSimulationTimeRecord(city, startTime, userId);
        return "{\"msg\": \"success\"}";
    }
    @PostMapping("/get_lock_and_maddpg_simulation_time")
    public AjaxResult getLockAndMaddpgSimulationTime(@RequestBody Map<String, String> requestBody) {
        try {
            Map<String, Object> result = lockSimulationTimeRecordService.getLockAndMaddpgSimulationTime(requestBody);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取模拟时间失败：" + e.getMessage());
        }
    }
    @PostMapping("/get_grid_shp")
    public Map<String, Object> getGridShp(@RequestParam String city,@RequestParam String userId) {
        return lockSimulationTimeRecordService.getShpJson(city,userId);
    }
    @PostMapping("/get_policy_result")
    public Map<String, Object> getPolicyResult(
            @RequestParam("city") String city,
            @RequestParam("policy_day") int policyDay,
            @RequestParam("policy_time") int policyTime,
            @RequestParam("userId") String userId,
            @RequestParam(value = "simulation_file_name", required = false, defaultValue = "latestRecord") String simulationFileName) {
        
        return lockSimulationTimeRecordService.getPolicyResult(city, policyDay, policyTime, simulationFileName, userId);
    }

    @PostMapping("/get_MADDPG_simulation_result")
    public ResponseEntity<Map<String, Object>> getMADDPGSimulationResult(
            @RequestParam("city") String city,
            @RequestParam("user_id") String userId,
            @RequestParam("date") int date,
            @RequestParam("time") int time,
            @RequestParam(value = "simulation_file_name", required = false, defaultValue = "latestRecord") String simulationFileName) {
        
        Map<String, Object> response = lockSimulationTimeRecordService.getMADDPGSimulationResult(city, date, time, simulationFileName, userId);
        return ResponseEntity.ok(response);
    }
}