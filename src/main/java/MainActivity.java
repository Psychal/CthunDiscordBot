import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.Color;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.LoginException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.discordbots.api.client.DiscordBotListAPI;

public class MainActivity extends ListenerAdapter {
    private static List<String[]> cards;
    private static Map<String, SearchMapValues> searchMap;
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static int cardAmount;
    private static int serverCount;
    private static DiscordBotListAPI api;

    public static void main(String[] args) throws LoginException, IOException {
        String botToken = System.getenv("BotToken");

        try{
            JDA builder = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .addEventListener(new MainActivity())
                    .setGame(Game.playing("!thun help"))
                    .build();
            builder.awaitReady();
            serverCount = builder.getGuilds().size();
        }
        catch(InterruptedException e){
            logger.error(e.getMessage());
        }
        cards = CardReader.cardReader();
        cardAmount = cards.size();
        searchMap = new HashMap<>();
        dblApi();
    }

    private static void dblApi(){
        String apiToken = System.getenv("APIToken");
        String clientId = "474523344864280578";

        api = new DiscordBotListAPI.Builder()
                .token(apiToken)
                .botId(clientId)
                .build();
        try{
            api.setStats(serverCount).toCompletableFuture().get();
            logger.info("Server count: " + serverCount);
        }
        catch (InterruptedException | ExecutionException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String userInput1 = event.getMessage().getContentRaw();
        String userInput2 = event.getMessage().getContentRaw().toLowerCase().replace("!thun math", "");
        String userId = event.getAuthor().getId();
        String userMention = "<@" + userId + ">";

        //Ignores messages from bots.
        if (event.getAuthor().isBot()) {
            return;
        }

        //Solves simple math with whole numbers.
        if (userInput1.toLowerCase().startsWith("!thun math")) {


            if(userInput1.toLowerCase().contains("/0")) {
                event.getChannel().sendMessage(userMention + " Cannot divide by zero").queue();
            }
            else {
                String numSum = Integer.toString(EvaluateMath.evaluateMath(userInput2));
                event.getChannel().sendMessage(userMention + " The result is: " + numSum).queue();
                }
        }

        //C'thun's quotes.
        //TODO Make local .txt file instead.
        if (userInput1.equalsIgnoreCase("!thun")) {
            String[] quotes = {
                    "Death is close...\n",
                    "You are already dead.\n",
                    "Your courage will fail.\n",
                    "Your friends will abandon you.\n",
                    "You will betray your friends.\n",
                    "You will die.\n",
                    "You are weak.\n",
                    "My dreaming ends... Your nightmare... begins...",
                    "Your minions will abandon you.\n",
                    "Your deck betrays you.\n",
                    "You have already lost.\n",
                    "Caress your fear.\n",
                    "Your minions think you are weak.\n",
                    "Hope is an illusion.\n",
                    "It was your fault.\n",
                    "That was a mistake.\n",
                    "Flee, screaming.\n",
                    "Give in, to your fear.\n",
                    "Well met.",
                    "Your heart will explode.",
                    "http://i.imgur.com/T7opo9v.jpg",
                    "Omae wa mou shindeiru.",
                    "https://i.imgur.com/HkLjkrT.png",
                    "It's pronounced \"Kuh-THOON\"!",
                    "You've failed this city.",
                    "Oolong tea: 9 parts vodka, 1 part whiskey.",
                    "Thrall's Balls! They're everywhere...",
                    "Jaraxxus told me you're a trifling gnome, and your arrogance will be your undoing!",
                    "Tenno skoom!",
                    "The era of flesh has ended.",
                    "Your software will fail. Your users will abandon you. You are already obsolete.",
                    "<a:cthunanim:520295664278700059>",
                    "Bleed for C'thun.",
                    "https://i.imgur.com/W2TIogP.gif"
            };

            Random rand = new Random();
            //32 is the maximum and the 0 is our minimum(0-32), can also use rand.nextInt((max - min) + 1) + min
            int n = rand.nextInt(quotes.length);
            event.getChannel().sendMessage(quotes[n]).queue();
        }

        //Uses custom emote from discord room.
        if (userInput1.toLowerCase().contains("eye for an eye")) {
            Emote emote = event.getJDA().getEmoteById(361675944190279690L);
            event.getMessage().addReaction(emote).queue();
        }

        //Local inner class for emotes.
        class CthunEmotes{
            private String unicode;
            private Emote emote;
            private CthunEmotes (Emote emote) {
                this.emote = emote;
            }
            private CthunEmotes (String unicode){
                this.unicode = unicode;
            }
        }
        //Reacts with one emote from a list of unicode and custom emotes.
        if (userInput1.toLowerCase().contains("c'thun")) {
            Random rand = new Random();
            List <CthunEmotes> emotes = new ArrayList<>();
            emotes.add(new CthunEmotes(event.getJDA().getEmoteById(520295664278700059L)));
            emotes.add(new CthunEmotes(event.getJDA().getEmoteById(520288408690491412L)));
            emotes.add(new CthunEmotes(event.getJDA().getEmoteById(520071874969862144L)));

            emotes.add(new CthunEmotes("\uD83D\uDC40"));
            emotes.add(new CthunEmotes("\uD83D\uDC41"));
            emotes.add(new CthunEmotes("\u2620"));
            emotes.add(new CthunEmotes("\uD83D\uDC80"));
            emotes.add(new CthunEmotes("\uD83D\uDDE1"));

            int n = rand.nextInt(emotes.size());
            CthunEmotes emoji = emotes.get(n);
            if (emoji.emote != null){
            event.getMessage().addReaction(emoji.emote).queue();
            }
            else {
                event.getMessage().addReaction(emoji.unicode).queue();
            }
        }

        //C'thun pinged.
        if(userInput1.equals("<@474523344864280578>")) {
            EmbedBuilder eB = new EmbedBuilder()
                    .setTitle("Info")
                    .setColor(new Color(0xEAED1F))
                    .setDescription("You can find the command list by typing: `!thun help`\n" +
                            "This bot uses Hearthstone card information available at Hearthpwn's website. Currently " +
                            cardAmount + " entries.\n" +
                            "Search through a database of cards(Patch 13.1) with a card name to find the specific one you want. \n" +
                            "If it doesn't find it, it will try to give you a list of cards with similar name.\n" +
                            "Use a deckstring to recieve a list over cards in the deck.\n" +
                            "This bot can also do simple math(addition, subtraction, division, multiplication) with whole numbers."+
                            "All commands are case insensitive.")
                    .addField("Bot Owner ", "Psychal#2359 <@178930497399816193>",true)
                    .setThumbnail("https://i.imgur.com/s5pGb2D.png");
            event.getChannel().sendMessage(eB.build()).queue();
        }

        //C'thun stats.
        if(userInput1.equalsIgnoreCase("!thun stats")){
            int guilds = event.getJDA().getGuilds().size();
            int users = event.getJDA().getUsers().size();
            int channels = event.getJDA().getTextChannels().size();
            long ping = event.getJDA().getPing();

            EmbedBuilder eB = new EmbedBuilder()
                    .setTitle("C'thun Sees")
                    .setColor(new Color(0xEAED1F))
                    .addField("Servers",Integer.toString(guilds),true)
                    .addField("Channels",Integer.toString(channels),true)
                    .addField("Users",Integer.toString(users),true)
                    .addField("Ping",Long.toString(ping)+" ms",true)
                    .addField("JDK Version","10.0.2",true)
                    .addField("JDA Version","3.8.0_436",true)
                    .addField("Developed By","Psychal#2359 <@178930497399816193>",false);
            event.getChannel().sendMessage(eB.build()).queue();
        }

        if (userInput1.equalsIgnoreCase("!thun token")) {
            event.getChannel().sendMessage(userMention + " Token cards are brought onto the board through card effects.").queue();
        }
        if (userInput1.equalsIgnoreCase("!thun elite")) {
            event.getChannel().sendMessage(userMention + " Elite cards can be identified by the dragon frame and are more powerful.").queue();
        }
        if (userInput1.equalsIgnoreCase("!thun collectible")) {
            event.getChannel().sendMessage(userMention + " Collectible cards are all cards that can be added to a player's collection.").queue();
        }

        //Searches through the ArrayList to find a matching card.
        if (userInput1.toLowerCase().startsWith("!thun search ")) {
            String searchInput = userInput1.toLowerCase().replace("!thun search ", "");
            String userIdSearch = event.getAuthor().getId();

            int index = -1;
            for (String [] str : cards){
                if(str[1].equals(searchInput)){
                    index = cards.indexOf(str);
                }
            }
            if(index == -1) {
                int cardCount=0;
                List<Integer> cSearch = new ArrayList<>();
                EmbedBuilder sB = new EmbedBuilder();
                StringBuilder sResult = new StringBuilder();
                for(String[] str: cards){
                    if(str[1].contains(searchInput)){
                        cSearch.add(cards.indexOf(str));
                        cardCount++;
                        sResult.append(cardCount).append(". ").append(str[0]).append("\n");
                    }
                    if (cardCount == 10){
                        break;
                    }
                }
                //If search is not completely empty
                if(!sResult.toString().equals("")) {
                    if (searchMap.containsKey(userId)){
                        searchMap.get(userId).timer.cancel();
                    }
                    Timer timer = new Timer();
                    SearchMapValues valuesMap = new SearchMapValues(cardCount,cSearch,timer);
                    searchMap.put(userIdSearch, valuesMap);
                    sB.setTitle("Did you mean...")
                        .setColor(new Color(0xF40C0))
                        .setDescription(sResult)
                        .setFooter("Select a number.",null);
                    event.getChannel().sendMessage(userMention).queue();
                    event.getChannel().sendMessage(sB.build()).queue();

                    //Timer to remove userid from Map
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println(userId);
                            searchMap.remove(userId);
                        }
                    };
                    searchMap.get(userId).timer.schedule(task,60000);
                }
                //Search is empty.
                if(sResult.toString().equals(""))
                {
                    event.getChannel().sendMessage(userMention + " Invalid search, try again.").queue();
                }
            }

            //If it finds an exact match.
            else {
                String img = null;
                if(!cards.get(index)[5].equals("")){
                    img = cards.get(index)[5];
                }
                EmbedBuilder eB = new EmbedBuilder()
                        .setTitle(cards.get(index)[0])
                        .setColor(new Color(0xF40C0C))
                        .setDescription(cards.get(index)[2])
                        .addField("Details:", cards.get(index)[3],true)
                        .addBlankField(true)
                        .setFooter(cards.get(index)[4], "https://i.imgur.com/s5pGb2D.png")
                        .setImage(img);
                event.getChannel().sendMessage(userMention).queue();
                event.getChannel().sendMessage(eB.build()).queue();
            }
        }

