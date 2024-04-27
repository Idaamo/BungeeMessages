package fr.idaamo.bungeemessages.commands;

import fr.idaamo.bungeemessages.BungeeMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

    BungeeMessages main;

    public Reload() {
        //Every command informations in the super
        super("bm", "bm.reload", "bungeemessages");
        //Initialising instance
        this.main = BungeeMessages.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //If there is at least one argument
        if(args.length == 0){
            //Send help to the sender
            sendHelp(sender);
            return;
        }

        if(args[0].equalsIgnoreCase("reload")){
            //Reloading configuration file
            main.loadConfig();
            sender.sendMessage(new ComponentBuilder(main.configuration.getString("messages.adminSuccessReload").replace("&", "§")).create());
        }
    }

    private void sendHelp(CommandSender sender) {
        //Send help to the sender
        sender.sendMessage(new ComponentBuilder(main.configuration.getString("messages.adminHelp").replace("&", "§")).create());
    }
}
