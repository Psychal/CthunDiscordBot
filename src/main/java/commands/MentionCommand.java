package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import parsers.CardReader;
import objects.CommandInterfaceBot;

import java.awt.*;
import java.util.List;

public class MentionCommand implements CommandInterfaceBot {
    public static final long CTHUNID = 474523344864280578L;

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        int cardAmount = CardReader.getCardList().size();
        EmbedBuilder eB = createEmbed(cardAmount);
        event.getChannel().sendMessage(eB.build()).queue();
    }
    private EmbedBuilder createEmbed(int cardAmount){
        return new EmbedBuilder()
                .setTitle("Info")
                .setColor(new Color(0xEAED1F))
                .setDescription("You can find the command list by typing: `!thun help` or `!thun help dm`\n" +
                        "This bot uses Hearthstone card information available at Hearthpwn's website. Currently " +
                        cardAmount + " entries.\n" +
                        "Search through a database of cards(Patch 14.2) with a card name to find the specific one you want. \n" +
                        "If it doesn't find it, it will try to give you a list of cards with similar name.\n" +
                        "Use a deck string to receive a list over cards in the deck.\n" +
                        "This bot can also do math, support the most common operators, decimal and negative numbers."+
                        "All commands are case insensitive.")
                .addField("Bot Owner ", "Psychal#2359 <@178930497399816193>",true)
                .setThumbnail("https://i.imgur.com/s5pGb2D.png");
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "<@"+CTHUNID+">";
    }
}
