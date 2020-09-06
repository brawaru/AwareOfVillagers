package com.github.sashasorokin.awareofvillagers.messages;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Messages {
    private final AwareOfVillagers plugin;
    private final HashMap<Message, String> messages = new HashMap<>();

    public Messages(AwareOfVillagers plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        for (Message message : Message.values()) {
            String value = plugin.getConfig().getString(message.getPath(), message.getDefaultMessage());

            messages.put(message, value);
        }
    }

    public String formatMessage(Message message) {
        return ChatColor.translateAlternateColorCodes('&', getMessage(message));
    }

    public String formatMessage(Message message, Map<String, String> placeholders) {
        String formattedMessage = getMessage(message);

        for (String placeholder : placeholders.keySet()) {
            String replacementPattern = Pattern.quote("%" + placeholder);
            String replacementValue = placeholders.get(placeholder);


            Bukkit.getLogger().info("Replacing \"" + replacementPattern + "\" as \"" + replacementValue + "\"");

            formattedMessage = formattedMessage.replaceAll(replacementPattern, replacementValue);
        }

        return ChatColor.translateAlternateColorCodes('&', formattedMessage);
    }

    public String getMessage(Message message) {
        return messages.get(message);
    }
}
