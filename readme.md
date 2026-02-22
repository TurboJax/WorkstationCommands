# WorkstationCommands
This is my first published minecraft plugin!  
With this plugin, you can allow players to open menus for any workstation from anywhere.
You can enable/disable commands in the config, as well as manage the aliases of each command.
Use `/wcreload` to reload the config without restarting.

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/plugin/workstationcommands)

## SPONGE ISSUES
The current version of the Sponge API does not allow for dynamic unregistration and registration of commands.  This means that /wcreload IS NOT A PART OF THE SPONGE IMPLEMENTATION!!!  

## Configuration
Configuration for this plugin is very simple!  
Commands default to being disabled if their config is not found, so you can remove a command from the config to disable it, or set the "enabled" flag to false.
You can also define a list of aliases for the command.  If this list is not found, it doesn't create any.

To enable only the crafting table, your config may look like this:  
```yml
wccraft:
  enabled: true
```
That's it, just two lines.  

Now let's say you dont want to run `/wccraft`, and you want a different command to open the gui.  
This is where the aliases config comes into play.  Let's add `/craft` as an alias to `/wccraft` (This is in the default config)  
```yml
wccraft:
  enabled: true
  aliases:
    - craft
```
Note that you don't need to include the forward slash when registering the alias.

## Commands  
- `/wcreload`: Reloads the configuration file.
- `/wccraft [target]`: Opens the crafting table GUI for the executor or the specified target.
- `/wcstonecutter [target]`: Opens the stonecutter GUI for the executor or the specified target.
- `/wcloom [target]`: Opens the loom GUI for the executor or the specified target.
- `/wccartography [target]`: Opens the cartography table GUI for the executor or the specified target.
- `/wcfurnace [target]`: Opens the furnace GUI for the executor or the specified target.
- `/wcblastfurnace [target]`: Opens the blast furnace GUI for the executor or the specified target.
- `/wcsmoker [target]`: Opens the smoker GUI for the executor or the specified target.
- `/wcsmithing [target]`: Opens the smithing table GUI for the executor or the specified target.
- `/wcanvil [target]`: Opens the anvil GUI for the executor or the specified target.
- `/wcenchant [target]`: Opens the enchanting table GUI for the executor or the specified target.
- `/wcgrindstone [target]`: Opens the grindstone GUI for the executor or the specified target.
- `/wcbrew [target]`: Opens the brewing stand GUI for the executor or the specified target.

## Player Permissions
**Permissions given to every player**
- `wc.craft`: Allows the player to use `/wccraft`.
- `wc.stonecutter`: Allows the player to use `/wcstonecutter`.
- `wc.loom`: Allows the player to use `/wcloom`.
- `wc.cartography`: Allows the player to use `/wccartography`.
- `wc.furnace`: Allows the player to use `/wcfurnace`.
- `wc.blastfurnace`: Allows the player to use `/wcblastfurnace`.
- `wc.smoker`: Allows the player to use `/wcsmoker`.
- `wc.smithing`: Allows the player to use `/wcsmithing`.
- `wc.anvil`: Allows the player to use `/wcanvil`.
- `wc.enchant`: Allows the player to use `/wcenchant`.
- `wc.grindstone`: Allows the player to use `/wcgrindstone`.
- `wc.brew`: Allows the player to use `/wcbrew`.

## Operator Permissions
**Permissions given to every operator**
- `wc.reload`: Allows players to use `/wcreload`.
- `wc.craft.other`: Allows the player to use `/craft [target]`.
- `wc.stonecutter.other`: Allows the player to use `/stonecutter [target]`.
- `wc.loom.other`: Allows the player to use `/loom [target]`.
- `wc.cartography.other`: Allows the player to use `/cartography [target]`.
- `wc.furnace.other`: Allows the player to use `/furnace [target]`.
- `wc.blastfurnace.other`: Allows the player to use `/blastfurnace [target]`.
- `wc.smoker.other`: Allows the player to use `/smoker [target]`.
- `wc.smithing.other`: Allows the player to use `/smithing [target]`.
- `wc.anvil.other`: Allows the player to use `/anvil [target]`.
- `wc.enchant.other`: Allows the player to use `/enchant [target]`.
- `wc.grindstone.other`: Allows the player to use `/grindstone [target]`.
- `wc.brew.other`: Allows the player to use `/brew [target]`.