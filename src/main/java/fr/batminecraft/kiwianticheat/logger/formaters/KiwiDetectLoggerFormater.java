package fr.batminecraft.kiwianticheat.logger.formaters;

import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.DetectedData;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.MainDataDetectionJson;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.PlayerData;

public class KiwiDetectLoggerFormater {

    public static MainDataDetectionJson formater(MainDataDetectionJson mainData, PlayerData playerData, DetectedData detectionData) {


        mainData.setPlayer(playerData);
        mainData.setDetected(detectionData);

        return mainData;
    }

}