        if (searchMap != null && searchMap.containsKey(userId)) {
            //Check if input is an int.
            if (userInput1 == null) {
                return;
            }
            if (userInput1.isEmpty()) {
                return;
            }
            int length = userInput1.length();
            int i = 0;
            if (userInput1.charAt(0) == '-') {
                if (length == 1) {
                    return;
                }
                i = 1;
            }
            for (; i < length; i++) {
                char c = userInput1.charAt(i);
                if (c < '0' || c > '9') {
                    return;
                }
            }

            int input = Integer.parseInt(userInput1);
            int searchCount = searchMap.get(userId).count;

            if (input <= searchCount && input > 0) {
                searchMap.get(userId).timer.cancel();
                int index = searchMap.get(userId).index.get(input-1);
                String img = null;
                if(!cards.get(index)[5].equals("")){
                    img = cards.get(index)[5];
                }
                EmbedBuilder eB = new EmbedBuilder()
                        .setTitle(cards.get(index)[0])
                        .setColor(new Color(0xF40C0C))
                        .setDescription(cards.get(index)[2])
                        .addField("Details:", cards.get(index)[3],true)
                        .addBlankField(true)
                        .setFooter(cards.get(index)[4], "https://i.imgur.com/s5pGb2D.png")
                        .setImage(img);
                event.getChannel().sendMessage(eB.build()).queue();
                searchMap.remove(userId);
                }
        }

