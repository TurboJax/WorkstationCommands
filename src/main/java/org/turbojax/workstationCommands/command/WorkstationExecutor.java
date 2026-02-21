package org.turbojax.workstationCommands.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorkstationExecutor implements TabExecutor {
    private final String otherPermission;
    private final MenuType menuType;

    public WorkstationExecutor(String otherPermission, MenuType menuType) {
        this.otherPermission = otherPermission;
        this.menuType = menuType;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String @NotNull [] args) {
        // Handling when the command is given a parameter
        if (args.length == 1) {
            // Verifying that the sender has the right permissions
            if (!sender.hasPermission(otherPermission)) {
                sender.sendMessage("You don't have permission to run this command!");
                return false;
            }

            Player player = Bukkit.getPlayer(args[0]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage("Could not find player \"" + args[0] + "\" online.");
                return false;
            }

            menuType.create(player, null).open();
            return true;
        }

        // Opening the GUI
        if (sender instanceof Player player) {
            menuType.create(player, null).open();
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // Skipping if the executor can't open the menu for another player
        if (!sender.hasPermission(otherPermission)) return List.of();

        List<String> players = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        return players;
    }
}
