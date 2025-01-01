package xyz.emirdev.emiritems.handlers;

import org.simpleyaml.configuration.MemorySection;
import org.simpleyaml.configuration.file.YamlFile;
import xyz.emirdev.emiritems.EmirItems;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private static final File DATA_FILE = new File(EmirItems.get().getDataFolder(), "config.yml");

    private YamlFile yamlFile;

    public ConfigHandler() {
        loadFile();

        Map<String, String> rarities = new HashMap<>();
        rarities.put("COMMON", "ᴄᴏᴍᴍᴏɴ");
        rarities.put("UNCOMMON", "ᴜɴᴄᴏᴍᴍᴏɴ");
        rarities.put("RARE", "ʀᴀʀᴇ");
        rarities.put("EPIC", "ᴇᴘɪᴄ");
        rarities.put("LEGENDARY", "ʟᴇɢᴇɴᴅᴀʀʏ");
        rarities.put("MYTHICAL", "ᴍʏᴛʜɪᴄᴀʟ");
        this.yamlFile.addDefault("rarities", rarities);

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

    public Map<String, String> getRarities() {
        Map<String, Object> map = ((MemorySection) this.yamlFile.get("rarities")).getValues(false);
        Map<String, String> returnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            returnMap.put(entry.getKey(), (String) entry.getValue());
        }
        return returnMap;
    }

}