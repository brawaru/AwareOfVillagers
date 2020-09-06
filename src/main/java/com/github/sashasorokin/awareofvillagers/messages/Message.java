package com.github.sashasorokin.awareofvillagers.messages;

public enum Message {
    VILLAGER_CURED("&aHooray! &l%player&r&a has cured &l%villager&r&a of infection"),
    VILLAGER_HURT("&6&l%villager&r&6 is hurt (%current / %max) at %x, %y, %z"),
    VILLAGER_ATTACKED("&6&l%villager&r&6 (%current / %max) is attacked by %attacker at %x, %y, %z"),
    VILLAGER_DIED("&cVillager &l%villager&r&c has died"),
    VILLAGER_KILLED("&c&l%villager&r&c has been killed by %attacker"),
    VILLAGER_INFECTED("&c&l%villager&r&c has been infected at %x, %y, %z"),
    SPAWN_DETECTED("&cZombie villager detected around %x, %y, %z"),
    NO_PERMISSION("&cYou don't have permission to execute this command"),
    RELOAD_COMPLETE("&aReload complete!"),
    SOMEONE("Someone"),
    ;

    private final String defaultMessage;

    Message(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getPath() {
        return "messages." + toString().toLowerCase();
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
