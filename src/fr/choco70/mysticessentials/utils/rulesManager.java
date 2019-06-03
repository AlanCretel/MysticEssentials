package fr.choco70.mysticessentials.utils;

import fr.choco70.mysticessentials.MysticEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class rulesManager{

    private MysticEssentials plugin = MysticEssentials.getPlugin(MysticEssentials.class);
    private File dataFolder = plugin.getDataFolder();

    public void createRulesFile(){
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }

        File file = new File(dataFolder,"rules.yml");

        if(!file.exists()){
            try {
                file.createNewFile();
                setRule(0, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getRulesFile(){
        return new File(plugin.getDataFolder(), "rules.yml");
    }

    public FileConfiguration getRulesConfig(){
        return YamlConfiguration.loadConfiguration(getRulesFile());
    }

    public void saveRulesConfig(FileConfiguration rulesConfig){
        try {
            rulesConfig.save(getRulesFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRulesList(){
        ArrayList<String> rulesArray = new ArrayList<>();
        if(getRulesConfig().isConfigurationSection("RULES")){
            Set<String> rules = getRulesConfig().getConfigurationSection("RULES").getKeys(false);
            for (String rule : rules) {
                rulesArray.add(getRulesConfig().getString("RULES." + rule));
            }
        }
        else{
            setRule(0, "");
        }
        return rulesArray;
    }

    public int getRulesNumber(){
        return getRulesList().size();
    }

    public String getRule(int index){
        ArrayList<String> rules = getRulesList();
        return rules.get(index);
    }

    public void addRule(String rule){
        FileConfiguration rulesConfig = getRulesConfig();
        rulesConfig.set("RULES." + getRulesNumber(), rule);
        saveRulesConfig(rulesConfig);
    }

    public void removeRule(int index){
        FileConfiguration rulesConfig = getRulesConfig();
        rulesConfig.set("RULES." + index, null);
        saveRulesConfig(rulesConfig);
        renumberRules();
    }

    public void setRule(int index, String rule){
        FileConfiguration rulesConfig = getRulesConfig();
        rulesConfig.set("RULES." + index, rule);
        saveRulesConfig(rulesConfig);
    }

    public void renumberRules(){
        FileConfiguration rulesConfig = getRulesConfig();
        ArrayList<String> rules = getRulesList();
        Integer ruleNumber = rules.size();
        HashMap<String, String> rulesList = new HashMap<>();
        for (int i = 0; i < ruleNumber; i++) {
            rulesList.put(i + "", getRule(i));
        }
        rulesConfig.set("RULES", null);
        if(ruleNumber == 0){
            rulesConfig.set("RULES." + 0, "");
        }
        else{
            for (int i = 0; i < ruleNumber; i++) {
                rulesConfig.set("RULES." + i, getRule(i));
            }
        }
        saveRulesConfig(rulesConfig);
    }
}
