package fr.batminecraft.kiwianticheat.logger.formaters.dataFormat;

public class OresMinedList {
    private int stone;
    private int emerald;
    private int netherite;
    private int diamond;
    private int gold;
    private int iron;
    private int copper;
    private int coal;

    // Getters et Setters
    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getEmerald() {
        return emerald;
    }

    public void setEmerald(int emerald) {
        this.emerald = emerald;
    }

    public int getNetherite() {
        return netherite;
    }

    public void setNetherite(int netherite) {
        this.netherite = netherite;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getIron() {
        return iron;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public int getCopper() {
        return copper;
    }

    public void setCopper(int copper) {
        this.copper = copper;
    }

    public int getCoal() {
        return coal;
    }

    public void setCoal(int coal) {
        this.coal = coal;
    }

    public OresMinedList(int stone, int emerald, int netherite, int diamond, int gold, int iron, int copper, int coal) {
        this.stone = stone;
        this.emerald = emerald;
        this.netherite = netherite;
        this.diamond = diamond;
        this.gold = gold;
        this.iron = iron;
        this.copper = copper;
        this.coal = coal;
    }
    public OresMinedList() {

    }
}
