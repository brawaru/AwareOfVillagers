package com.github.sashasorokin.awareofvillagers.events;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import com.github.sashasorokin.awareofvillagers.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class EntityDeathListener implements Listener {
    private final AwareOfVillagers plugin;
    private final List<ZombieVillager> recentInfections = new ArrayList<>();

    public EntityDeathListener(AwareOfVillagers plugin) {
        this.plugin = plugin;

        this.reload();
    }

    public void registerInfection(ZombieVillager infectedVillager) {
        recentInfections.add(infectedVillager);

        new RecentInfectionRemoval(infectedVillager).runTaskLater(plugin, 5);
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (!entity.getType().equals(EntityType.VILLAGER)) return;

        Villager villager = (Villager) entity;

        new DeathAnnounce(villager).runTaskLater(plugin, 1);
    }

    public void reload() {
        HandlerList.unregisterAll(this);

        if (plugin.getFeatures().isFeatureEnabled(Feature.DEATH_MESSAGES)) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    private class RecentInfectionRemoval extends BukkitRunnable {
        private final ZombieVillager zombieVillager;

        public RecentInfectionRemoval(ZombieVillager zombieVillager) {
            this.zombieVillager = zombieVillager;
        }

        @Override
        public void run() {
            recentInfections.remove(zombieVillager);
        }
    }

    public class DeathAnnounce extends BukkitRunnable {
        private final Villager villager;

        public DeathAnnounce(Villager villager) {
            this.villager = villager;
        }

        @Override
        public void run() {
            for (ZombieVillager infectedVillager : recentInfections) {
                if (infectedVillager.getLocation().distance(villager.getLocation()) <= 1) {
                    plugin.getAnnouncements().announceVillagerInfection(villager);

                    return;
                }
            }

            plugin.getAnnouncements().announceVillagerDeath(villager);
        }
    }
}
