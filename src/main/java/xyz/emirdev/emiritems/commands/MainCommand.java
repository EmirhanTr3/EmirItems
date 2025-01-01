package xyz.emirdev.emiritems.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import xyz.emirdev.emiritems.EmirItems;
import xyz.emirdev.emiritems.Utils;
import xyz.emirdev.emiritems.item.CustomItem;

@Command({"emiritems", "ei"})
@CommandPermission("emiritems.command")
public class MainCommand {

    @Subcommand("reload")
    @CommandPermission("emiritems.command.reload")
    public void reload(CommandSender sender) {
        long time = System.currentTimeMillis();

        EmirItems.get().getPluginConfig().loadFile();
        EmirItems.get().getItemManager().reload();

        Utils.sendMessage(sender,
                "<#00eeee>Reloaded configuration in <#00ccff>{0}<#00eeee>ms.",
                System.currentTimeMillis() - time
        );
    }

    @Subcommand("get")
    @CommandPermission("emiritems.command.get")
    public void get(Player player, CustomItem item) {
        player.getInventory().addItem(item.build());

        Utils.sendMessage(player,
                "<#00eeee>You have been given item <u>{0}</u>.",
                item.itemId()
        );
    }
}
