package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class langsManager{
    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    File dataFolder = plugin.getDataFolder();
    File langsForlder = new File(dataFolder + File.separator + "langs" + File.separator);

    public void createLanguageFile(String language){
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }
        if(!langsForlder.exists()){
            langsForlder.mkdir();
        }

        File file = new File(langsForlder,language + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getLanguageFile(String language){
        return new File(langsForlder,language + ".yml");
    }

    public void saveLanguageConfig(FileConfiguration languageConfig, String language){
        try {
            languageConfig.save(getLanguageFile(language));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String language, String message, String defaultMessage){
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(getLanguageFile(language));

        String translation = langConfig.getString(message, defaultMessage);

        langConfig.set(message, translation);
        saveLanguageConfig(langConfig, language);

        return translation;
    }
}
