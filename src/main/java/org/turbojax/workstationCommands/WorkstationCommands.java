package org.turbojax.workstationCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WorkstationCommands extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        HashMap<String, MenuType> menuTypes = new HashMap<>();
        menuTypes.put("wccraft", MenuType.CRAFTING);
        menuTypes.put("wcstonecutter", MenuType.STONECUTTER);
        menuTypes.put("wcloom", MenuType.LOOM);
        menuTypes.put("wccartography", MenuType.CARTOGRAPHY_TABLE);
        menuTypes.put("wcfurnace", MenuType.FURNACE);
        menuTypes.put("wcblastfurnace", MenuType.BLAST_FURNACE);
        menuTypes.put("wcsmoker", MenuType.SMOKER);
        menuTypes.put("wcsmithing", MenuType.SMITHING);
        menuTypes.put("wcanvil", MenuType.ANVIL);
        menuTypes.put("wcenchant", MenuType.ENCHANTMENT);
        menuTypes.put("wcgrindstone", MenuType.GRINDSTONE);
        menuTypes.put("wcbrew", MenuType.BREWING_STAND);

        // Loading commands if enabled in the config
        menuTypes.forEach((label, menuType) -> {
            if (getConfig().getBoolean(label + ".enabled")) {
                String basePermission = "wc." + label.substring(2);

                // Getting the command
                PluginCommand command = getCommand(label);
                assert command != null;
                command.setPermission(basePermission);

                // Assigning the Executor and TabCompleter
                command.setExecutor(new CommandExecutor() {
                    @Override
                    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
                        // Handling when the command is given a parameter
                        if (args.length == 1) {
                            // Verifying that the sender has the right permissions
                            if (!sender.hasPermission(basePermission + ".other")) {
                                sender.sendMessage("§cYou don't have permission to run this command!");
                                return false;
                            }

                            Player player = Bukkit.getPlayer(args[0]);
                            if (player == null || !player.isOnline()) {
                                sender.sendMessage("§cCould not find player \"" + args[0] + "\" online.");
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
                        if (sender.hasPermission(basePermission + ".other")) {
                            List<String> players = new ArrayList<String>();
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                players.add(player.getName());
                            }

                            return players;
                        }

                        return List.of();
                    }
                });

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
