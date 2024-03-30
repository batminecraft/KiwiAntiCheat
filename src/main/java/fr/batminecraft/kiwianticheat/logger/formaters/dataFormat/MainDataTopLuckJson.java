package fr.batminecraft.kiwianticheat.logger.formaters.dataFormat;

public class MainDataTopLuckJson {
    private String date;
    private PlayerData player;
    private OresMinedList infos;

    // Getters et Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PlayerData getPlayer() {
        return player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    public OresMinedList getInfos() {
        return infos;
    }

    public void setInfos(OresMinedList infos) {
        this.infos = infos;
    }

    public MainDataTopLuckJson(String date) {
        this.date = date;
    }

    public MainDataTopLuckJson() {

    }
}
