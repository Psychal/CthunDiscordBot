package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;
import parsers.CardReader;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class CardCommand implements CommandInterfaceBot {
    private final Random random;

    public CardCommand(Random random){
        this.random = random;
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String input = String.join(" ", args).toLowerCase();
        String userId = event.getAuthor().getId();
        String userMention = "<@" + userId + ">";
        if(input.equals("!thun card")){
            //cards.size is the maximum and the 0 is our minimum(0-card.size), alternatively use rand.nextInt((max - min) + 1) + min
            int n = random.nextInt(CardReader.size);

            EmbedBuilder eB = cardEmbed(n);
            event.getChannel().sendMessage(eB.build()).queue();
        }
        else{
            input = input.replace("!thun card ","");
            SearchCommand search = new SearchCommand();
            search.performSearch(input);
            search.checkCardCount(userId,userMention,event);
        }
    }
    static EmbedBuilder cardEmbed(int number){
        //[0]=Name, [1]=Lowercase-name,[2]=Desc, [3]=Info,[4]=Flavour, [5]=Img
        List<String[]> cards = CardReader.getCardList();
        String img = cards.get(number)[5].equals("") ? null : cards.get(number)[5];
        return new EmbedBuilder()
                .setTitle(cards.get(number)[0])
                .setColor(new Color(0xF40C0C))
                .setDescription(cards.get(number)[2])
                .addField("Details:", cards.get(number)[3],true)
                .addBlankField(true)
                .setFooter(cards.get(number)[4], "https://i.imgur.com/s5pGb2D.png")
                .setImage(img);
    }

    @Override
    public String getHelp() {
        return "Posts a random card or hero as a discord embed.\n" +
                "The command also behaves like a search if you append a card name at the end.\n" +
                "Example usage: `"+getInvoke()+"`\n" +
                "`"+getInvoke()+" Misdirection`";
    }

    @Override
    public String getInvoke() {
        return "!thun card";
    }
}
