package com.github.sashasorokin.awareofvillagers.events;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import com.github.sashasorokin.awareofvillagers.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final AwareOfVillagers plugin;

    public EntityDamageListener(AwareOfVillagers plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public void reload() {
        HandlerList.unregisterAll(this);

        if (plugin.getFeatures().isFeatureEnabled(Feature.HEALTH_MESSAGES)) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    private void onEntityHurt(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!entity.getType().equals(EntityType.VILLAGER)) return;

        // Do not announce health if villager has died after this event x_x
        if (entity.isDead()) return;

        if (event.getFinalDamage() == 0) return;

        if (event instanceof EntityDamageByEntityEvent) {
            plugin.getAnnouncements().announceVillagerHealth((Villager) entity, ((EntityDamageByEntityEvent) event).getDamager());

            return;
        }

        plugin.getAnnouncements().announceVillagerHealth((Villager) entity);
    }
}