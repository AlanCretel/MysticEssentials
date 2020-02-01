package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

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

    public FileConfiguration getLanguageConfig(String language){
        return YamlConfiguration.loadConfiguration(new File(langsForlder + File.separator + language + ".yml"));
    }

    public void saveLanguageConfig(FileConfiguration languageConfig, String language){
        try {
            languageConfig.save(getLanguageFile(language));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String language, String message){
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(getLanguageFile(language));
        String defaultMessage = getDefaultMessage(language, message);
        String translation = langConfig.getString(message, defaultMessage);

        langConfig.set(message, translation);
        saveLanguageConfig(langConfig, language);

        return translation;
    }

    private String getDefaultMessage(String languageName, String message){
        if(isDefaultLanguage(languageName)){
            InputStream languageClassStream = plugin.getClass().getResourceAsStream("/langs/" + languageName + ".yml");
            InputStreamReader streamReader = new InputStreamReader(languageClassStream);
            FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(streamReader);
            return defaultLanguageConfig.getString(message);
        }
        else{
            InputStream languageClassStream = plugin.getClass().getResourceAsStream("/langs/en_us.yml");
            InputStreamReader streamReader = new InputStreamReader(languageClassStream);
            FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(streamReader);
            return defaultLanguageConfig.getString(message);
        }
    }

    public ArrayList<String> getAvailableLanguages(){
        ArrayList<String> availableLanguages = new ArrayList<>();
        File[] languageFiles = langsForlder.listFiles();
        if (languageFiles != null) {
            for (File languageFile : languageFiles) {
                if(languageFile.getName().endsWith(".yml")){
                    availableLanguages.add(languageFile.getName().replace(".yml", "").replaceAll(" ", "_"));
                }
            }
        }
        return availableLanguages;
    }

    public boolean isAvailableLanguage(String languageName){
        return getAvailableLanguages().contains(languageName);
    }

    public void setupDefaultLanguages(){
        ArrayList<String> defaultLanguages = getDefaultLanguages();
        for (String defaultLanguage : defaultLanguages){
            File languageFile = new File(langsForlder, defaultLanguage + ".yml");
            if(!languageFile.exists()){
                plugin.saveResource("langs" + File.separator + defaultLanguage + ".yml", false);
                FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(languageFile);
                languageConfig.options().copyDefaults(true);
                try {
                    languageConfig.save(languageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateLanguagesConfigs(){
        ArrayList<String> availableLanguages = getAvailableLanguages();
        updateDefaultLanguagesConfigs();
        for (String availableLanguage : availableLanguages) {
            boolean isDefaultLanguage = isDefaultLanguage(availableLanguage);
            if(!isDefaultLanguage){
                plugin.getServer().getConsoleSender().sendMessage("Checking: " + availableLanguage + " language config.");
                updateCustomLanguageConfig(availableLanguage);
            }
        }
    }

    private void updateDefaultLanguagesConfigs(){
        ArrayList<String> defaultLanguages = new ArrayList<>();
        for (String availableLanguage : getAvailableLanguages()) {
            if(isDefaultLanguage(availableLanguage)){
                defaultLanguages.add(availableLanguage);
            }
        }
        for (String defaultLanguage : defaultLanguages) {
            plugin.getServer().getConsoleSender().sendMessage("Checking: " + defaultLanguage + " language config.");
            updateDefaultLanguageConfig(defaultLanguage);
        }
    }

    private void updateDefaultLanguageConfig(String languageName){
        File languageFile = new File(langsForlder, languageName + ".yml");
        if(isDefaultLanguage(languageName)){
            InputStream languageClassStream = plugin.getClass().getResourceAsStream("/langs/" + languageName + ".yml");
            if(languageClassStream == null){
                return;
            }
            Reader configReader = new InputStreamReader(languageClassStream, StandardCharsets.UTF_8);
            FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(configReader);
            if(!languageFile.exists()){
                createLanguageFile(languageName);
            }
            FileConfiguration languageConfig = getLanguageConfig(languageName);
            Set<String> defaultMessageNames = defaultLanguageConfig.getKeys(false);
            for(String defaultMessageName : defaultMessageNames){
                if(!languageConfig.isConfigurationSection(defaultMessageName)){
                    languageConfig.set(defaultMessageName, defaultLanguageConfig.getString(defaultMessageName));
                }
            }
            saveLanguageConfig(languageConfig, languageName);
            plugin.getServer().getConsoleSender().sendMessage("Successfully updated " + languageName + " language config.");
            try {
                languageClassStream.close();
                configReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMainLanguage(){
        String mainLanguage = "en_us";
        if(isDefaultLanguage(getServerLanguage())){
            mainLanguage = getServerLanguage();
        }
        return mainLanguage;
    }

    private void updateCustomLanguageConfig(String languageName){
        if(isAvailableLanguage(languageName)){
            FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(getLanguageFile(getMainLanguage()));
            FileConfiguration languageConfig = getLanguageConfig(languageName);
            Set<String> defaultMessageNames = defaultLanguageConfig.getKeys(false);
            for (String defaultMessageName : defaultMessageNames) {
                if(!languageConfig.isConfigurationSection(defaultMessageName)){
                    languageConfig.set(defaultMessageName, defaultLanguageConfig.getString(defaultMessageName));
                }
            }
            saveLanguageConfig(languageConfig, languageName);
            plugin.getServer().getConsoleSender().sendMessage("Successfully updated " + languageName + " language config.");
        }
    }

    public String getServerLanguage(){
        return plugin.getConfig().getString("SETTINGS.serverLanguage", "en_us");
    }

    private ArrayList<String> getDefaultLanguages(){
        ArrayList<String> defaultLanguages = new ArrayList<>();
        defaultLanguages.add("en_us");
        defaultLanguages.add("fr_fr");
        return defaultLanguages;
    }

    private boolean isDefaultLanguage(String languageName){
        ArrayList<String> defaultLanguages = getDefaultLanguages();
        return defaultLanguages.contains(languageName);
    }
}