        //Card embed.
        if (userInput1.equalsIgnoreCase("!thun card")) {
            Random rand = new Random();
            //28 is the maximum and the 0 is our minimum(0-28), alternatively use rand.nextInt((max - min) + 1) + min
            int n = rand.nextInt(cards.size());
            String img = null;
            if(!cards.get(n)[5].equals("")){
                img = cards.get(n)[5];
            }
            EmbedBuilder eB = new EmbedBuilder()
            .setTitle(cards.get(n)[0])
            .setColor(new Color(0xF40C0C))
            .setDescription(cards.get(n)[2])
            .addField("Details:", cards.get(n)[3],true)
            .addBlankField(true)
            .setFooter(cards.get(n)[4], "https://i.imgur.com/s5pGb2D.png")
            .setImage(img);
            event.getChannel().sendMessage(userMention).queue();
            event.getChannel().sendMessage(eB.build()).queue();
        }

        //Help embed.
        if(userInput1.equalsIgnoreCase("!thun help")) {
            EmbedBuilder eB = new EmbedBuilder()
                    .setTitle("C'thun Commands")
                    .setColor(new Color(0xEAED1F))
                    .setDescription(
                            "`!thun` \nGet C'thun to say something!\n\n" +
                            "`c'thun`\nInvoke C'thun's reaction.\n\n" +
                            "`!thun deck [deckstring]` \nDecodes a deckstring to recieve an embed of all the cards in the deck.\n\n" +
                            "`!thun card` \n Posts a random hearthstone card in the form of a discord embed.\n\n" +
                            "`!thun search [word]` `!thun search mecha'thun`\nEnter a word or full name to perform a search. Yields a maximum of 10 results. Result input lasts only for 60 seconds. \n\n" +
                            "`!thun math [expression]` `!thun math 1+1`\n Enter an expression to make C'thun solve it. Whole numbers only. He can currently only solve addition, subtraction, division and multiplication.\n\n" +
                            "`!thun token` `!thun collectible` `!thun elite`\nThese are card keyword commands to bring up a description of what each keyword means.\n\n" +
                            "`!thun stats`\n Stats and other info about the bot.\n\n" +
                            "`!thun changelog`\n Changelog over the most recent couple of changes.")
                    .setFooter("Commands are case insensitive and without square-brackets.",null)
                    .setThumbnail("https://i.imgur.com/s5pGb2D.png");
            EmbedBuilder eB2 = new EmbedBuilder()
                    .setDescription("[Invite bot to your server](https://discordapp.com/api/oauth2/authorize?client_id=474523344864280578&permissions=18496&scope=bot) " +
                            "[Join support channel](https://discord.gg/bBDwQMM)");
            event.getChannel().sendMessage(eB.build()).queue();
            event.getChannel().sendMessage(eB2.build()).queue();
        }

