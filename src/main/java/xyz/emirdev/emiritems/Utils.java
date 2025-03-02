package xyz.emirdev.emiritems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Utils {
    /**
     *
     * @param string string to format
     * @param args arguments that will be replaced in the string. argument format: {0}
     * @return formatted string
     */
    public static String stringFormat(String string, Object... args) {
        for (int i = 0; i < args.length; i++) {
            string = string.replaceFirst(
                    "\\{"+ i + "}",
                    Objects.requireNonNullElse(
                                    args[i],
                                    "null"
                            )
                            .toString()
                            .replaceAll("([$\\\\])", "\\$1")
            );
        }
        return string;
    }

    public static String unformat(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    public static Component format(String string, Object... args) {
        return MiniMessage.miniMessage().deserialize(stringFormat(string, args));
    }

    public static void sendMessage(CommandSender sender, String string, Object... args) {
        sender.sendMessage(format(string, args));
    }

    public static void sendError(CommandSender sender, String string, Object... args) {
        sender.sendMessage(format("<#ee4444>" + string, args));
    }

    public static void broadcast(String string, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, string, args);
        }
        sendMessage(Bukkit.getConsoleSender(), string, args);
    }

    public static void broadcastWithPermission(String perm, String string, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(perm)) {
                sendMessage(player, string, args);
            }
        }
        sendMessage(Bukkit.getConsoleSender(), string, args);
    }

    public static String convertComponentToLegacyString(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
