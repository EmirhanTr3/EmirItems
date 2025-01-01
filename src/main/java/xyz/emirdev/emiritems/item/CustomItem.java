package xyz.emirdev.emiritems.item;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.emirdev.emiritems.EmirItems;
import xyz.emirdev.emiritems.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItem {
    public static final NamespacedKey ITEM_TAG = new NamespacedKey(EmirItems.get(), "item");

    private final String itemId;
    private final Material material;
    private Component name;
    private List<Component> description = new ArrayList<>();
    private String rarity;
    private Component rarityText;
    private String itemModel;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private boolean unbreakable = false;
    private Integer maxStackSize = null;
    private Integer maxDamage = null;

    public CustomItem(String itemId, Material material) {
        this.itemId = itemId;
        this.material = material;
    }

    public String itemId() {
        return this.itemId;
    }
    
    public Material material() {
        return this.material;
    }

    public Component name() {
        return name;
    }

    public CustomItem name(Component name) {
        this.name = name;
        return this;
    }

    public List<Component> description() {
        return description;
    }

    public CustomItem description(List<Component> description) {
        this.description = description;
        return this;
    }

    public String rarity() {
        return rarity;
    }

    public CustomItem rarity(String rarity) {
        this.rarity = rarity;
        this.rarityText = Utils.format(EmirItems.get().getPluginConfig().getRarities().get(rarity));
        return this;
    }

    public String itemModel() {
        return this.itemModel;
    }

    public CustomItem itemModel(String itemModel) {
        this.itemModel = itemModel;
        return this;
    }

    public Map<Enchantment, Integer> enchantments() {
        return this.enchantments;
    }

    public CustomItem enchantments(Map<String, Integer> enchantments) {
        Map<Enchantment, Integer> keyEnchantments = new HashMap<>();
        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

        for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = enchantmentRegistry.getOrThrow(Key.key(entry.getKey()));
            keyEnchantments.put(enchantment, entry.getValue());
        }

        this.enchantments = keyEnchantments;
        return this;
    }

    public boolean unbreakable() {
        return this.unbreakable;
    }

    public CustomItem unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public Integer maxStackSize() {
        return this.maxStackSize;
    }

    public CustomItem maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public Integer maxDamage() {
        return this.maxDamage;
    }

    public CustomItem maxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public ItemStack build() {
        if (material == null || name == null || rarity == null) {
            throw new NullPointerException(Utils.stringFormat(
                    "{1} was not found while building item {0}.",
                    itemId,
                    material == null ? "Material" : name == null ? "Name" : "Rarity"
            ));
        }
        ItemStack item = ItemStack.of(material);
        ItemMeta meta = item.getItemMeta();
        meta.itemName(name);
        meta.setUnbreakable(unbreakable);
        if (maxStackSize != null) meta.setMaxStackSize(maxStackSize);
        if (meta.hasMaxStackSize() && meta.getMaxStackSize() == 1 && maxDamage != null) {
            Damageable damageable = (Damageable) meta;
            damageable.setMaxDamage(maxDamage);
        }
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        List<Component> lore = new ArrayList<>();
        lore.addAll(description);
        lore.add(Component.space());
        lore.add(rarityText);
        meta.lore(lore.stream().map(line -> line.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList());

        meta.getPersistentDataContainer().set(ITEM_TAG, PersistentDataType.STRING, itemId);

        item.setItemMeta(meta);
        return item;
    }
}
