package fr.idaamo.bungeemessages;

import fr.idaamo.bungeemessages.commands.Message;
import fr.idaamo.bungeemessages.commands.Reload;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class BungeeMessages extends Plugin {

    private static BungeeMessages main;

    public Configuration configuration;

    @Override
    public void onEnable() {
        //Register proxy commands
        this.getProxy().getPluginManager().registerCommand(this, new Message());
        this.getProxy().getPluginManager().registerCommand(this, new Reload());
        //Initialise the main variable, used to get the main instance
        main = this;
        //Creating the config file
        createFiles();
    }

    @Override
    public void onDisable() {
        //On disable, just letting know that the plugin is disabling
        getLogger().info("Plugin turning off.");
    }

    public void createFiles(){
        //if datafolder doesn't exist
        if(!getDataFolder().exists())
            //Creating it
            getDataFolder().mkdir();
        //getting the config.yml
        File file = new File(getDataFolder(), "config.yml");
        //if doesn't exist
        if(!file.exists()) {
            //Creating it
            file.mkdir();
            //Getting the config.yml from the resources
            InputStream in = getResourceAsStream("config.yml");
            //Copying it into the datafolder
            try {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                //Loading the configuration file from the bungeecord yaml provider
                this.configuration = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                this.getLogger().info("Error:" + e.getMessage());
            }
        }
    }

    public void loadConfig(){
        try{
            configuration = YamlConfiguration.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        }catch (IOException e){
            this.getLogger().info("Error:" + e.getMessage());
        }
    }

    public static BungeeMessages getInstance(){
        //Create static method to get the main instance
        //We could use a constructor but i chose to do so
        return main;
    }
}
