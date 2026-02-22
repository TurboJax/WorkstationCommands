package org.turbojax.workstationCommands;

import com.google.inject.Inject;

import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("workstationcommands")
public class WorkstationCommands {
    private final PluginContainer pluginContainer;
    private final Logger logger;

    private final HashMap<String,Supplier<? extends ContainerType>> containerTypes;
    private final HashMap<String,Component> descriptions;

    YamlConfigurationLoader defaultConfigLoader;

    @Inject
    public WorkstationCommands(PluginContainer pluginContainer, Logger logger) {
        this.pluginContainer = pluginContainer;
        this.logger = logger;

        this.containerTypes = new HashMap<>();
        containerTypes.put("wccraft", ContainerTypes.CRAFTING);
        containerTypes.put("wcstonecutter", ContainerTypes.STONECUTTER);
        containerTypes.put("wcloom", ContainerTypes.LOOM);
        containerTypes.put("wccartography", ContainerTypes.CARTOGRAPHY_TABLE);
        containerTypes.put("wcfurnace", ContainerTypes.FURNACE);
        containerTypes.put("wcblastfurnace", ContainerTypes.BLAST_FURNACE);
        containerTypes.put("wcsmoker", ContainerTypes.SMOKER);
        containerTypes.put("wcsmithing", ContainerTypes.SMITHING);
        containerTypes.put("wcanvil", ContainerTypes.ANVIL);
        containerTypes.put("wcenchant", ContainerTypes.ENCHANTMENT);
        containerTypes.put("wcgrindstone", ContainerTypes.GRINDSTONE);
        containerTypes.put("wcbrew", ContainerTypes.BREWING_STAND);

        descriptions = new HashMap<>();
        descriptions.put("wccraft", Component.text("Opens the crafting table GUI"));
        descriptions.put("wcstonecutter", Component.text("Opens the stonecutter GUI"));
        descriptions.put("wcloom", Component.text("Opens the loom GUI"));
        descriptions.put("wccartography", Component.text("Opens the cartography table GUI"));
        descriptions.put("wcfurnace", Component.text("Opens the furnace GUI"));
        descriptions.put("wcblastfurnace", Component.text("Opens the blast furnace GUI"));
        descriptions.put("wcsmoker", Component.text("Opens the smoker GUI"));
        descriptions.put("wcsmithing", Component.text("Opens the smithing table GUI"));
        descriptions.put("wcanvil", Component.text("Opens the anvil GUI"));
        descriptions.put("wcenchant", Component.text("Opens the enchanting table GUI"));
        descriptions.put("wcgrindstone", Component.text("Opens the grindstone GUI"));
        descriptions.put("wcbrew", Component.text("Opens the brewing stand GUI"));

        // Loading the default config
        defaultConfigLoader = YamlConfigurationLoader.builder()
            .path(Path.of("config.yml"))
            .build();
    }

    @Listener
    private void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        // Register a simple command
        // When possible, all commands should be registered within a command register event
        final Parameter.Value<ServerPlayer> nameParam = Parameter.player().key("name").build();

        for (String label : containerTypes.keySet()) {
            // Getting the configs
            String[] aliases = {};
            try {
                CommentedConfigurationNode config = defaultConfigLoader.load();

                // Skipping disabled commands
                if (!config.node(label, "enabled").getBoolean()) continue;

                // TODO: Check if there needs to be a default option for this config call.
                aliases = config.node(label, "aliases").getList(String.class).toArray(String[]::new);
            } catch (SerializationException err) {
                logger.log(Level.ERROR, "Failed to deserialize aliases for " + label + ".aliases");
            } catch (ConfigurateException err) {
                logger.log(Level.ERROR, "Failed to load YAML from the default config.");
            }

            // Getting the permissions from the label
            String perm = "wc." + label.substring(2);
            String otherPerm = perm + ".other";

            // Registering the command
            event.register(this.pluginContainer, Command.builder()
                .addParameter(nameParam)
                .shortDescription(descriptions.get(label))
                .permission((String) null)
                .executor(ctx -> {
                    // Getting the player argument
                    Optional<ServerPlayer> playerArg = ctx.one(nameParam);
                    if (playerArg.isPresent()) {
                        // Making sure the sender can use the command on other players
                        if (!ctx.cause().hasPermission(otherPerm)) {
                            return CommandResult.error(Component.text("You don't have permission to run this command!"));
                        }

                        // Opening the menu for the target
                        InventoryMenu.of(ViewableInventory.builder().type(containerTypes.get(label)).completeStructure().build()).open(playerArg.get());
                        return CommandResult.success();
                    }

                    // Making sure the sender can use the command
                    if (!ctx.cause().hasPermission(perm) && !ctx.cause().hasPermission(otherPerm)) {
                        return CommandResult.error(Component.text("You don't have permission to run this command!"));
                    }

                    // Making sure the sender is a player
                    if (!(ctx.cause().root() instanceof ServerPlayer player)) {
                        return CommandResult.error(Component.text("This command can ony be used by a player"));
                    }

                    // Opening the menu for the sender
                    InventoryMenu.of(ViewableInventory.builder().type(containerTypes.get(label)).completeStructure().build()).open(player);
                    return CommandResult.success();
                })
                .build(), label, aliases);
        }
    }
}
