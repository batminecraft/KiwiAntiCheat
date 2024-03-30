package fr.batminecraft.kiwianticheat.logger.formaters.dataFormat;

public class DetectedData {
        private String type;
        private String details;

        // Getters et Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public DetectedData(String type, String details) {
            this.type = type;
            this.details = details;
        }

        public DetectedData() {

        }
    }
