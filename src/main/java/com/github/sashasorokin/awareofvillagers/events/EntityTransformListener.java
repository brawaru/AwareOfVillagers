package com.github.sashasorokin.awareofvillagers.events;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import com.github.sashasorokin.awareofvillagers.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class EntityTransformListener implements Listener {
    private static final Feature[] entityTransformFeatures = new Feature[]{
            Feature.INFECTED_MESSAGES,
            Feature.CURED_MESSAGES,
    };
    private final AwareOfVillagers plugin;

    public EntityTransformListener(AwareOfVillagers plugin) {
        this.plugin = plugin;
        this.reload();
    }

    @EventHandler
    public void onEntityTransform(EntityTransformEvent event) {
        Entity transformedInto = event.getTransformedEntity();
        Entity transformedFrom = event.getEntity();

        plugin.getLogger().info(String.format("EntityTransformEvent: entity %s, transformed: %s, reason: %s", event.getEntity(), event.getTransformedEntity(), event.getTransformReason()));

        switch (event.getTransformReason()) {
            case CURED: {
                ZombieVillager zombieVillager = (ZombieVillager) transformedFrom;
                Villager curedVillager = (Villager) transformedInto;

                plugin.getAnnouncements().announceVillagerCured(curedVillager, zombieVillager.getConversionPlayer());

                return;
            }
            case INFECTION: {
                // THIS IS BUGGED IN <1.16.2, SEE SPIGOT-6107
                // Villager infectedVillager = (Villager) transformedFrom;
                // ZombieVillager zombieVillager = (ZombieVillager) transformedInto;
                // plugin.getAnnouncements().announceVillagerInfection(infectedVillager);

                return;
            }
            default:
                break;
        }
    }

    public void reload() {
        HandlerList.unregisterAll(this);

        if (!plugin.getFeatures().someEnabled(entityTransformFeatures)) {
            return;
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
