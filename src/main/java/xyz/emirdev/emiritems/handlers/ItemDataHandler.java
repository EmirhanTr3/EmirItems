package xyz.emirdev.emiritems.handlers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.simpleyaml.configuration.file.YamlFile;
import xyz.emirdev.emiritems.EmirItems;
import xyz.emirdev.emiritems.item.CustomItem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemDataHandler {
    private static final File DATA_FILE = new File(EmirItems.get().getDataFolder(), "items.yml");

    private YamlFile yamlFile;

    public ItemDataHandler() {
        loadFile();
        saveFile();
    }

    public void loadFile() {
        this.yamlFile = new YamlFile(DATA_FILE);
        try {
            this.yamlFile.createOrLoadWithComments();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile() {
        try {
            this.yamlFile.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setItems(Map<String, CustomItem> items) {
        Map<String, CustomItem> currentItems = getItems();

        for (Map.Entry<String, CustomItem> entry : items.entrySet()) {
            String itemId = entry.getKey();
            if (!currentItems.get(itemId).equals(entry.getValue())) {
                Map<String, Object> fields = entry.getValue().deserialize();
                this.yamlFile.set(itemId, fields);
            }
        }
    }

    public Map<String, CustomItem> getItems() {
        Map<String, Object> map = this.yamlFile.getMapValues(false);
        Map<String, CustomItem> items = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Map<String, Object> fields = (Map<String, Object>) entry.getValue();

            CustomItem item = new CustomItem(entry.getKey(), fields);

            items.put(item.itemId(), item);
        }

        return items;
    }
}
