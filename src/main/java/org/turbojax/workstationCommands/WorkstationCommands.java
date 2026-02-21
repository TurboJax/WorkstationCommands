package org.turbojax.workstationCommands;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;
import org.turbojax.workstationCommands.command.ReloadCommand;
import org.turbojax.workstationCommands.command.WorkstationExecutor;

public final class WorkstationCommands extends JavaPlugin {
    private final HashMap<String, MenuType> menuTypes;

    public WorkstationCommands() {
        this.menuTypes = new HashMap<>();
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
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Registering the commands
        loadCommands();

        // Registering the reload command
        registerCommand("wcreload", new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    public void loadCommands() {
        // Unregistering all commands
        CommandMap map = getServer().getCommandMap();
        for (String label : menuTypes.keySet()) {
            Command cmd = map.getCommand(label);
            if (cmd == null) continue;
            cmd.unregister(map);
        }
        
        // Loading each command
        menuTypes.forEach((label, menuType) -> {
            // Skipping disabled commands
            if (!getConfig().getBoolean(label + ".enabled")) return;

            // Getting the command
            PluginCommand command = getCommand(label);
            assert command != null;

            // Registering permissions
            String basePermission = "wc." + label.substring(2);
            command.setPermission(basePermission);

            // Assigning the Executor and TabCompleter
            WorkstationExecutor executor = new WorkstationExecutor(basePermission + ".other", menuType);
            command.setExecutor(executor);
            command.setTabCompleter(executor);

            // Registering aliases
            for (String alias : getConfig().getStringList(label + ".aliases")) {
                getServer().getCommandMap().register(alias, "workstationcommands", command);
            }
        });
    }
}
