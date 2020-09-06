package com.github.sashasorokin.awareofvillagers;

import com.github.sashasorokin.awareofvillagers.messages.Message;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Announcements {
    private final AwareOfVillagers plugin;
    private final HashMap<Villager.Profession, String> professionNames = new HashMap<>();
    private final CoordinatesObfuscator coordinatesObfuscator = new CoordinatesObfuscator();
    private double minHealthPercentage;
    private boolean obfuscationEnabled;
    private double detectionRange;

    public Announcements(AwareOfVillagers plugin) {
        this.plugin = plugin;
        reload();
    }

    private static String getEntityName(Entity villager) {
        String name = villager.getCustomName();

        if (name == null) name = villager.getName();

        if (name == null) {
            name = villager.getType().name();

            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        return name;
    }

    private static void putVillagerLocation(Entity villager, Map<String, String> map) {
        Location location = villager.getLocation();

        map.put("x", String.format("%.1f", location.getX()));
        map.put("y", String.format("%.1f", location.getY()));
        map.put("z", String.format("%.1f", location.getZ()));
    }

    private String getVillagerName(Villager villager) {
        String villagerName = villager.getCustomName();

        if (villagerName != null) return villagerName;

        Villager.Profession profession = villager.getProfession();

        // By some reason,
        if (profession.equals(Villager.Profession.NONE)) {
            profession = Villager.Profession.NITWIT;
        }

        villagerName = professionNames.get(profession);

        return villagerName == null ? getEntityName(villager) : villagerName;
    }

    public void announceVillagerHealth(Villager villager, @Nullable Entity attacker) {
        // if (villager.getLastDamage() == 0) return;

        AttributeInstance maxHealth = villager.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (maxHealth == null) return;

        double healthPercent = 100 * (villager.getHealth() / maxHealth.getValue());

        if (healthPercent > minHealthPercentage) return;

        HashMap<String, String> placeholders = new HashMap<>();

        putVillagerLocation(villager, placeholders);

        placeholders.put("villager", getVillagerName(villager));

        placeholders.put("current", String.format("%.0f", villager.getHealth()));
        placeholders.put("max", String.format("%.0f", maxHealth.getValue()));

        String message;

        if (attacker == null) {
            message = plugin.getMessages().formatMessage(Message.VILLAGER_HURT, placeholders);
        } else {
            placeholders.put("attacker", getEntityName(attacker));

            message = plugin.getMessages().formatMessage(Message.VILLAGER_ATTACKED, placeholders);
        }

        plugin.getServer().broadcastMessage(message);
    }

    public void announceVillagerHealth(Villager villager) {
        announceVillagerHealth(villager, null);
    }

    public void announceVillagerDeath(Villager villager) {
        if (!villager.isDead()) return;

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("villager", getVillagerName(villager));

        putVillagerLocation(villager, placeholders);

        String message;

        Entity killer = villager.getKiller();

        if (killer == null) {
            message = plugin.getMessages().formatMessage(Message.VILLAGER_DIED, placeholders);
        } else {
            placeholders.put("attacker", getEntityName(killer));

            message = plugin.getMessages().formatMessage(Message.VILLAGER_KILLED, placeholders);
        }

        plugin.getServer().broadcastMessage(message);
    }

    public void announceVillagerCured(Villager villager, @Nullable OfflinePlayer curedBy) {
        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("villager", getVillagerName(villager));
        placeholders.put("player", curedBy == null ? plugin.getMessages().getMessage(Message.SOMEONE) : curedBy.getName());

        putVillagerLocation(villager, placeholders);

        String message = plugin.getMessages().formatMessage(Message.VILLAGER_CURED, placeholders);

        plugin.getServer().broadcastMessage(message);
    }

    public void announceVillagerInfection(Villager villager) {
        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("villager", getVillagerName(villager));

        putVillagerLocation(villager, placeholders);

        String message = plugin.getMessages().formatMessage(Message.VILLAGER_INFECTED, placeholders);

        plugin.getServer().broadcastMessage(message);
    }

    public void announceDetection(ZombieVillager zombieVillager) {
        Location location = zombieVillager.getLocation();

        if (obfuscationEnabled) location = coordinatesObfuscator.obfuscate(location);

        HashMap<String, String> placeholders = new HashMap<>();

        placeholders.put("x", String.format("%.1f", location.getX()));
        placeholders.put("y", String.format("%.1f", location.getY()));
        placeholders.put("z", String.format("%.1f", location.getZ()));

        // This has no sense for zombie villagers
        // placeholders.put("villager", getEntityName(zombieVillager));

        String message = plugin.getMessages().formatMessage(Message.SPAWN_DETECTED, placeholders);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getLocation().distance(zombieVillager.getLocation()) <= detectionRange) {
                player.sendMessage(message);
            }
        }
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();

        for (Villager.Profession profession : Villager.Profession.values()) {
            String professionName = config.getString("villager_profession." + profession.name().toLowerCase(), null);

            if (professionName == null) {
                professionName = profession.name();
                professionName = professionName.substring(0, 1).toUpperCase() + professionName.substring(1).toLowerCase();
            }

            professionNames.put(profession, professionName);
        }

        minHealthPercentage = config.getDouble("health_alerts.percentage", 30);

        detectionRange = config.getDouble("detection.range", 128);

        coordinatesObfuscator.setMinRange(config.getDouble("detection.coordinates_obfuscation.min_range", 2));
        coordinatesObfuscator.setMaxRange(config.getDouble("detection.coordinates_obfuscation.max_range", 16));

        obfuscationEnabled = plugin.getFeatures().isFeatureEnabled(Feature.DETECTION_OBFUSCATION);
    }
}
