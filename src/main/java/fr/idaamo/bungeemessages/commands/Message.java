package fr.idaamo.bungeemessages.commands;

import fr.idaamo.bungeemessages.BungeeMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class Message extends Command {

    BungeeMessages main;

    public Message() {
        //Need to put the command name in the super
        super("msg", "", "m", "message", "whisper", "tell");
        //And initialising the main variable to use it later
        this.main = BungeeMessages.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Execute, method from bungeecord, we have to use it.
        //Everything will be executed after the sender sends the command
        if(args.length <= 1){
            //Checking if there is more than 1 argument ( /msg <player> <sender>
            sendHelp(sender);
            //If not, returning
            return;
        }
        String potentialTarget = args[0];
        //If potential target is online
        if(main.getProxy().getPlayer(potentialTarget) != null){
            //if so, getting the player
            ProxiedPlayer target = main.getProxy().getPlayer(potentialTarget);
            if(!sender.equals(target)) {
                String message = getBuiltMessage(args);
                //Sending each message
                sender.sendMessage(getSenderMessage(target));
                target.sendMessage(getTargetMessage(sender));
            }else{
                //Sending the message ( cannot send to myself ) from the config file
                sender.sendMessage(new ComponentBuilder(main.configuration.getString("messages.cannotSendSelf")
                        .replace("&", "§"))
                        .create());
            }
        }else{
            //Sending the message ( player not connected ) from the config file
            sender.sendMessage(new ComponentBuilder(main.configuration.getString("messages.PlayerNotConnected")).color(ChatColor.RED).create());
        }
    }

    private BaseComponent[] getSenderMessage(ProxiedPlayer target) {
        //New ComponentBuilder
        ComponentBuilder builder = new ComponentBuilder();
        //Getting the message in the config.yml ( customizable )
        //And replacing the target char ( from %t% to the target name, and the &s for the color )
        String senderMessage = main.configuration.getString("messages.sender").replace("%t%", target.getName()).replace("&", "§");
        //Adding the string in the component builder
        builder.append(senderMessage);
        //Returning the BaseComponents
        return builder.create();
    }

    private BaseComponent[] getTargetMessage(CommandSender sender){
        //Same as the getSenderMethod
        ComponentBuilder builder = new ComponentBuilder();
        String targetMessage = main.configuration.getString("messages.target").replace("%s%", sender.getName()).replace("&", "§");
        builder.append(targetMessage);
        return builder.create();
    }

    private String getBuiltMessage(String[] args) {
        //New string array, including only the arguments after the first one
        String[] parts = Arrays.copyOfRange(args, 1, args.length);
        //Creating a new string builder
        StringBuilder builder = new StringBuilder();
        //For all strings in parts
        for(String s : parts){
            //We put all strings in the builder with a space
            //To build the message with each args
            builder.append(s + " ");
        }
        //Returning the builder to string
        return builder.toString();
    }

    private void sendHelp(CommandSender sender) {
        //send help
        sender.sendMessage(new ComponentBuilder(main.configuration.getString("messages.usage").replace("&", "§")).create());
    }
}
