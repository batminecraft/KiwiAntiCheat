package fr.batminecraft.kiwianticheat.logger.formaters.dataFormat;

import org.bukkit.entity.Player;

public class PlayerData {
        private String name;
        private String ip;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public PlayerData(String name, String ip) {
            this.name = name;
            this.ip = ip;
        }
        public PlayerData() {
        }
    }
