package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;
import objects.CommandManagerBot;

import java.awt.Color;
import java.util.List;

public class HelpCommand implements CommandInterfaceBot {
    private final CommandManagerBot manager;
    public HelpCommand(CommandManagerBot manager){
        this.manager = manager;
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String joined = String.join(" ",args).toLowerCase();
        if(joined.equals("!thun help")||joined.equals("!thun help dm")){
            createAndSendEmbed(event,joined);
            return;
        }
        commandHelp(event,joined);
    }

    private void commandHelp(MessageReceivedEvent event, String joined){
        CommandInterfaceBot command;
        command = manager.getCommand(joined.replace("!thun help ", "!thun "));
        if (command == null) {
            command = manager.getCommand(joined.replace("!thun help ", ""));
            if(command == null) {
                event.getChannel().sendMessage("Command: " + joined + " does not exist\n").queue();
                return;
            }
        }
        String message = "Command help for: `"+command.getInvoke() +"`\n"+command.getHelp();
        event.getChannel().sendMessage(message).queue();
    }

    private void createAndSendEmbed(MessageReceivedEvent event, String args){
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("C'thun Commands:")
                .setFooter("Commands are case insensitive.",null)
                .setColor(new Color(0xEAED1F))
                .setThumbnail("https://i.imgur.com/s5pGb2D.png");
        EmbedBuilder inviteEmbed = new EmbedBuilder()
                .setDescription("[Invite bot to your server](https://discordapp.com/api/oauth2/authorize?client_id=474523344864280578&permissions=18496&scope=bot) " +
                        "| [Join support channel](https://discord.gg/bBDwQMM)");
        if(args.equals("!thun help dm")){
            directMessageDescription(builder);
            directMessage(inviteEmbed,builder,event);
            return;
        }
        channelDescription(builder);
        event.getChannel().sendMessage(builder.build()).queue();
        event.getChannel().sendMessage(inviteEmbed.build()).queue();
    }

    private void channelDescription(EmbedBuilder builder){
        StringBuilder descriptionBuild = builder.getDescriptionBuilder();
        descriptionBuild.append("For command description, append the command behind the help command. Example: `!thun help card`\n" +
                "Type `!thun help dm` to get the commands with full description as direct message.\n\n");
        for (CommandInterfaceBot command : manager.getCommandValues()) {
            if (command.getInvoke().equals("<@474523344864280578>")) {
                continue;
            }
            descriptionBuild.append("`").append(command.getInvoke()).append("`\n");
        }
    }
    private void directMessageDescription(EmbedBuilder builder){
        StringBuilder descriptionBuild = builder.getDescriptionBuilder();
        for (CommandInterfaceBot command : manager.getCommandValues()) {
            if (command.getInvoke().equals("<@474523344864280578>")) {
                continue;
            }
            descriptionBuild.append("`").append(command.getInvoke()).append("`\n").append(command.getHelp()).append("\n\n");
        }
        if (descriptionBuild.length() > 2048){
            builder.setDescription(descriptionBuild.substring(0,2048));
        }
    }

    private void directMessage(EmbedBuilder inviteEmbed,EmbedBuilder eB, MessageReceivedEvent event){
        event.getAuthor().openPrivateChannel().queue((channel)-> channel.sendMessage(eB.build()).queue());
        event.getAuthor().openPrivateChannel().queue((channel)-> channel.sendMessage(inviteEmbed.build()).queue());
        event.getMessage().addReaction("\u2705").queue();
    }

    @Override
    public String getHelp() {
        return "If you append another command behind the help command, it'll give you more info about that command.\n" +
                "Example usage: `"+getInvoke()+" card`";
    }

    @Override
    public String getInvoke() {
        return "!thun help";
    }
}
