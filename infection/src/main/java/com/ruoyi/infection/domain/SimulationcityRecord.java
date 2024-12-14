package com.ruoyi.infection.domain;

import com.alibaba.fastjson2.JSONObject;

public class SimulationcityRecord {
    private String simulationTime;
    private String taskState;
    private JSONObject paraJson;

    // Getters and Setters
    public String getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(String simulationTime) {
        this.simulationTime = simulationTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public JSONObject getParaJson() {
        return paraJson;
    }

    public void setParaJson(JSONObject paraJson) {
        this.paraJson = paraJson;
    }
}
