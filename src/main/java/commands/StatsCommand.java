package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;
import objects.Uptime;
import parsers.CardReader;

import java.awt.Color;
import java.util.List;

public class StatsCommand implements CommandInterfaceBot {
    static final String patch = "17.6.0.53261";
    static final String owner = "Psychal#2359 <@178930497399816193>";

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        int guilds = event.getJDA().getGuilds().size();
        int users = event.getJDA().getUsers().size();
        int cardAmount = CardReader.size;
        String uptime = Uptime.getUptime(System.currentTimeMillis());
        int channels = event.getJDA().getTextChannels().size();
        long ping = event.getJDA().getGatewayPing();

        EmbedBuilder eB = createEmbed(channels,guilds,users,ping, cardAmount, uptime);
        event.getChannel().sendMessage(eB.build()).queue();
    }
    private EmbedBuilder createEmbed(int channels, int guilds, int users, long ping, int cardAmount, String uptime){
        return new EmbedBuilder()
                .setTitle("C'thun Sees")
                .setColor(new Color(0xEAED1F))
                .addField("Servers",Integer.toString(guilds),true)
                .addField("Channels",Integer.toString(channels),true)
                .addField("Users",Integer.toString(users),true)
                .addField("Ping(Websocket)",ping +  " ms",true)
                .addField("JDK Version","OpenJDK 13",true)
                .addField("JDA Version","4.2.0_180",true)
                .addField("Card Amount", Integer.toString(cardAmount), true)
                .addField("HS Patch",patch,true)
                .addField("Uptime", uptime,false)
                .addField("Developed By",owner,false);
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