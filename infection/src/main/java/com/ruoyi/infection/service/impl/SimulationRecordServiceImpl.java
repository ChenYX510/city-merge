package com.ruoyi.infection.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruoyi.infection.domain.SimulationcityRecord;
import com.ruoyi.infection.domain.CitySimulationResult;
import com.ruoyi.infection.mapper.SimulationRecordMapper;
import com.ruoyi.infection.mapper.LockSimulationTimeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.ruoyi.infection.service.ISimulationRecordService;
import org.locationtech.jts.geom.Geometry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import org.apache.commons.csv.*;
import java.io.*;

@Service
public class SimulationRecordServiceImpl implements ISimulationRecordService {
    private static final String ROOT_FILE = System.getProperty("user.dir") + "\\testUser\\";
    private static final String[] CITY_NAMES = {
            "shanghai", "chongqing", "guangzhou", "wulumuqi",
            "ningbo", "dongying", "weihai", "zibo",
            "lianyungang", "wuxi", "ezhou", "sihui"
    };
    @Autowired
    private SimulationRecordMapper simulationRecordMapper;
    @Autowired
    private LockSimulationTimeRecordMapper LOCKsimulationRecordMapper;
    @Override
    public List<Long> getIdsByCity(String city,String userId) {
        return simulationRecordMapper.selectIdsByCity(city);
    }
       @Override
    public Map<String, Object> getCitySimulationResults(String userId) {
        String dirPath = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"unlock_result"+"\\";
        Map<String, Object> resultMap = new HashMap<>();
        List<CitySimulationResult> citySimulationResults = new ArrayList<>();

        for (String city : CITY_NAMES) {
            String dirr = dirPath + city;
            File dir = new File(dirr);
            int numResults = dir.exists() ? dir.list().length : 0;

            List<SimulationcityRecord> simulationRecords = simulationRecordMapper.selectSimulationRecordsByCity(city,userId);

            for (SimulationcityRecord record : simulationRecords) {
                String dataFilePath = dirr + "\\" + record.getSimulationTime() + "\\data.json";//这个地方还有待商榷
                File dataFile = new File(dataFilePath);

                if (dataFile.exists()) {
                    try {
                        // 读取 JSON 文件的内容并转换为字符串
                        String jsonContent = new String(Files.readAllBytes(Paths.get(dataFilePath)));
                        // 将 JSON 字符串转换为 JSONObject
                        JSONObject jsonObject = JSONObject.parseObject(jsonContent);

                        // 设置 paraJson 为 JSONObject 格式
                        record.setParaJson(jsonObject);
                    } catch (IOException e) {
                        JSONObject msg = JSONObject.parseObject("读时出错 JSON file: " + e.getMessage());
                        record.setParaJson(msg);
                    }
                } else {
                    JSONObject msg = JSONObject.parseObject("没有找到 data.json 文件");
                    record.setParaJson(msg);
                }
            }
           
            CitySimulationResult result = new CitySimulationResult();
            result.setCity(city);
            result.setSimulationRecordNum(numResults);
            result.setSimulationRecord(simulationRecords);

            citySimulationResults.add(result);
        }

        // 填充返回的 Map 数据
        resultMap.put("msg", "success");
        resultMap.put("simulation_task", citySimulationResults);

        return resultMap;
    }
    @Override
    public Map<String, Object> getCitySimulationLockResults(String userId) {
        String dirPath = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"lock_result"+"\\";
        Map<String, Object> resultMap = new HashMap<>();
        List<CitySimulationResult> citySimulationResults = new ArrayList<>();

        for (String city : CITY_NAMES) {
            String dirr = dirPath + city;
            File dir = new File(dirr);
            int numResults = dir.exists() ? dir.list().length : 0;

            List<SimulationcityRecord> simulationRecords = simulationRecordMapper.selectSimulationLockRecordsByCity(city,userId);

            for (SimulationcityRecord record : simulationRecords) {
                String dataFilePath = dirr + "\\" + record.getSimulationTime() + "\\data.json";//这个地方还有待商榷
                File dataFile = new File(dataFilePath);

                if (dataFile.exists()) {
                    try {
                        // 读取 JSON 文件的内容并转换为字符串
                        String jsonContent = new String(Files.readAllBytes(Paths.get(dataFilePath)));
                        // 将 JSON 字符串转换为 JSONObject
                        JSONObject jsonObject = JSONObject.parseObject(jsonContent);

                        // 设置 paraJson 为 JSONObject 格式
                        record.setParaJson(jsonObject);
                    } catch (IOException e) {
                        JSONObject msg = JSONObject.parseObject("读时出错 JSON file: " + e.getMessage());
                        record.setParaJson(msg);
                    }
                } else {
                    JSONObject msg = JSONObject.parseObject("没有找到 data.json 文件");
                    record.setParaJson(msg);
                }
            }

            CitySimulationResult result = new CitySimulationResult();
            result.setCity(city);
            result.setSimulationRecordNum(numResults);
            result.setSimulationRecord(simulationRecords);

            citySimulationResults.add(result);
        }
        // 填充返回的 Map 数据
        resultMap.put("msg", "success");
        resultMap.put("simulation_task", citySimulationResults);

        return resultMap;
    }
    @Override
    public Map<String, Object> getCitySimulationMADDPGResults(String userId) {
        String dirPath = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"MADDPG_result"+"\\";
        Map<String, Object> resultMap = new HashMap<>();
        List<CitySimulationResult> citySimulationResults = new ArrayList<>();

        for (String city : CITY_NAMES) {
            String dirr = dirPath + city;
            File dir = new File(dirr);
            int numResults = dir.exists() ? dir.list().length : 0;

            List<SimulationcityRecord> simulationRecords = simulationRecordMapper.selectSimulationMADDPGRecordsByCity(city,userId);

            for (SimulationcityRecord record : simulationRecords) {
                String dataFilePath = dirr + "\\" + record.getSimulationTime() + "\\data.json";//这个地方还有待商榷
                File dataFile = new File(dataFilePath);

                if (dataFile.exists()) {
                    try {
                        // 读取 JSON 文件的内容并转换为字符串
                        String jsonContent = new String(Files.readAllBytes(Paths.get(dataFilePath)));
                        // 将 JSON 字符串转换为 JSONObject
                        JSONObject jsonObject = JSONObject.parseObject(jsonContent);

                        // 设置 paraJson 为 JSONObject 格式
                        record.setParaJson(jsonObject);
                    } catch (IOException e) {
                        JSONObject msg = JSONObject.parseObject("读时出错 JSON file: " + e.getMessage());
                        record.setParaJson(msg);
                    }
                } else {
                    JSONObject msg = JSONObject.parseObject("没有找到 data.json 文件");
                    record.setParaJson(msg);
                }
            }

            CitySimulationResult result = new CitySimulationResult();
            result.setCity(city);
            result.setSimulationRecordNum(numResults);
            result.setSimulationRecord(simulationRecords);

            citySimulationResults.add(result);
        }
        // 填充返回的 Map 数据
        resultMap.put("msg", "success");
        resultMap.put("simulation_task", citySimulationResults);

        return resultMap;
    }
        @Override
    public Map<String, Object> getSimulationResult(String city, int simulationDay, int simulationHour, String simulationFileName,String userId) {
        Map<String, Object> result = new HashMap<>();
        String dirPath = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"unlock_result"+"\\";
        dirPath = dirPath + city + "\\";
        int targetSimulationHour = (simulationDay - 1) * 24 + (simulationHour - 1);

        // 获取最新的模拟记录文件夹
        if ("latestRecord".equals(simulationFileName)) {
            Long latestId = simulationRecordMapper.findLatestIdByCity(city,userId);
            if (latestId == null) {
                result.put("msg", "没有最新的模拟记录");
                return result;
            }
            simulationFileName = simulationRecordMapper.findFilePathById(latestId);
        }

        dirPath += simulationFileName + "\\";

        File directory = new File(dirPath);

        System.out.println("检查文件夹路径----"+dirPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                int numResult = (files.length - 1) / 2; // 计算已生成的文件数量

                // 检查目标 CSV 文件是否存在
                String fileName = "simulation_DSIHR_result_" + targetSimulationHour + ".csv";
                File resultFile = new File(dirPath + fileName);
                System.out.println("结果文件路径："+dirPath+fileName);
                if (resultFile.exists()) {
                    // 读取 CSV 文件内容
                    readCSVFile(resultFile, result);
                    result.put("state", "current task completed");
                } else {
                    result.put("state", "not completed");
                }

                // 读取模拟的 JSON 文件
                readSimulationJson(dirPath + "data.json", result, numResult);
            }
        } else {
            result.put("state", "no simulationResult");
        }

