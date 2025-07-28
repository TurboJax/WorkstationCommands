package org.turbojax.workstationCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class WorkstationCommands extends JavaPlugin {
    private static WorkstationCommands instance;
    public static Logger logger = LoggerFactory.getLogger("WorkstationCommands");

    public static WorkstationCommands getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        HashMap<String,MenuType> menuTypes = new HashMap<>();
        menuTypes.put("craftingtable", MenuType.CRAFTING);
        menuTypes.put("stonecutter", MenuType.STONECUTTER);
        menuTypes.put("loom", MenuType.LOOM);
        menuTypes.put("cartographytable", MenuType.CARTOGRAPHY_TABLE);
        menuTypes.put("furnace", MenuType.FURNACE);
        menuTypes.put("blastfurnace", MenuType.BLAST_FURNACE);
        menuTypes.put("smoker", MenuType.SMOKER);
        menuTypes.put("smithingtable", MenuType.SMITHING);
        menuTypes.put("anvil", MenuType.ANVIL);
        menuTypes.put("enchantingtable", MenuType.ENCHANTMENT);
        menuTypes.put("grindstone", MenuType.GRINDSTONE);
        menuTypes.put("brewingstand", MenuType.BREWING_STAND);

        // Loading commands if enabled in the config

        menuTypes.forEach((label, menuType) -> {
            if (getConfig().getBoolean(label + ".enabled")) {
                // Getting the command
                PluginCommand command = getCommand(label);
                assert command != null;
                command.setPermission(getConfig().getString(label + ".permission.name"));

                // Assigning the Executor and TabCompleter
                command.setExecutor(new CommandExecutor() {
                    @Override
                    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
                        // Handling when the command is given a parameter
                        if (args.length == 1) {
                            // Verifying that the sender has the right permissions

                            if (!sender.hasPermission(getConfig().getString(label + ".other-permission.name"))) {
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
                });
                command.setTabCompleter(new TabCompleter() {
                            @Override
                            public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
                                if (sender.hasPermission(getConfig().getString(label + ".other-permission.name"))) {
                                    List<String> players = new ArrayList<String>();
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        players.add(player.getName());
                                    }

                                    return players;
                                }

                                return List.of();
                            }
                        }
                );

                // Registering aliases
                for (String alias : getConfig().getStringList(label + ".aliases")) {
                    getServer().getCommandMap().register(alias, "workstationcommands", command);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
