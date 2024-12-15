package com.ruoyi.infection.service;
import java.util.Map;

import com.ruoyi.infection.domain.SimulationRequest;

import java.util.List;

public interface ILockSimulationService {
    Map<String, Object> getLockEveryHourInfection(String city, String userId,String simulationFileName);
    Map<String, Object> getEveryHourInfection(String city, String userId,String simulationFileName);
    Map<String, Object> getMADDPGEveryHourInfection(String city,String userId, String simulationFileName);
    Map<String, Object> getMADDPGRiskPoints(String userId,String simulationCity,int simulationDay,int simulationHour,int thresholdInfected,String simulationFileName );
}