        return result;
    }


    @Override
    public Map<String, Object> getLockSimulationResult(String city, int simulationDay, int simulationHour, String simulationFileName,String userId) {
        Map<String, Object> result = new HashMap<>();
        String dirPath = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"lock_result"+"\\";
        dirPath = dirPath + city + "\\";
        int targetSimulationHour = (simulationDay - 1) * 24 + (simulationHour - 1);

        // 获取最新的模拟记录文件夹
        if ("latestRecord".equals(simulationFileName)) {
            Long latestId = simulationRecordMapper.findLockLatestIdByCity(city,userId);
            if (latestId == null) {
                result.put("msg", "没有最新的模拟记录");
                return result;
            }
            simulationFileName = simulationRecordMapper.findLockFilePathById(latestId);
        }

        dirPath += simulationFileName + "\\";

        File directory = new File(dirPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                int numResult = (files.length - 1) / 2; // 计算已生成的文件数量

                // 检查目标 CSV 文件是否存在
                String fileName = "simulation_DSIHR_result_" + targetSimulationHour + ".csv";
                File resultFile = new File(dirPath + fileName);
                if (resultFile.exists()) {
                    // 读取 CSV 文件内容
                    readCSVFile(resultFile, result);
                    result.put("state", "current task completed");
                } else {
                    result.put("state", "not completed");
                }

                // 读取模拟的 JSON 文件
                readSimulationJson(dirPath + "data.json", result, numResult);
            }
        } else {
            result.put("state", "no simulationResult");
        }

        return result;
    }

    /**
     * 读取 CSV 文件内容并填充到结果中
     */
    private void readCSVFile(File csvFile, Map<String, Object> result) {
        List<Double> S_data = new ArrayList<>();
        List<Double> I_data = new ArrayList<>();
        List<Double> H_data = new ArrayList<>();
        List<Double> R_data = new ArrayList<>();
        List<Double> newInfectedData = new ArrayList<>();
        List<Double> totalNumData = new ArrayList<>();
        String state = "not completed";

        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("Column1","geometry", "S", "I", "H", "R", "new_infected", "total_num") // 为所有列指定名称
                     .withSkipHeaderRecord(true)
                     .withTrim())) {

            for (CSVRecord record : csvParser) {
                // 使用 get 方法获取每个列名对应的数据
                // 如果没有对应的列名会报错，所以需要使用 contains 方法判断列是否存在
                if (record.isMapped("S")) {
                    S_data.add(Double.parseDouble(record.get("S")));
                }
                if (record.isMapped("I")) {
                    I_data.add(Double.parseDouble(record.get("I")));
                }
                if (record.isMapped("H")) {
                    H_data.add(Double.parseDouble(record.get("H")));
                }
                if (record.isMapped("R")) {
                    R_data.add(Double.parseDouble(record.get("R")));
                }
                if (record.isMapped("new_infected")) {
                    newInfectedData.add(Double.parseDouble(record.get("new_infected")));
                }
                if (record.isMapped("total_num")) {
                    totalNumData.add(Double.parseDouble(record.get("total_num")));
                }
            }

            state = "current task completed";

            result.put("S_data", S_data);
            result.put("I_data", I_data);
            result.put("H_data", H_data);
            result.put("R_data", R_data);
            result.put("newInfected_data", newInfectedData);
            result.put("total_num_data", totalNumData);
            result.put("state", state);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("msg", "读取CSV文件失败：" + e.getMessage());
        }
    }

    /**
     * 读取 JSON 文件内容并填充到结果中
     */
    private void readSimulationJson(String jsonFilePath, Map<String, Object> result, int numResult) {
        File jsonFile = new File(jsonFilePath);
        if (jsonFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line);
                }
                String content = contentBuilder.toString();
                JSONObject jsonObject = JSONObject.parseObject(content);
                result.put("num_result", numResult);
                result.put("para_json", jsonObject);

                int plannedSimulationDays = Integer.parseInt(jsonObject.get("simulation_days").toString());
                int plannedFilesNum = plannedSimulationDays * 24;

                if (plannedFilesNum == numResult) {
                    result.put("state", "all tasks completed");
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.put("msg", "读取JSON文件失败：" + e.getMessage());
            }
        } else {
            result.put("state", "no simulationTask");
        }
    }

    @Override
    public Map<String, Object> getSimulationRiskPoints(String city, int simulationDay, int simulationHour, int thresholdInfected, String simulationFileName, String userId) {
        Map<String, Object> result = new HashMap<>();
        String dir = ROOT_FILE + userId + "\\" + "SimulationResult" + "\\" + "unlock_result" + "\\" + city + "\\";

        if ("latestRecord".equals(simulationFileName)) {
            Integer maxId = LOCKsimulationRecordMapper.findLatestSimulationId(city, userId);
            if (maxId == null) {
                result.put("msg", "没有最新的模拟记录");
                return result;
            }
            simulationFileName = LOCKsimulationRecordMapper.findFilePathById(maxId, userId);
            dir += simulationFileName + "\\";
        } else {
            dir += simulationFileName + "\\";
        }

        simulationHour = (simulationDay - 1) * 24 + (simulationHour - 1);
        String filePath = dir + "simulation_DSIHR_result_" + simulationHour + ".csv";
        List<List<Object>> riskPoints = new ArrayList<>();

        System.out.println(filePath);
        File csvFile = new File(filePath);
        if (csvFile.exists()) {
            // 调用 readCSVFile 函数来读取 CSV 文件，并获取 I 列数据
            Map<String, Object> readResult = new HashMap<>();
            readCSVFile(csvFile, readResult);

            List<Double> I_data = (List<Double>) readResult.get("I_data");

            // 判断是否读取到 I 数据
            if (I_data == null || I_data.isEmpty()) {
                result.put("msg", "没有 I 列数据");
                return result;
            }

            // 遍历 I 数据，筛选出大于阈值的记录
            int index = 0; // 用于跟踪当前行号
            for (Double infected : I_data) {
                if (infected >= thresholdInfected) {
                    riskPoints.add(Arrays.asList(index, infected));
                }
                index++;
            }

            result.put("msg", riskPoints.isEmpty() ? "没有超过阈值的网格" : "success");

        } else {
            result.put("msg", "没有这个时刻的模拟结果");
        }

        result.put("result", riskPoints);
        return result;
    }

    @Override
    public Map<String, Object> getLockSimulationRiskPoints(String city, int simulationDay, int simulationHour, int thresholdInfected, String simulationFileName,String userId) {
        Map<String, Object> result = new HashMap<>();
        String dir = ROOT_FILE+userId+"\\"+"SimulationResult"+"\\"+"unlock_result"+"\\";
        dir = dir + city + "\\";
        if ("latestRecord".equals(simulationFileName)) {
            Integer maxId = LOCKsimulationRecordMapper.findLockLatestSimulationId(city,userId);
            if (maxId == null) {
                result.put("msg", "没有最新的模拟记录");
                return result;
            }
            simulationFileName = LOCKsimulationRecordMapper.findLockFilePathById(maxId,userId);
            dir += simulationFileName + "\\";
        } else {
            dir += simulationFileName + "\\";
        }

        simulationHour = (simulationDay - 1) * 24 + (simulationHour - 1);
        String filePath = dir + "simulation_DSIHR_result_" + simulationHour + ".csv";
        List<List<Object>> riskPoints = new ArrayList<>();

        File csvFile = new File(filePath);
        if (csvFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String headerLine = br.readLine(); // 读取并跳过第一行的表头
                String line;

                int index = 0; // 用于跟踪当前行号
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // 根据逗号分隔字段

                    int infected = Integer.parseInt(values[2].trim()); // 假设 I 列是第二列

                    if (infected >= thresholdInfected) {
                        riskPoints.add(Arrays.asList(index, infected));
                    }
                    index++;
                }

                result.put("msg", riskPoints.isEmpty() ? "没有超过阈值的网格" : "success");

            } catch (IOException e) {
                result.put("msg", "读取 CSV 文件失败：" + e.getMessage());
            }
        } else {
            result.put("msg", "没有这个时刻的模拟结果");
        }

        result.put("result", riskPoints);
        return result;
    }
    @Override
    public Map<String, Object> getgrid_control_policy(String city, String userId) {
        Map<String, Object> result = new HashMap<>();
        String dirPath = ROOT_FILE+userId+"\\"+"grid_coefficient"+"\\";
        dirPath = dirPath + city + "\\";
        File directory = new File(dirPath);
        // 返回的消息和文件数量
        String msg;
        int numFile = 0;
        if (directory.exists() && directory.isDirectory()) {
            // 统计目录中的文件数量
            numFile = directory.listFiles() != null ? directory.listFiles().length : 0;
            msg = (numFile == 0) ? "没有文件" : "success";
        } else {
            // 目录不存在
            msg = "没有文件";
        }
        result.put("msg", msg);
        result.put("num_file", numFile);

        return result;
    }
    @Override
        public Map<String, Object> getCity4LevelName(String city,String userId) {
        Map<String, Object> response = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        String filePath = ROOT_FILE+userId+"\\" + city + "\\upload.shp";
        File file = new File(filePath);

        if (!file.exists()) {
            response.put("msg", "Shapefile 文件不存在");
            response.put("name_list", nameList);
            return response;
        }

        try {
            File files = new File(filePath);
            if (!files.exists()) {
                response.put("msg", "文件不存在");
                return response;
            }

            FileDataStore store = FileDataStoreFinder.getDataStore(files);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            FeatureCollection<?, ?> collection = featureSource.getFeatures();

            try (FeatureIterator<?> features = collection.features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) features.next();
                    String name = (String) feature.getAttribute("index"); // 读取 "index" 列的值
                    nameList.add(name);
                }
            }
            response.put("msg", "success");
            response.put("name_list", nameList);

        } catch (Exception e) {
            response.put("msg", "读取文件时发生错误");
            e.printStackTrace();
        }

        return response;
    }
    @Override
        public Map<String, Object> getDSIHR(String file,double I_H_para,double I_R_para,double H_R_para,String userId) {

            String pythonExecutable = "C:\\Users\\86182\\anaconda3\\python.exe";  // 或者直接指定完整路径
            String scriptPath = "C:\\Users\\86182\\Desktop\\数据库课设\\Software-Engineering\\ruoyi-admin\\testuser\\getDSIHR.py";
            // 处理结果
             // 调用函数
             Map<String, Object> result = callPythonScript(pythonExecutable, scriptPath, I_H_para, I_R_para, H_R_para,file);


        return result;
    }

    @Override
    public String getSimulationFilePath(String userId, String city, int simulationDay, int simulationHour, String simulationType, String simulationFileName) {
        int simulationHourAdjusted = (simulationDay - 1) * 24 + (simulationHour - 1);
        String queryFileName = "";
        String resultTable = "";
        String resultColumn = "";
        String resultName = "";
        String filePath = "";

        String dirPath = ROOT_FILE+userId+"\\SimulationResult\\";
        switch (simulationType) {
            case "none_type":
                resultTable = "infection_unlock_simulation_result";
                resultColumn = "unlock_result_id";
                resultName = "unlock_result";
                //filePath = dirPath + resultName + "\\" + city + "\\" + queryFileName + "\\simulation_DSIHR_result_" + simulationHourAdjusted + ".csv";
                break;
            case "lock_type":
                resultTable = "infection_lock_simulation_result";
                resultColumn = "lock_result_id";
                resultName = "lock_result";
                //filePath = dirPath + resultName + "\\" + city + "\\" + queryFileName + "\\simulation_DSIHR_result_" + simulationHourAdjusted + ".csv";
                break;
            case "MADDPG_type":
                resultTable = "infection_maddpg_simulation_result";
                resultColumn = "maddpg_result_id";
                resultName = "MADDPG_result";
                //filePath = dirPath + resultName + "\\" + city + "\\" + queryFileName + "\\simulation_DSIHR_result_" + simulationHourAdjusted + ".csv";
                break;
            default:
        }

        if ("latestRecord".equals(simulationFileName)) {
            queryFileName = simulationRecordMapper.getLatestFilePath(city, resultTable, resultColumn, userId);
            if (queryFileName == null) {
                return null;
            }
        } else {
            queryFileName = simulationFileName;
        }
        filePath = dirPath + resultName + "\\" + city + "\\" + queryFileName + "\\simulation_DSIHR_result_" + simulationHourAdjusted + ".csv";
        return filePath;
    }

    public static Map<String, Object> callPythonScript(String pythonExecutable, String scriptPath,
                                                       double I_H_para, double I_R_para, double H_R_para,
                                                       String filepath) {
        ProcessBuilder pb;
        Process process;
        BufferedReader reader;
        StringBuilder output = new StringBuilder();
        Gson gson = new Gson();

        try {
            // 构建命令行参数
            String[] command = {pythonExecutable, scriptPath,
                                Double.toString(I_H_para), Double.toString(I_R_para),
                                Double.toString(H_R_para), filepath};

            // 创建进程构建器并启动进程
            pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);  // 合并标准输出和标准错误流
            process = pb.start();

            // 读取输出
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Python script exited with error code: " + exitCode);
                return null;  // 或者抛出异常
            }
            System.out.println("Python script output: " + output.toString());
            // 将 JSON 字符串转换为 Map
            return gson.fromJson(output.toString(), HashMap.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

