package xyz.emirdev.emiritems.item;

import xyz.emirdev.emiritems.handlers.ItemDataHandler;

import java.util.Map;

public class ItemManager {
    private final ItemDataHandler dataHandler;
    private Map<String, CustomItem> items;

    public ItemManager(ItemDataHandler dataHandler) {
        this.dataHandler = dataHandler;
        this.items = dataHandler.getItems();
    }

    public void reload() {
        dataHandler.loadFile();
        this.items = dataHandler.getItems();
    }

    public Map<String, CustomItem> getItems() {
        return items;
    }

    public CustomItem getItem(String itemId) {
        return items.get(itemId);
    }
}
