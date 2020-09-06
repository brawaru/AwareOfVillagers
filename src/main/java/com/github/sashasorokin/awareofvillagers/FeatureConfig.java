package com.github.sashasorokin.awareofvillagers;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.logging.Logger;

public class FeatureConfig {
    private final FileConfiguration config;
    private final HashMap<Feature, Boolean> features = new HashMap<>();
    private final AwareOfVillagers plugin;

    public FeatureConfig(AwareOfVillagers plugin) {
        this.config = plugin.getConfig();
        this.plugin = plugin;
        reload();
    }

    public void setFeatureEnabled(Feature feature, boolean enabled) {
        features.put(feature, enabled);
        config.set(feature.getPath(), enabled);
    }

    public boolean isFeatureEnabled(Feature feature) {
        return features.getOrDefault(feature, feature.isEnabledByDefault());
    }

    public boolean someEnabled(Feature[] features) {
        for (Feature feature : features) {
            if (isFeatureEnabled(feature)) return true;
        }

        return false;
    }

    public void reload() {
        Logger logger = plugin.getLogger();

        for (Feature feature : Feature.values()) {
            boolean isEnabled = config.getBoolean(feature.getPath(), feature.isEnabledByDefault());

            setFeatureEnabled(feature, isEnabled);

            logger.info(String.format("Feature %s is %s", feature.name(), isEnabled ? "enabled" : "disabled"));
        }
    }
}
