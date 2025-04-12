package com.marcos.starwarsapi.dto;

import java.util.List;
import java.util.Map;

public class SwapiResponseDTO {

    private List<SwapiEntity> result;

    public static class SwapiEntity {
        private Map<String, String> properties;
        private String uid;

        public Map<String, String> getProperties() {
            return properties;
        }

        public String getUid() {
            return uid;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public List<SwapiEntity> getResult() {
        return result;
    }

    public void setResult(List<SwapiEntity> result) {
        this.result = result;
    }
}
