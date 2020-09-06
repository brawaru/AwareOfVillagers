package com.github.sashasorokin.awareofvillagers;

import com.github.sashasorokin.awareofvillagers.commands.GeneralCommand;
import com.github.sashasorokin.awareofvillagers.messages.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public final class AwareOfVillagers extends JavaPlugin {
    private FeatureConfig features;
    private Messages messages;
    private Announcements announcements;
    private Events events;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        features = new FeatureConfig(this);
        messages = new Messages(this);
        announcements = new Announcements(this);
        events = new Events(this);

        new GeneralCommand(this).register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        reloadConfig();

        features.reload();
        messages.reload();
        announcements.reload();
        events.reload();
    }

    public FeatureConfig getFeatures() {
        return features;
    }

    public Messages getMessages() {
        return messages;
    }

    public Announcements getAnnouncements() {
        return announcements;
    }
}
