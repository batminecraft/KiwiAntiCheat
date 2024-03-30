package fr.batminecraft.kiwianticheat.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.batminecraft.kiwianticheat.Main;
import fr.batminecraft.kiwianticheat.logger.formaters.KiwiDetectLoggerFormater;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.MainDataDetectionJson;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.MainDataTopLuckJson;
import fr.batminecraft.kiwianticheat.utils.PluginInfos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static fr.batminecraft.kiwianticheat.sqllink.SqlLink.getConnection;

public class KiwiLogger {
    private String fileName;
    private KiwiLogSystem loggerSystem;
    private String fileDirectory;
    private File dataFolder = Main.instance.getDataFolder();
    private File logFolder;
    public File dataFile;

    public KiwiLogger(String fileName, KiwiLogSystem loggerSystem) throws Exception {
        this.fileName = fileName;
        this.loggerSystem = loggerSystem;
        verifyOrCreateFiles();
    }

    public void logDetect(MainDataDetectionJson logInfos) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        MainDataDetectionJson[] updatedLogs;
        try {
            MainDataDetectionJson[] existingLogs;
            if (dataFile.exists() && dataFile.length() > 0) {
                existingLogs = objectMapper.readValue(dataFile, MainDataDetectionJson[].class);
            } else {
                existingLogs = new MainDataDetectionJson[0];
            }

            updatedLogs = Arrays.copyOf(existingLogs, existingLogs.length + 1);
            updatedLogs[existingLogs.length] = logInfos;

            objectMapper.writeValue(dataFile, updatedLogs);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = getConnection().createStatement();
            String type = logInfos.getDetected().getType();
            String logInfosJson = objectMapper.writeValueAsString(logInfos);
            String sql = "INSERT INTO detections (type, content) VALUES ('" + type + "', '" + logInfosJson + "')";
            int rowsAffected = statement.executeUpdate(sql);

        } catch (SQLException e) {
            PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                PluginInfos.errorLogger.logError("[ERROR] -> [SqlLink] -> " + e.getMessage());
            }
        }
    }

    public void logTopLuck(MainDataTopLuckJson logInfos) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            MainDataTopLuckJson[] existingLogs;
            if (dataFile.exists() && dataFile.length() > 0) {
                existingLogs = objectMapper.readValue(dataFile, MainDataTopLuckJson[].class);
            } else {
                existingLogs = new MainDataTopLuckJson[0];
            }

            MainDataTopLuckJson[] updatedLogs = Arrays.copyOf(existingLogs, existingLogs.length + 1);
            updatedLogs[existingLogs.length] = logInfos;

            objectMapper.writeValue(dataFile, updatedLogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logError(String logInfos) {
        try {
            LocalDateTime hour = LocalDateTime.now();
            String decimalDate = hour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            FileWriter fileWriter = new FileWriter(dataFile, true);
            fileWriter.write("\n" + decimalDate + " <LOG> " + logInfos);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyOrCreateFiles() throws Exception {
        if(verifyFileCreateRequirements()) {
            if(loggerSystem == KiwiLogSystem.KIWI_DETECT_LOGGER) {
                createKiwiDetectLoggerFiles();
            } else if(loggerSystem == KiwiLogSystem.KIWI_TOP_LUCK_LOGGER) {
                createKiwiTopLuckLoggerFiles();
            } else if(loggerSystem == KiwiLogSystem.KIWI_ERROR_LOGGER) {
                createKiwiErrorLoggerFiles();
            }
        }
    }

    private void createKiwiDetectLoggerFiles() {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        logFolder = new File(dataFolder, "KiwiDetectionLogs");
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        dataFile = new File(logFolder, decimalDate + "_KiwiDetectLogger.json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void createKiwiTopLuckLoggerFiles() {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        logFolder = new File(dataFolder, "KiwiTopLuckLogs");
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        dataFile = new File(logFolder, decimalDate + "_KiwiTopLuckLogger.json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void createKiwiErrorLoggerFiles() {
        LocalDateTime hour = LocalDateTime.now();
        String decimalDate = hour.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        logFolder = new File(dataFolder, "KiwiErrorLogs");
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        dataFile = new File(logFolder, decimalDate + "_KiwiErrorLogger.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean verifyFileCreateRequirements() throws Exception {
        if(fileName == null || loggerSystem == null) {
            throw new Exception("fileName or loggerSystem var is null !");
        }
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public KiwiLogSystem getLoggerSystem() {
        return loggerSystem;
    }
}
