package org.turbojax.workstationCommands.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.turbojax.workstationCommands.WorkstationCommands;

public class ReloadCommand implements CommandExecutor {
    private final WorkstationCommands plugin;

    public ReloadCommand(WorkstationCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.loadCommands();
        
        return true;
    }
}
