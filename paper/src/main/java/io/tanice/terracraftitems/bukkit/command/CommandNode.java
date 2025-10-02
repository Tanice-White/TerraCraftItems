package io.tanice.terracraftitems.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandNode {

    public abstract String getName();

    public abstract boolean execute(CommandSender sender, String[] args);

    public String getDescription() {
        return "";
    }

    public String getUsage() {
        return "";
    }

    public String getPermission() {
        return "terracraftitems.command." + getName().toLowerCase();
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public void onload(){}

    public void unload(){}

    public void reload(){}

    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    protected List<String> playerList(String preStr) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.startsWith(preStr))
                .collect(Collectors.toList());
    }
}
