package com.ruoyi.infection.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimulationTask {

    @JsonProperty("R0")
    private String R0; // 输入为字符串，但返回为数字
    @JsonProperty("I_H_para")
    private String I_H_para; // 输入为字符串，但返回为数字
    @JsonProperty("I_R_para")
    private String I_R_para; // 输入为字符串，但返回为数字
    @JsonProperty("H_R_para")
    private String H_R_para; // 输入为字符串，但返回为数字

    @JsonProperty("I_input")
    private String I_input; // "{\"0\":10,\"1\":10,\"2\":10,\"3\":10}"

    @JsonProperty("regionList")
    private String regionList; // "{\"0\":[106.56290146498928,29.897638015977453],\"1\":[106.56290146498928,29.897638015977453],\"2\":[106.68827338995888,29.815208874019206],\"3\":[106.7184724533223,29.732779732060976]}"

    private String simulationCity; // "chongqing"
    private String simulationDays; // "1"

    private String lock_area; // e.g., "{\"0\": [113.38578, 23.32171], \"1\": [113.41566, 23.29624]}"
    private String lock_day; // "1"

    private String simulationFileName; // "2024/12/14"

    private String userId; // "1"

    // Getters and Setters

    public Double getR0() {
        return (R0 != null && !R0.trim().isEmpty()) ? Double.parseDouble(R0) : 0.0;
    }

    public void setR0(String R0) {
        this.R0 = R0;
    }

    public Double getI_H_para() {
        return (I_H_para != null && !I_H_para.trim().isEmpty()) ? Double.parseDouble(I_H_para) : 0.0;
    }

    public void setI_H_para(String I_H_para) {
        this.I_H_para = I_H_para;
    }

    public Double getI_R_para() {
        return (I_R_para != null && !I_R_para.trim().isEmpty()) ? Double.parseDouble(I_R_para) : 0.0;
    }

    public void setI_R_para(String I_R_para) {
        this.I_R_para = I_R_para;
    }

    public Double getH_R_para() {
        return (H_R_para != null && !H_R_para.trim().isEmpty()) ? Double.parseDouble(H_R_para) : 0.0;
    }

    public void setH_R_para(String H_R_para) {
        this.H_R_para = H_R_para;
    }

    public String getI_input() {
        return (I_input != null) ? I_input : "{}"; // Default to empty JSON if null
    }

    public void setI_input(String I_input) {
        this.I_input = I_input;
    }

    public String getRegionList() {
        return (regionList != null) ? regionList : "{}"; // Default to empty JSON if null
    }

    public void setRegionList(String regionList) {
        this.regionList = regionList;
    }

    public String getSimulationCity() {
        return (simulationCity != null) ? simulationCity : "unknown"; // Default to "unknown" if null
    }

    public void setSimulationCity(String simulationCity) {
        this.simulationCity = simulationCity;
    }

    public Integer getSimulationDays() {
        return (simulationDays != null && !simulationDays.trim().isEmpty()) ? Integer.parseInt(simulationDays) : 1;
    }

    public void setSimulationDays(String simulationDays) {
        this.simulationDays = simulationDays;
    }

    public Long getUserId() {
        return (userId != null && !userId.trim().isEmpty()) ? Long.parseLong(userId) : 1L;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLock_area() {
        return (lock_area != null) ? lock_area : "{}"; // Default to empty JSON if null
    }

    public void setLock_area(String lock_area) {
        this.lock_area = lock_area;
    }

    public int getLock_day() {
        return (lock_day != null && !lock_day.trim().isEmpty()) ? Integer.parseInt(lock_day) : 1;
    }

    public void setLock_day(String lock_day) {
        this.lock_day = lock_day;
    }

    public String getSimulationFileName() {
        return (simulationFileName != null) ? simulationFileName : "default_simulation_file";
    }

    public void setSimulationFileName(String simulationFileName) {
        this.simulationFileName = simulationFileName;
    }

}
