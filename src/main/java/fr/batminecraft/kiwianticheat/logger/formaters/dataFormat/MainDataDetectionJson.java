package fr.batminecraft.kiwianticheat.logger.formaters.dataFormat;

public class MainDataDetectionJson {
        private String date;
        private String address;
        private PlayerData player;
        private DetectedData detected;

        public MainDataDetectionJson() {
        }

        // Getters et Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public PlayerData getPlayer() {
            return player;
        }

        public void setPlayer(PlayerData player) {
            this.player = player;
        }

        public DetectedData getDetected() {
            return detected;
        }

        public void setDetected(DetectedData detected) {
            this.detected = detected;
        }

        public MainDataDetectionJson(String date, String address) {
            this.date = date;
            this.address = address;
        }

    }
