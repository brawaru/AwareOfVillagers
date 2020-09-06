package com.github.sashasorokin.awareofvillagers;

import com.github.sashasorokin.awareofvillagers.events.CreatureSpawnListener;
import com.github.sashasorokin.awareofvillagers.events.EntityDamageListener;
import com.github.sashasorokin.awareofvillagers.events.EntityDeathListener;
import com.github.sashasorokin.awareofvillagers.events.EntityTransformListener;
import org.bukkit.entity.ZombieVillager;

public class Events implements CreatureSpawnListener.CreatureSpawnHandlers {
    private final CreatureSpawnListener creatureSpawnListener;
    private final EntityDamageListener entityDamageListener;
    private final EntityDeathListener entityDeathListener;
    private final EntityTransformListener entityTransformListener;

    public Events(AwareOfVillagers plugin) {
        entityDamageListener = new EntityDamageListener(plugin);
        entityDeathListener = new EntityDeathListener(plugin);
        creatureSpawnListener = new CreatureSpawnListener(plugin, this);
        entityTransformListener = new EntityTransformListener(plugin);

        reload();
    }

    public void reload() {
        // Health alerts require entity damage listener
        entityTransformListener.reload();
        entityDamageListener.reload();
        entityDeathListener.reload();
        creatureSpawnListener.reload();
    }

    @Override
    public void onVillagerInfected(ZombieVillager infectedVillager) {
        entityDeathListener.registerInfection(infectedVillager);
    }
}
