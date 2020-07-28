package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.*;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.ParseJson;

import java.awt.Color;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckCommand implements CommandInterfaceBot {
    private  final Logger logger = LoggerFactory.getLogger(DeckCommand.class);
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String userMention = "<@" + userId + ">";
        String userInput = event.getMessage().getContentRaw();
        String deckInput = userInput.replaceAll("(?i)"+ Pattern.quote("!thun deck "), "");

        try {
            //Extracts deckstring and deckname from input.
            String deckName = extractDeckName(deckInput);
            deckInput = explodeString(deckInput);
            String deckString = extractDeckString(deckInput);
            if(deckString.equals("")){
                event.getChannel().sendMessage(userMention + " Invalid deck string.").queue();
                return;
            }
            event.getChannel().sendMessage(userMention).queue();
            createAndSendEmbed(deckString,deckName,event);
        }
        catch (IOException | ParseException | BufferUnderflowException e) {
            event.getChannel().sendMessage(userMention + " Deck embed failed.").queue();
            logger.error(e.getMessage() + " " + e);
        }
    }

    void createAndSendEmbed(String deckString, String deckName, MessageReceivedEvent event) throws IOException, ParseException {
        ParseJson parseJson = new ParseJson(deckString);
        EmbedBuilder eB = new EmbedBuilder()
                .setAuthor(deckName+" ["+parseJson.hero+", "+parseJson.hsFormat+"]",null,parseJson.heroImg)
                .setTitle("Mana Curve:")
                .setDescription(parseJson.manaCurve)
                .setColor(new Color(0xF40C0C))
                .addField("Class Cards:", parseJson.classCardBuild.toString(),true)
                .addField("Neutral Cards:", parseJson.neutralCardBuild.toString(),true)
                .setFooter(Integer.toString(parseJson.dust), "https://cdn.discordapp.com/emojis/528697022048501760.png?v=1");
        event.getChannel().sendMessage(eB.build()).queue();
    }

    private String extractDeckString(String deckInput){
        String regexDeck = "(?i)(?m)(AAE(.{25,}))";
        Pattern pattern = Pattern.compile(regexDeck);
        Matcher matcher = pattern.matcher(deckInput);
        if(matcher.find()){
            return matcher.group(1);
        }
        else{
            return "";
        }
    }
    private String extractDeckName(String deckInput){
        String regexName = "(?i)(?:#{3})(.*?)(?:[#\\n]|(?:AAE(?:.*)))";
        Pattern pattern = Pattern.compile(regexName);
        Matcher matcher = pattern.matcher(deckInput);
        if(matcher.find()) {
            if (matcher.group(1).trim().length() <= 25) {
                return matcher.group(1).trim();
            }
            else return "Name Too Long";
        }
        else {
            return "";
        }
    }
    //Removes everything in front of the start of the deckstring if the deckstring isn't on a new line.
    private String explodeString(String deckInput){
        return deckInput.replaceAll("(?i)(#{3}|#)(.*)(?=AAE)","");
    }

    @Override
    public String getHelp() {
        return "Paste a hearthstone deck string to show the deck as a discord embed, which will include card name, cost, amount, class and format.\n" +
                "You can also include/create a name for your deck by having the line of the deck name start with `###`\n" +
                "Example usage: `" + getInvoke() + " ### DeckNameHere AAECAf0EEp4CuwLJA831Aur2Au72AsP4Asb4ApOAA6CAA6uMA+eVA8SZA5aaA5+bA6CbA/+dA+KfAwZNqwTLBJYFzYkDwJgDAA`";
    }

    @Override
    public String getInvoke() {
        return "!thun deck";
    }
}
