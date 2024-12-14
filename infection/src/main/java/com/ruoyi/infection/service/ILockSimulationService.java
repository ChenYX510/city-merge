package com.ruoyi.infection.service;
import java.util.Map;

import com.ruoyi.infection.domain.SimulationRequest;

import java.util.List;

public interface ILockSimulationService {
    Map<String, Object> getLockEveryHourInfection(String city, String userId,String simulationFileName);
    Map<String, Object> getEveryHourInfection(String city, String userId,String simulationFileName);
    Map<String, Object> getMADDPGEveryHourInfection(String city,String userId, String simulationFileName);
    Map<String, Object> getMADDPGRiskPoints(SimulationRequest request);
}