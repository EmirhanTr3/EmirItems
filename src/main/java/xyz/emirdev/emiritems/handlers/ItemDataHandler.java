package xyz.emirdev.emiritems.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.simpleyaml.configuration.file.YamlFile;
import xyz.emirdev.emiritems.EmirItems;
import xyz.emirdev.emiritems.Utils;
import xyz.emirdev.emiritems.item.CustomItem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
                Map<String, Object> fields = new HashMap<>();
                CustomItem item = entry.getValue();

                fields.put("material", item.material().toString());
                fields.put("name", Utils.unformat(item.name()));
                fields.put("description", item.description().stream().map(Utils::unformat).toList());
                fields.put("rarity", item.rarity());
                if (item.itemModel() != null) fields.put("item_model", item.itemModel());
                if (!item.enchantments().isEmpty()) {
                    Map<String, Integer> enchantments = new HashMap<>();
                    for (Map.Entry<Enchantment, Integer> enchantmentEntry : item.enchantments().entrySet()) {
                        enchantments.put(enchantmentEntry.getKey().key().asString(), enchantmentEntry.getValue());
                    }

                    fields.put("enchantments", enchantments);
                }
                fields.put("unbreakable", item.unbreakable());
                if (item.maxStackSize() != null) fields.put("max_stack_size", item.maxStackSize());
                if (item.maxDamage() != null) fields.put("max_damage", item.maxDamage());

                this.yamlFile.set(itemId, fields);
            }
        }
    }

    public Map<String, CustomItem> getItems() {
        Map<String, Object> map = this.yamlFile.getMapValues(false);
        Map<String, CustomItem> items = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Map<String, Object> fields = (Map<String, Object>) entry.getValue();

            if (fields.get("material") == null || fields.get("name") == null || fields.get("rarity") == null) {
                throw new NullPointerException(Utils.stringFormat(
                        "{1} was not found while loading item {0}.",
                        entry.getKey(),
                        fields.get("material") == null ? "Material" : fields.get("name") == null ? "Name" : "Rarity"
                ));
            }

            CustomItem item = new CustomItem(entry.getKey(), Material.valueOf((String) fields.get("material")))
                    .name(Utils.format((String) fields.get("name")))
                    .rarity((String) fields.get("rarity"))
                    .itemModel((String) fields.get("item_model"))
                    .unbreakable(fields.get("unbreakable") != null && (boolean) fields.get("unbreakable"));

            if (fields.get("enchantments") != null) item.enchantments((Map<String, Integer>) fields.get("enchantments"));
            if (fields.get("max_stack_size") != null) item.maxStackSize((int) fields.get("max_stack_size"));
            if (fields.get("max_damage") != null) item.maxDamage((int) fields.get("max_damage"));

            List<String> description = (List<String>) fields.get("description");
            if (description != null && !description.isEmpty()) item.description(description.stream().map(Utils::format).toList());

            items.put(item.itemId(), item);
        }

        return items;
    }
}
