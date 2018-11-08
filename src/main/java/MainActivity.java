import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.EmbedBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.*;
import java.util.*;

import javax.security.auth.login.LoginException;

public class MainActivity extends ListenerAdapter {
    private static List<String> cardsN;
    private static List<String> cardsD;
    private static List<String> cardsI;
    private static List<String> cardsF;
    private static List<String> cardsImg;
    private static List<String> cardsLcase;
    private static Map<String, List<Integer>> searchMap;
    private static Map<String, Timer> timerMap;
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    //Evaluates the math expression
    private static int evaluate(String expression)
    {
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder strBuild = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    strBuild.append(tokens[i++]);
                }
                i--;
                values.push(Integer.parseInt(strBuild.toString()));
            }
            else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            }
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(calcOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            }
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && precedence(tokens[i], ops.peek())) {
                    values.push(calcOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }
        while (!ops.empty()) {
            values.push(calcOp(ops.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }

    private static boolean precedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }

    private static int calcOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }

    public static void main(String[] args) throws LoginException {
        String token = "token";
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListener(new MainActivity());
        builder.setGame(Game.playing("!thun help"));
        builder.build();
        cardReader();
        searchMap = new HashMap<>();
        timerMap = new HashMap<>();
    }

    private static void cardReader() {
        cardsN = new ArrayList<>();
        cardsD = new ArrayList<>();
        cardsI = new ArrayList<>();
        cardsF = new ArrayList<>();
        cardsImg = new ArrayList<>();
        cardsLcase = new ArrayList<>();
        String card;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Mikal\\Desktop\\Cards.txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader("C:\\Users\\Mikal\\Desktop\\CardsIMG.txt"));
            while ((card = reader.readLine()) != null) {
                if(card.startsWith("NAME: ")) {
                    cardsN.add(card.replace("NAME: ",""));
                    cardsLcase.add(card.toLowerCase().replace("name: ", ""));
                }
                else if(card.startsWith("DESC: ")) {
                    cardsD.add(card.replace("DESC: ",""));
                }
                else if(card.startsWith("INFO: ")) {
                    cardsI.add(card.replace("INFO: ","")
                            .replace("Type:","**Type:**")
                            .replace("Token","\nToken")
                            .replace("Elite","\nElite")
                            .replace("Collectible","\nCollectible")
                            .replace("Rarity:","\n**Rarity:**")
                            .replace("Faction:","\n**Faction:**")
                            .replace("Class:","\n**Class:**")
                            .replace("Artist:","\n**Artist:**")
                            .replace("Crafting Cost:","\n**Crafting Cost:**")
                            .replace("Arcane Dust Gained:","\n**Arcane Dust Gained:**")
                            .replace("Race:","\n**Race:**")
                            .replace("Set:","\n**Set:**"));
                }
                else if(card.startsWith("FLAVOUR: ")) {
                    cardsF.add(card.replace("FLAVOUR: ","")
                            .replace("<i>","*")
                            .replace("</i>","*"));
                }
            }
            while ((card = reader2.readLine()) != null) {
                if(card.startsWith("IMG: ")){
                    cardsImg.add(card.replace("IMG: ", ""));
                }
            }
            reader.close();
            reader2.close();
            logger.info(java.time.LocalDateTime.now().withNano(0)+ " " + cardsLcase.size() + " Cards ready");
        }
        catch (IOException e) {
            logger.error(java.time.LocalDateTime.now().withNano(0)+ " " + e.getMessage());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
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
                    String numSum = Integer.toString(evaluate(userInput2));
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
                    "Bleed for C'thun."
            };
            Random rand = new Random();
            //32 is the maximum and the 0 is our minimum(0-32), can also use rand.nextInt((max - min) + 1) + min
            int n = rand.nextInt(32);
            event.getChannel().sendMessage(quotes[n]).queue();
        }

        //Uses custom emote from discord room.
        if (userInput1.toLowerCase().contains("eye for an eye")) {
            Emote emote = event.getJDA().getEmoteById(361675944190279690L);
            event.getMessage().addReaction(emote).queue();
        }

        //Uses regular emote which requires unicode character.
        if (userInput1.toLowerCase().contains("c'thun")) {
            Random rand = new Random();
            int n = rand.nextInt(5);
            String [] unicode = {
                    "\uD83D\uDC40",
                    "\uD83D\uDC41",
                    "\u2620",
                    "\uD83D\uDC80",
                    "\uD83D\uDDE1"};
            event.getMessage().addReaction(unicode[n]).queue();
        }

        //C'thun pinged.
        if(userInput1.equals("<@474523344864280578>")) {
            EmbedBuilder eB = new EmbedBuilder()
                    .setTitle("Info")
                    .setColor(new Color(0xEAED1F))
                    .setDescription("This bot uses Hearthstone card information available at Hearthpwn's website.\n" +
                            "You can find the command list by typing: `!thun help`\n" +
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

            int index = cardsLcase.indexOf(searchInput);
            if(index == -1) {
                int c=0;
                List<Integer> cSearch = new ArrayList<>();
                EmbedBuilder sB = new EmbedBuilder();
                StringBuilder sResult = new StringBuilder();
                for(int i=0;i<cardsLcase.size();i++) {
                    if (cardsLcase.get(i).contains(searchInput)) {
                        cSearch.add(i);
                        c++;
                        sResult.append(c).append(". ").append(cardsN.get(i)).append("\n");
                    }
                    if(c==10) {
                        break;
                    }
                }

                //If search is not completely empty
                if(!sResult.toString().equals("")) {
                    if (timerMap.containsKey(userId)){
                        timerMap.get(userId).cancel();
                    }
                    Timer timer = new Timer();
                    searchMap.put(userIdSearch, cSearch);
                    timerMap.put(userIdSearch,timer);
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
                            searchMap.remove(userId);
                            timerMap.remove(userId);
                        }
                    };
                    timerMap.get(userId).schedule(task,60000);
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
                    if (!cardsImg.get(index).equals("")) {
                        img = cardsImg.get(index);
                    }
                    EmbedBuilder eB = new EmbedBuilder()
                        .setTitle(cardsN.get(index))
                        .setColor(new Color(0xF40C0C))
                        .setDescription(cardsD.get(index))
                        .addField("Details:", cardsI.get(index),true)
                        .addBlankField(true)
                        .setFooter(cardsF.get(index), "https://i.imgur.com/s5pGb2D.png")
                        .setImage(img);
                    event.getChannel().sendMessage(userMention).queue();
                    event.getChannel().sendMessage(eB.build()).queue();
                }
        }
        if (searchMap.containsKey(userId)) {
            try {
                int i = Integer.parseInt(userInput1);
                if (i <= 10) {
                    timerMap.get(userId).cancel();
                    timerMap.remove(userId);
                    List<Integer> cSearch2 = searchMap.get(userId);
                    int index = cSearch2.get(i-1);
                    String img = null;
                if (!cardsImg.get(index).equals("")) {
                    img = cardsImg.get(index);
                }
                EmbedBuilder eB = new EmbedBuilder()
                    .setTitle(cardsN.get(index))
                    .setColor(new Color(0xF40C0C))
                    .setDescription(cardsD.get(index))
                    .addField("Details:", cardsI.get(index),true)
                    .addBlankField(true)
                    .setFooter(cardsF.get(index), "https://i.imgur.com/s5pGb2D.png")
                    .setImage(img);
                event.getChannel().sendMessage(eB.build()).queue();
                searchMap.remove(userId);
                }
            }
            catch (NumberFormatException ex){
                logger.info(java.time.LocalDateTime.now().withNano(0)+ " " + ex.getMessage());
            }
        }
        //Card embed.
        if (userInput1.equalsIgnoreCase("!thun card")) {
            Random rand = new Random();
            //28 is the maximum and the 0 is our minimum(0-28), alternatively use rand.nextInt((max - min) + 1) + min
            int n = rand.nextInt(cardsN.size());
            String img = null;
            if (!cardsImg.get(n).equals("")) {
                img = cardsImg.get(n);
            }
            EmbedBuilder eB = new EmbedBuilder()
            .setTitle(cardsN.get(n))
            .setColor(new Color(0xF40C0C))
            .setDescription(cardsD.get(n))
            .addField("Details:", cardsI.get(n),true)
            .addBlankField(true)
            .setFooter(cardsF.get(n), "https://i.imgur.com/s5pGb2D.png")
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
                            "`!thun card` \n Posts a random hearthstone card in the form of a discord embed.\n\n" +
                            "`!thun search [word]` \nEnter a word or full name to perform a search. Yields a maximum of 10 results. Result input lasts only for 60 seconds. \n\n" +
                            "`!thun math [expression]`\n Enter an expression to make C'thun solve it. He can currently only solve addition, subtraction, division and multiplication of whole numbers.\n\n" +
                            "`!thun token` `!thun collectible` `!thun elite`\nThese are card keyword commands to bring up a description of what each keyword means.\n\n" +
                            "`!thun stats`\n Stats and other info about the bot.")
                    .setFooter("Commands are case insensitive.",null)
                    .setThumbnail("https://i.imgur.com/s5pGb2D.png");
            event.getChannel().sendMessage(eB.build()).queue();
        }
    }
}
