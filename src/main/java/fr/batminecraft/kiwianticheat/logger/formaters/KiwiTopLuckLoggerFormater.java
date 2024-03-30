package fr.batminecraft.kiwianticheat.logger.formaters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.batminecraft.kiwianticheat.logger.formaters.dataFormat.*;

public class KiwiTopLuckLoggerFormater {

    public static MainDataTopLuckJson formater(MainDataTopLuckJson mainData, PlayerData playerData, OresMinedList oresList) {
        mainData.setPlayer(playerData);
        mainData.setInfos(oresList);

        return mainData;
    }

}

