package com.github.sashasorokin.awareofvillagers.commands;

import com.github.sashasorokin.awareofvillagers.AwareOfVillagers;
import com.github.sashasorokin.awareofvillagers.messages.Message;
import org.bukkit.command.*;

import java.util.Collections;
import java.util.List;

public class GeneralCommand implements CommandExecutor, TabCompleter {
    private final AwareOfVillagers plugin;
    private boolean registered = false;

    public GeneralCommand(AwareOfVillagers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("awareofvillagers.command.use")) {
            sender.sendMessage(plugin.getMessages().formatMessage(Message.NO_PERMISSION));

            return true;
        }

        if (args.length != 1) {
            return false;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        if (!sender.hasPermission("awareofvillagers.command.reload")) {
            sender.sendMessage(plugin.getMessages().formatMessage(Message.NO_PERMISSION));

            return true;
        }

        plugin.reload();

        sender.sendMessage(plugin.getMessages().formatMessage(Message.RELOAD_COMPLETE));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return null;
        }

        return Collections.singletonList("reload");
    }

    public void register() {
        if (registered) throw new IllegalStateException("Command has already been registered");

        PluginCommand command = plugin.getCommand("awareofvillagers");

        if (command == null) {
            plugin.getLogger().warning("Failed to register command '/awareofvillagers': unable to retrieve command");

            return;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        registered = true;
    }
}
