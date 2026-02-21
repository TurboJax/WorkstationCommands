package org.turbojax.workstationCommands.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.turbojax.workstationCommands.WorkstationCommands;

public class ReloadCommand implements BasicCommand {
    private final WorkstationCommands plugin;

    public ReloadCommand(WorkstationCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        plugin.loadCommands();        
    }
}
