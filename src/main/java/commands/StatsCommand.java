package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.awt.*;
import java.util.List;

public class StatsCommand implements CommandInterfaceBot {
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        int guilds = event.getJDA().getGuilds().size();
        int users = event.getJDA().getUsers().size();
        int channels = event.getJDA().getTextChannels().size();
        long ping = event.getJDA().getPing();

        EmbedBuilder eB = createEmbed(channels,guilds,users,ping);
        event.getChannel().sendMessage(eB.build()).queue();
    }
    private EmbedBuilder createEmbed(int channels, int guilds, int users, long ping){
        return new EmbedBuilder()
                .setTitle("C'thun Sees")
                .setColor(new Color(0xEAED1F))
                .addField("Servers",Integer.toString(guilds),true)
                .addField("Channels",Integer.toString(channels),true)
                .addField("Users",Integer.toString(users),true)
                .addField("Ping",ping + " ms",true)
                .addField("JDK Version","11.0.1",true)
                .addField("JDA Version","3.8.3_463",true)
                .addField("Developed By","Psychal#2359 <@178930497399816193>",false);
    }

    @Override
    public String getHelp() {
        return "Provides an embed with various statistics which includes all the users,servers and channels that the bot can see, as well as what kind of version the bot is running.\n" +
                "Example usage: `"+getInvoke()+"`";
    }

    @Override
    public String getInvoke() {
        return "!thun stats";
    }
}
