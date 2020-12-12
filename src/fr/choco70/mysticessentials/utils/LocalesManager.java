package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public class LocalesManager {

    private final File dataFolder;
    private final File localesFolder;
    private final MysticEssentials plugin;

    public LocalesManager(MysticEssentials plugin){
        this.dataFolder = plugin.getDataFolder();
        this.localesFolder = new File(dataFolder + File.separator + "langs" + File.separator);
        this.plugin = plugin;
    }

    public void createLanguageFile(String language){
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }
        if(!localesFolder.exists()){
            localesFolder.mkdir();
        }

        File file = new File(localesFolder,language + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getLocaleFile(String language){
        return new File(localesFolder,language + ".yml");
    }

    public FileConfiguration getLocaleConfig(String language){
        return YamlConfiguration.loadConfiguration(new File(localesFolder + File.separator + language + ".yml"));
    }

    public void saveLocaleConfig(FileConfiguration languageConfig, String language){
        try {
            languageConfig.save(getLocaleFile(language));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String language, String message){
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(getLocaleFile(language));
        String defaultMessage = getDefaultMessage(language, message);
        String translation = langConfig.getString(message, defaultMessage);

        langConfig.set(message, translation);
        saveLocaleConfig(langConfig, language);

        return translation;
    }

    private String getDefaultMessage(String languageName, String message){
        if(isDefaultLocale(languageName)){
            InputStream languageClassStream = plugin.getClass().getResourceAsStream("/langs/" + languageName + ".yml");
            InputStreamReader streamReader = new InputStreamReader(languageClassStream);
            try {
                FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(streamReader);
                streamReader.close();
                languageClassStream.close();
                return defaultLanguageConfig.getString(message);
            } catch (IOException e) {
                e.printStackTrace();
                FileConfiguration defaultLanguageConfig = getLocaleConfig(languageName);
                return defaultLanguageConfig.getString(message);
            }
        }
        else{
            FileConfiguration defaultLanguageConfig = getLocaleConfig(languageName);
            return defaultLanguageConfig.getString(message);
        }
    }

    public ArrayList<String> getAvailableLocales(){
        ArrayList<String> availableLanguages = new ArrayList<>();
        File[] languageFiles = localesFolder.listFiles();
        if (languageFiles != null) {
            for (File languageFile : languageFiles) {
                if(languageFile.getName().endsWith(".yml")){
                    availableLanguages.add(languageFile.getName().replace(".yml", "").replaceAll(" ", "_"));
                }
            }
        }
        return availableLanguages;
    }

    public boolean isAvailableLocale(String languageName){
        return getAvailableLocales().contains(languageName);
    }

    public void setupDefaultLocales(){
        ArrayList<String> defaultLanguages = getDefaultLocales();
        for (String defaultLanguage : defaultLanguages){
            File languageFile = new File(localesFolder, defaultLanguage + ".yml");
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

    public void updateLocalesConfigs(){
        ArrayList<String> availableLanguages = getAvailableLocales();
        updateDefaultLocalesConfigs();
        for (String availableLanguage : availableLanguages) {
            boolean isDefaultLanguage = isDefaultLocale(availableLanguage);
            if(!isDefaultLanguage){
                plugin.getServer().getConsoleSender().sendMessage("Checking: " + availableLanguage + " language config.");
                updateCustomLocaleConfig(availableLanguage);
            }
        }
    }

    private void updateDefaultLocalesConfigs(){
        ArrayList<String> defaultLanguages = new ArrayList<>();
        for (String availableLanguage : getAvailableLocales()) {
            if(isDefaultLocale(availableLanguage)){
                defaultLanguages.add(availableLanguage);
            }
        }
        for (String defaultLanguage : defaultLanguages) {
            plugin.getServer().getConsoleSender().sendMessage("Checking: " + defaultLanguage + " language config.");
            updateDefaultLocaleConfig(defaultLanguage);
        }
    }

    private void updateDefaultLocaleConfig(String languageName){
        File languageFile = new File(localesFolder, languageName + ".yml");
        if(isDefaultLocale(languageName)){
            InputStream languageClassStream = plugin.getClass().getResourceAsStream("/langs/" + languageName + ".yml");
            if(languageClassStream == null){
                return;
            }
            Reader configReader = new InputStreamReader(languageClassStream, StandardCharsets.UTF_8);
            try{
                FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(configReader);
                if(!languageFile.exists()){
                    createLanguageFile(languageName);
                }
                updateLocaleConfig(languageName, defaultLanguageConfig);
                try {
                    languageClassStream.close();
                    configReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch(Exception e){
                System.out.println("An error occured when updating the locale " + languageName + ". To fix this issue please restart the server.");
            }
            try {
                configReader.close();
                languageClassStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getMainLocale(){
        String mainLanguage = "en_us";
        if(isDefaultLocale(getServerLocale())){
            mainLanguage = getServerLocale();
        }
        return mainLanguage;
    }

    private void updateCustomLocaleConfig(String languageName){
        if(isAvailableLocale(languageName)){
            FileConfiguration defaultLanguageConfig = YamlConfiguration.loadConfiguration(getLocaleFile(getMainLocale()));
            updateLocaleConfig(languageName, defaultLanguageConfig);
        }
    }

    private void updateLocaleConfig(String languageName, FileConfiguration defaultLanguageConfig) {
        FileConfiguration languageConfig = getLocaleConfig(languageName);
        Set<String> defaultMessageNames = defaultLanguageConfig.getKeys(false);
        for (String defaultMessageName : defaultMessageNames) {
            if(!languageConfig.isConfigurationSection(defaultMessageName)){
                languageConfig.set(defaultMessageName, defaultLanguageConfig.getString(defaultMessageName));
            }
        }
        saveLocaleConfig(languageConfig, languageName);
        plugin.getServer().getConsoleSender().sendMessage("Successfully updated " + languageName + " language config.");
    }

    public String getServerLocale(){
        return plugin.getConfig().getString("SETTINGS.serverLanguage", "en_us");
    }

    private ArrayList<String> getDefaultLocales(){
        ArrayList<String> defaultLanguages = new ArrayList<>();
        defaultLanguages.add("en_us");
        defaultLanguages.add("fr_fr");
        return defaultLanguages;
    }

    private boolean isDefaultLocale(String languageName){
        ArrayList<String> defaultLanguages = getDefaultLocales();
        return defaultLanguages.contains(languageName);
    }
}