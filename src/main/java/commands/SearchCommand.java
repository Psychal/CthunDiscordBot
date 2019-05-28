package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import parsers.CardReader;
import objects.CommandInterfaceBot;
import parsers.DecodeVarInt;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SearchCommand implements CommandInterfaceBot {
    private static Map<String, SearchMapValues> searchMap = new HashMap<>();
    //[0]=Name, [1]=Lowercase-name,[2]=Desc, [3]=Info,[4]=Flavour, [5]=Img,
    private static List<String[]> cards = CardReader.getCardList();
    private List<Integer> cSearch;
    private int cardCount;
    private StringBuilder searchResult;

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String userMention = "<@" + userId + ">";
        String searchInput = String.join(" ",args).toLowerCase().replace("!thun search ","");
        performSearch(searchInput);
        //If search is not completely empty
        if(cardCount > 1) {
            fillSearchMap(userId);
            searchEmbed(event,userMention);
            embedTimer(userId);
        }
        //If search only yields one card.
        else if(cardCount == 1){
            int index = cSearch.get(0);
            event.getChannel().sendMessage(CardCommand.cardEmbed(index).build()).queue();
        }
        //Search is empty.
        else {
            event.getChannel().sendMessage(userMention + " Invalid search, try again.").queue();
        }
    }

    private void fillSearchMap(String userId){
        if (searchMap.containsKey(userId)){
            searchMap.get(userId).timer.cancel();
        }
        Timer timer = new Timer();
        SearchMapValues valuesMap = new SearchMapValues(cardCount,cSearch,timer);
        searchMap.put(userId, valuesMap);
    }

    private void performSearch(String searchInput){
        cSearch = new ArrayList<>();
        searchResult = new StringBuilder();
        cardCount=0;
        for(String[] str: cards){
            if(str[1].contains(searchInput)){
                cSearch.add(cards.indexOf(str));
                cardCount++;
                searchDescription(str);
            }
            if (cardCount == 10){
                break;
            }
        }
    }

    private void searchEmbed(MessageReceivedEvent event, String userMention){
        EmbedBuilder eB = new EmbedBuilder();
        eB.setTitle("Did you mean...")
                .setColor(new Color(0xF40C0))
                .setDescription(getSearchResult())
                .setFooter("Select a number.",null);
        event.getChannel().sendMessage(userMention).queue();
        event.getChannel().sendMessage(eB.build()).queue();
    }
    private void searchDescription(String[] str){
        searchResult.append(cardCount).append(". ").append(str[0]).append("\n");
    }
    private StringBuilder getSearchResult(){
        return this.searchResult;
    }
    //Timer to remove userid from Map
    private void embedTimer(String userId){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(userId);
                searchMap.remove(userId);
            }
        };
        searchMap.get(userId).timer.schedule(task,60000);
    }

    public static Map<String, SearchMapValues> getSearchMap(){
        return searchMap;
    }

    public static void getSearchInputResult(MessageReceivedEvent event){
        String userInput = event.getMessage().getContentRaw();
        String userId = event.getAuthor().getId();
        //Check if input is an int.
        if(!DecodeVarInt.stringIsInt(userInput)){
            return;
        }

        int input = Integer.parseInt(userInput);
        int searchCount = searchMap.get(userId).count;

        if (input <= searchCount && input > 0) {
            searchMap.get(userId).timer.cancel();
            int index = searchMap.get(userId).index.get(input-1);
            event.getChannel().sendMessage(CardCommand.cardEmbed(index).build()).queue();
            searchMap.remove(userId);
        }
    }

    //Card info for Searchmap.
    public static class SearchMapValues{
        public final int count;
        final List<Integer> index;
        final Timer timer;

        SearchMapValues(int numbers, List<Integer> indexArray, Timer timers){
            this.count = numbers;
            this.index = indexArray;
            this.timer = timers;
        }
    }

    @Override
    public String getHelp() {
        return "With this command you can search through the cards by their name. You will then receive a list of max 10 results, and by typing one of the numbers it'll post the card." +
                "If it can only find one card, it'll just post the card instead of a result list. There's also a 1 min timer on replying to the list. Making a new search will override the list." +
                "Example usage: `"+getInvoke()+" molten`";
    }

    @Override
    public String getInvoke() {
        return "!thun search";
    }
}