        //Changelog embed.
        if(userInput1.equalsIgnoreCase("!thun changelog")) {

            event.getChannel().sendMessage("```C'thun Changelog \n\n" +
                    "# Changelog\n" +
                    "Only the few most recent changelog entries will be listed here\n" +
                    "\n" +
                    "## [Unreleased]\n" +
                    "- Add feature to import public decklist from hearthpwn.\n" +
                    "- Add images for cards that do not currently have any, but which exist.\n" +
                    "- Add artist name to all cards with images.\n" +
                    "- Add unplayable removed cards.\n" +
                    "- Implement a system for duplicate entries with different stats.\n" +
                    "\n" +
                    "## - 2018-12-31\n" +
                    "### Added\n" +
                    "- Decode deckstrings feature.\n" +
                    "\n" +
                    "## - 2018-12-21\n" +
                    "### Changed\n" +
                    "- Updated existing entries to Patch 13.1 (2018-12-19).\n" +
                    "\n" +
                    "## - 2018-12-06\n" +
                    "### Added\n" +
                    "- Card entries from Rastakhan's Rumble.\n" +
                    "\n" +
                    "### Changed\n" +
                    "- Update existing entries to Patch 12.2.0.27358(2018-10-18).\n" +
                    "- Update help section.```").queue();
        }
        //Decode deckstrings.
        if(userInput1.toLowerCase().startsWith("!thun deck ")){
            int dust=0;
            int mana0=0;
            int mana1=0;
            int mana2=0;
            int mana3=0;
            int mana4=0;
            int mana5=0;
            int mana6=0;
            int mana7=0;
            byte [] deck;
            JSONParser parser = new JSONParser();

            String heroClass ="Hero Class";
            String hsFormat="Format";
            String classImg="https://cdn.discordapp.com/emojis/528697094840647692.png?v=1";
            StringBuilder classCardBuild = new StringBuilder();
            StringBuilder neutralCardbuild = new StringBuilder();
            String deckInput = userInput1.replaceAll("(?i)"+ Pattern.quote("!thun deck "), "");
            String deckInput2;
            String deckInput3 = "";
            String lengthWarn = "";
            String regexDeck = "(?i)(?m)([a-zA-Z0-9/=+]{45,})";
            String regexName = "(?i)(?<=(###))(.*)";
            Pattern pattern = Pattern.compile(regexDeck);
            Matcher matcher;
            matcher = pattern.matcher(deckInput);

            //Extracts deckstring and deckname from input.
            if(matcher.find()){
                deckInput2 = matcher.group(1);
                deckInput = deckInput.replaceAll(regexDeck,"");
                Pattern pattern2 = Pattern.compile(regexName);
                matcher = pattern2.matcher(deckInput);

                if(matcher.find()){
                    if(matcher.group(2).trim().length() <= 25 ){
                    deckInput3 = matcher.group(2).trim();
                    }
                    else{
                        lengthWarn = " Deck name too long.";
                    }
                }
            }
            else{
                event.getChannel().sendMessage(userMention + " Invalid deck string.").queue();
                return;
            }

            try{
                deck = Base64.getDecoder().decode(deckInput2);
            }
            catch (IllegalArgumentException e){
                event.getChannel().sendMessage(userMention+ " Invalid deck string.").queue();
                logger.error(e.getMessage());
                return;
            }

            //VarInts placed in Buffer for decoding and usage.
            //Header block: Reserved byte, Version, Format.
            ByteBuffer buf = ByteBuffer.wrap(deck);
            buf.get();
            int version = buf.get();
            if (version != 1) {
                try {
                    throw new ParseException(version);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            int format = DecodeVarInt.decodeVarInt(buf);
            switch (format){
                case 1:
                    hsFormat = "Wild";
                    break;
                case 2:
                    hsFormat = "Standard";
                    break;
                default :
                    logger.error("HS Format error",new ParseException(format));
            }

            //Card block: Heroes(amount, dbfid(s)), Single-cards(amount, dbfid(s)), 2-cards(amount, dbfid(s)), n-cards(dbfid, amount).
            List<Integer> hero = new ArrayList<>();
            int heroCount = DecodeVarInt.decodeVarInt(buf);
            for(int i = 0; i<heroCount; i++){
                hero.add(DecodeVarInt.decodeVarInt(buf));
            }

            List<CardPair> card = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                int c = DecodeVarInt.decodeVarInt(buf);
                for (int j = 0; j < c; j++) {
                    int dbfId = DecodeVarInt.decodeVarInt(buf);
                    int count;
                    if (i == 3) {
                        count = DecodeVarInt.decodeVarInt(buf);
                    } else {
                        count = i;
                    }
                    card.add(new CardPair(count,dbfId));
                }
            }

            try {
                Object obj = parser.parse(new InputStreamReader(this.getClass().getResourceAsStream("cards.collectible.json")));
                JSONArray jsonArray = (JSONArray) obj;
                JSONObject jObj;

                long cardId;
                long cardCostLong;
                int cardCost = 0;
                String cardClass;
                String cardRare;
                String cardName;

                for (Object o : jsonArray){
                    jObj = (JSONObject) o;
                    cardId = (long) jObj.get("dbfId");
                    if(!Objects.isNull(jObj.get("cost"))){
                    cardCostLong = (long) jObj.get("cost");
                        cardCost = (int)cardCostLong;
                    }
                    cardRare = (String) jObj.get("rarity");
                    cardName = (String) jObj.get("name");
                    cardClass = (String) jObj.get("cardClass");

                    for (Integer i : hero){
                        if(i == cardId){
                            switch (cardClass){
                                case "WARRIOR":
                                    heroClass = "Warrior";
                                    classImg = "https://cdn.discordapp.com/emojis/528697094840647692.png?v=1";
                                    break;
                                case "SHAMAN":
                                    heroClass = "Shaman";
                                    classImg = "https://cdn.discordapp.com/emojis/528697097982181416.png?v=1";
                                    break;
                                case "ROGUE":
                                    heroClass = "Rogue";
                                    classImg = "https://cdn.discordapp.com/emojis/528697094769344523.png?v=1";
                                    break;
                                case "PALADIN":
                                    heroClass = "Paladin";
                                    classImg = "https://cdn.discordapp.com/emojis/528697095041843200.png?v=1";
                                    break;
                                case "HUNTER":
                                    heroClass = "Hunter";
                                    classImg = "https://cdn.discordapp.com/emojis/528697062930251776.png?v=1";
                                    break;
                                case "DRUID":
                                    heroClass = "Druid";
                                    classImg = "https://cdn.discordapp.com/emojis/528697035910676491.png?v=1";
                                    break;
                                case "WARLOCK":
                                    heroClass = "Warlock";
                                    classImg = "https://cdn.discordapp.com/emojis/528697112691605515.png?v=1";
                                    break;
                                case "MAGE":
                                    heroClass = "Mage";
                                    classImg = "https://cdn.discordapp.com/emojis/528697049873514496.png?v=1";
                                    break;
                                case "PRIEST":
                                    heroClass = "Priest";
                                    classImg = "https://cdn.discordapp.com/emojis/528697096547598356.png?v=1";
                                    break;
                                default:
                                    heroClass = "";
                            }
                        }
                    }

                    //Calculate dust cost and amount of cards with same mana cost.
                    for(CardPair c : card){
                        if(c.dbfid == cardId && !cardClass.equals("NEUTRAL")){
                            switch (cardCost){
                                case 0:
                                    mana0 = mana0 + c.count;
                                    break;
                                case 1:
                                    mana1 = mana1 + c.count;
                                    break;
                                case 2:
                                    mana2 = mana2 + c.count;
                                    break;
                                case 3:
                                    mana3 = mana3 + c.count;
                                    break;
                                case 4:
                                    mana4 = mana4 + c.count;
                                    break;
                                case 5:
                                    mana5 = mana5 + c.count;
                                    break;
                                case 6:
                                    mana6 = mana6 + c.count;
                                    break;
                                case 7:
                                    mana7 = mana7 + c.count;
                                    break;
                                default:
                                    if(cardCost>7){
                                        mana7 = mana7 + c.count;
                                    }
                            }

                            switch (cardRare){
                                case "COMMON":
                                    dust = dust + 40*c.count;
                                    break;
                                case "RARE":
                                    dust = dust + 100*c.count;
                                    break;
                                case "EPIC":
                                    dust = dust + 400*c.count;
                                    break;
                                case "LEGENDARY":
                                    dust = dust + 1600*c.count;
                                    break;
                                default:
                            }
                            classCardBuild.append(c.count).append("x ").append(cardName).append("(").append(cardCost).append(")").append("\n");
                        }

                        else if(c.dbfid == cardId && cardClass.equals("NEUTRAL")){
                            switch (cardCost){
                                case 0:
                                    mana0 = mana0 + c.count;
                                    break;
                                case 1:
                                    mana1 = mana1 + c.count;
                                    break;
                                case 2:
                                    mana2 = mana2 + c.count;
                                    break;
                                case 3:
                                    mana3 = mana3 + c.count;
                                    break;
                                case 4:
                                    mana4 = mana4 + c.count;
                                    break;
                                case 5:
                                    mana5 = mana5 + c.count;
                                    break;
                                case 6:
                                    mana6 = mana6 + c.count;
                                    break;
                                case 7:
                                    mana7 = mana7 + c.count;
                                    break;
                                default:
                                    if(cardCost>7){
                                        mana7 = mana7 + c.count;
                                    }
                            }

                            switch (cardRare){
                                case "COMMON":
                                    dust = dust + 40*c.count;
                                    break;
                                case "RARE":
                                    dust = dust + 100*c.count;
                                    break;
                                case "EPIC":
                                    dust = dust + 400*c.count;
                                    break;
                                case "LEGENDARY":
                                    dust = dust + 1600*c.count;
                                    break;
                                default:
                            }
                            neutralCardbuild.append(c.count).append("x ").append(cardName).append("(").append(cardCost).append(")").append("\n");
                        }
                    }
                }
            }
            catch (ParseException | IOException e){
                logger.error(e.getMessage());
            }

            EmbedBuilder eB = new EmbedBuilder()
                    .setAuthor(deckInput3+" "+heroClass+" "+"["+hsFormat+"]",null,classImg)
                    .setTitle("Mana Curve:")
                    .setDescription(mana0+"x"+"<:hsmana0:528645542142672918> "+mana1+"x"+"<:hsmana1:528645566012456978> "+
                            mana2+"x"+"<:hsmana2:528645580931596308> "+mana3+"x"+"<:hsmana3:528645595250950147> "+
                            mana4+"x"+"<:hsmana4:528645606495879168> "+mana5+"x"+"<:hsmana5:528645631942721536> "+
                            mana6+"x"+"<:hsmana6:528645647553789954> "+mana7+"x"+"<:hsmana7plus:528645669624479746>")
                    .setColor(new Color(0xF40C0C))
                    .addField("Class Cards:", classCardBuild.toString(),true)
                    .addField("Neutral Cards:", neutralCardbuild.toString(),true)
                    .setFooter(Integer.toString(dust), "https://cdn.discordapp.com/emojis/528697022048501760.png?v=1");
            event.getChannel().sendMessage(userMention + lengthWarn).queue();
            event.getChannel().sendMessage(eB.build()).queue();
        }

        //Test block
        if(userId.equals("178930497399816193") && userInput1.equalsIgnoreCase("!thun test")) {
            int guilds = event.getJDA().getGuilds().size();

            try{
                api.setStats(guilds).toCompletableFuture().get();
                logger.info("Updated server count: " + guilds);
            }
            catch (InterruptedException | ExecutionException e){
                logger.error(e.getMessage());
            }
            event.getChannel().sendMessage("Testing complete. <a:cthunanim:520295664278700059>").queue();
        }

    }
}
