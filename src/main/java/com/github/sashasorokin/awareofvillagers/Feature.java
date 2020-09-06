package com.github.sashasorokin.awareofvillagers;

public enum Feature {
    DEATH_MESSAGES("alerts.died", true),
    INFECTED_MESSAGES("alerts.infected", true),
    CURED_MESSAGES("alerts.cured", true),
    HEALTH_MESSAGES("health_alerts.enabled", true),
    DETECTION("detection.enabled", true),
    DETECTION_OBFUSCATION("detection.coordinates_obfuscation", true),
    ;

    private final String path;
    private final boolean defaultEnabled;

    Feature(String path, boolean defaultEnabled) {
        this.path = path;
        this.defaultEnabled = defaultEnabled;
    }

    public String getPath() {
        return path;
    }

    public boolean isEnabledByDefault() {
        return defaultEnabled;
    }
}
