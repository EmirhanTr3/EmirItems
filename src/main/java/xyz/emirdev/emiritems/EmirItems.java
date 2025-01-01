package xyz.emirdev.emiritems;

import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import xyz.emirdev.emiritems.commands.MainCommand;
import xyz.emirdev.emiritems.handlers.ConfigHandler;
import xyz.emirdev.emiritems.handlers.ItemDataHandler;
import xyz.emirdev.emiritems.item.CustomItem;
import xyz.emirdev.emiritems.item.ItemManager;
import xyz.emirdev.emiritems.parameters.CustomItemParameterType;

import java.util.List;

public final class EmirItems extends JavaPlugin {
    private static EmirItems instance;
    private ConfigHandler config;
    private ItemDataHandler itemDataHandler;
    private ItemManager itemManager;

    public static EmirItems get() {
        return instance;
    }

    public ConfigHandler getPluginConfig() {
        return config;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        config = new ConfigHandler();
        itemDataHandler = new ItemDataHandler();
        itemManager = new ItemManager(itemDataHandler);

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .parameterTypes(parameters -> {
                    parameters.addParameterType(CustomItem.class, new CustomItemParameterType());
                })
                .build();

        List.of(
                new MainCommand()
        ).forEach(lamp::register);

    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
