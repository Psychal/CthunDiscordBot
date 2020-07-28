package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;
import org.apache.commons.validator.routines.UrlValidator;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.DecodeVarInt;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImportCommand implements CommandInterfaceBot {
    private static Map<Long, DeckMapValues> deckMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ImportCommand.class);
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        final String url = "hearthpwn.com/decks/";
//        if (!event.getAuthor().getId().equals("178930497399816193")){
//            return;
//        }
        String userInput = event.getMessage().getContentRaw().replaceAll(getInvoke(),"").trim();
        if(userInput.equals("")){
            event.getChannel().sendMessage(event.getAuthor().getAsMention()).queue();
            event.getChannel().sendMessage("Foolish mortal, you forgot to provide name or a link. " +
                    "<:cthuntentacle:579480694229696513> <:cthuneye:520071874969862144> <:cthuntentacle:579480694229696513>").queue();
            return;
        }
        if (isUsername(userInput)) {
            try {
                multipleDecks(event,userInput);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        if (userInput.startsWith(url) || userInput.startsWith("www."+url)){
            userInput = "https://"+userInput;
        }
        else if (userInput.startsWith("http://"+url)){
            userInput = userInput.replaceFirst("http://","https://");
        }
        if (!isUrl(userInput)){
            return;
        }
        try {
            singleDeck(event,userInput);
        } catch (IOException | ParseException e) {
            logger.error(e.getMessage());
        }


    }
    private boolean isUsername(String input){
        return (input.split("/")[0].equals(input));
    }

    private boolean isUrl(String input){
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(input);
    }

    private static void singleDeck(MessageReceivedEvent event, String input) throws IOException, ParseException{
        Document doc = Jsoup.connect(input).get();
        Elements copyDeck = doc.getElementsByClass("copy-button button");
        String deckString = copyDeck.attr("data-clipboard-text");
        Elements metatags = doc.getElementsByTag("meta");
        String deckName = doc.getElementsByClass("deck-title tip").attr("title");
        for(Element meta : metatags){
            if (meta.attr("property").equals("og:title")){
                deckName = meta.attr("content");
            }
        }
        if (deckName.length() > 25){
            deckName = deckName.substring(0,25) + "...";
        }
        new DeckCommand().createAndSendEmbed(deckString,deckName,event);

    }

    private void multipleDecks(MessageReceivedEvent event, String input) throws IOException {
        String baseurl = "https://www.hearthpwn.com";
        String url = baseurl+"/members/"+input+"/decks";
        long userId = event.getAuthor().getIdLong();
        Document doc = Jsoup.connect(url).get();
        Elements pages = doc.getElementsByClass("b-pagination-item");
        Elements decks = doc.select("a[href]");
        List<String[]> deckList = new ArrayList<>();
        Map<Long,String[]> deckMap2 = new HashMap<>();
        int pageNumber = 1;
        int index = 0;

        for(Element page : pages) {
            if(page == pages.get(pages.size() - 2)) {
                //Last iteration
                if(DecodeVarInt.stringIsInt(page.text())){
                    pageNumber = Integer.parseInt(page.text());
                }
            }
        }
        for(Element deck : decks){
            if(deck.attr("href").startsWith("/decks/")){
                String deckname = deck.text();
                if (deckname.length() > 45){
                    deckname = deckname.substring(0,45);
                }
                index++;
                String [] deckLinks = {String.valueOf(index),deckname,baseurl+deck.attr("href")};
                deckList.add(deckLinks);
            }
            if(index == 10){
                break;
            }
        }
        if (index == 0){
            event.getChannel().sendMessage("No decks found.").queue();
            return;
        }
        fillDeckMap(userId,deckList);
        createAndSendEmbed(event,deckList);
        embedTimer(userId);

    }

    private void createAndSendEmbed(MessageReceivedEvent event, List<String[]> deckList){
        StringBuilder embedDesc = new StringBuilder();

        for(String[] deck : deckList){
            embedDesc.append(deck[0]).append(". ").append(deck[1]).append("\n");
        }
        EmbedBuilder eB = new EmbedBuilder()
                .setTitle("Decks")
                .setColor(new Color(0xF40C0))
                .setDescription(embedDesc)
                .setFooter("Select a number.",null);
        event.getChannel().sendMessage(event.getAuthor().getAsMention()).queue();
//        event.getChannel().sendMessage(eB.build()).queue((r1) -> {
//            r1.addReaction("\u2b05").queue();
//            r1.addReaction("\u27a1").queue();
//        });
        event.getChannel().sendMessage(eB.build()).queue();
    }
    public static Map<Long, DeckMapValues> getDeckMap(){
        return deckMap;
    }

    public static void getSearchInputResult(MessageReceivedEvent event) throws IOException, ParseException {
        String userInput = event.getMessage().getContentRaw();
        long userId = event.getAuthor().getIdLong();
        //Check if input is an int.
        if(!DecodeVarInt.stringIsInt(userInput)){
            return;
        }

        int input = Integer.parseInt(userInput);
        int listLength = deckMap.get(userId).deckArray.size();

        if (input <= listLength && input > 0) {
            deckMap.get(userId).timer.cancel();
            String link = deckMap.get(userId).deckArray.get(input-1)[2];
            singleDeck(event,link);
            deckMap.remove(userId);
        }
    }

    private void fillDeckMap(long userId, List<String[]> deckList){
        if (deckMap.containsKey(userId)){
            deckMap.get(userId).timer.cancel();
        }
        Timer timer = new Timer();
        DeckMapValues valuesMap = new DeckMapValues(deckList,timer);
        deckMap.put(userId, valuesMap);
    }

    //Timer to remove userid from Map
    private void embedTimer(long userId){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(userId);
                deckMap.remove(userId);
            }
        };
        deckMap.get(userId).timer.schedule(task,60000);
    }

    public static class DeckMapValues{
        final List<String[]> deckArray;
        final Timer timer;

        DeckMapValues(List<String[]> deckArray, Timer timers){
            this.deckArray = deckArray;
            this.timer = timers;
        }
    }

    @Override
    public String getHelp() {
        return "Experimental command. Imports decks from Hearthpwn and embeds it like the deck command.\n" +
                "Enter a hearthpwn username to get a list of 10 decks, or enter a direct link to deck.\n" +
                "Example usage: `"+getInvoke()+" zul0man`";
    }

    @Override
    public String getInvoke() {
        return "!thun import";
    }
}
