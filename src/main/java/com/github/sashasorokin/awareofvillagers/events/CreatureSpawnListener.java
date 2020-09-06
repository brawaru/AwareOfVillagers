package com.github.sashasorokin.awareofvillagers.events;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import com.github.sashasorokin.awareofvillagers.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {
    private final AwareOfVillagers plugin;
    private final CreatureSpawnHandlers handlers;

    public CreatureSpawnListener(AwareOfVillagers plugin, CreatureSpawnHandlers handlers) {
        this.plugin = plugin;
        this.handlers = handlers;
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (!entity.getType().equals(EntityType.ZOMBIE_VILLAGER)) return;

        switch (event.getSpawnReason()) {
            case NATURAL:
            case DEFAULT: {
                plugin.getAnnouncements().announceDetection((ZombieVillager) entity);

                return;
            }
            case INFECTION: {
                this.handlers.onVillagerInfected((ZombieVillager) entity);

                return;
            }
            default:
                break;
        }
    }

    public void reload() {
        HandlerList.unregisterAll(this);

        if (plugin.getFeatures().isFeatureEnabled(Feature.DETECTION)) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    public interface CreatureSpawnHandlers {
        /**
         * Event that is called whenever villager gets infected
         *
         * @param zombieVillager Zombie villager that villager was converted into
         */
        void onVillagerInfected(ZombieVillager zombieVillager);
    }
}
