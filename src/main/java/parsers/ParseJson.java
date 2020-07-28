package parsers;

import objects.Card;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ParseJson {
    enum Heroes{
        WARRIOR("Warrior","https://cdn.discordapp.com/emojis/528697094840647692.png?v=1"), SHAMAN("Shaman","https://cdn.discordapp.com/emojis/528697097982181416.png?v=1"),
        ROGUE("Rogue","https://cdn.discordapp.com/emojis/528697094769344523.png?v=1"), PALADIN("Paladin","https://cdn.discordapp.com/emojis/528697095041843200.png?v=1"),
        HUNTER("Hunter","https://cdn.discordapp.com/emojis/528697062930251776.png?v=1"), DRUID("Druid","https://cdn.discordapp.com/emojis/528697035910676491.png?v=1"),
        WARLOCK("Warlock","https://cdn.discordapp.com/emojis/528697112691605515.png?v=1"), MAGE("Mage","https://cdn.discordapp.com/emojis/528697049873514496.png?v=1"),
        PRIEST("Priest","https://cdn.discordapp.com/emojis/528697096547598356.png?v=1"), DEMONHUNTER("Demon Hunter","https://cdn.discordapp.com/emojis/697944393369387148.png?v=1");

        String hero;
        String img;

        Heroes(String hero, String img) {
            this.hero = hero;
            this.img = img;
        }
        public String getHero(){
            return hero;
        }
        public String getImg(){
            return img;
        }
    }

    public String hero;
    public String heroImg;
    public String manaCurve;
    public String hsFormat;
    public int dust = 0;
    public StringBuilder classCardBuild = new StringBuilder();
    public StringBuilder neutralCardBuild = new StringBuilder();
    private Map<String, Integer> rarity = new HashMap<>();
    private JSONObject jObj;
    private List<Card> classCards = new ArrayList<>();
    private List<Card> neutralCards = new ArrayList<>();

    public ParseJson(String deckInput) throws IOException, ParseException {
        ParseDeck deck = new ParseDeck(deckInput);
        hsFormat = deck.hsFormat;
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("cards.collectible.json"))));
        rarityMap(rarity);
        parsingCards(deck.heroArrList,deck.cardsArrList,obj);

    }

    private void parsingCards(List<Integer> heroArrList, List<ParseDeck.CardPair> cardsArrList, Object obj){
        int cardCost = 0;
        int[]mana = new int[8];
        JSONArray jsonArray = (JSONArray) obj;

        for (Object o : jsonArray){
            jObj = (JSONObject) o;
            if(!Objects.isNull(jObj.get("cost"))){
                long cost = (long) jObj.get("cost");
                cardCost = (int) cost;
            }
            setHero(heroArrList);
            appendCards(cardsArrList,cardCost,mana);
            manaCurve = getManaCurve(mana);
        }
        sortCards(classCards,neutralCards);

    }

    private void appendCards(List<ParseDeck.CardPair> cardsArrList, int cardCost, int[] mana){
        for(ParseDeck.CardPair c : cardsArrList){
            if(c.dbfid == getCardId()){
                if(!getCardClass().equals("NEUTRAL")){
                    classCards.add(new Card(getCardName(),c.count,cardCost));
                }
                else{
                    neutralCards.add(new Card(getCardName(),c.count,cardCost));
                }
                cardCost = Math.min(cardCost, 7);
                mana[cardCost < 0 ? 7 : cardCost] += c.count;
                dust += rarity.getOrDefault(getCardRarity(),0)*c.count;
            }
        }
    }

    private void sortCards(List<Card> classCards, List<Card> neutralCards){
        classCards.sort(Comparator.comparing(Card::getMana).thenComparing(Card::getName));
        neutralCards.sort(Comparator.comparing(Card::getMana).thenComparing(Card::getName));
        for (Card c : classCards){
            classCardBuild.append(c.count).append("x ").append(c.name).append("(").append(c.mana).append(")\n");
        }
        for (Card c : neutralCards){
            neutralCardBuild.append(c.count).append("x ").append(c.name).append("(").append(c.mana).append(")\n");
        }
    }

    private void setHero(List<Integer> heroArrList){
        for (Integer i : heroArrList){
            if(i == getCardId()){
                hero = Heroes.valueOf(getCardClass()).getHero();
                heroImg = Heroes.valueOf(getCardClass()).getImg();
            }
        }
    }
    private long getCardId(){
        return (long) jObj.get("dbfId");
    }
    private String getCardRarity(){
        return (String) jObj.get("rarity");
    }
    private String getCardName(){
        return (String) jObj.get("name");
    }
    private String getCardClass(){
        return (String) jObj.get("cardClass");
    }

    private void rarityMap(Map<String, Integer> rarity){
        rarity.put("COMMON",40);
        rarity.put("RARE",100);
        rarity.put("EPIC",400);
        rarity.put("LEGENDARY",1600);
    }
    private String getManaCurve(int[] mana){
        return mana[0]+"x<:hsmana0:528645542142672918> "+mana[1]+"x<:hsmana1:528645566012456978> "+
                mana[2]+"x<:hsmana2:528645580931596308> "+mana[3]+"x<:hsmana3:528645595250950147> "+
                mana[4]+"x<:hsmana4:528645606495879168> "+mana[5]+"x<:hsmana5:528645631942721536> "+
                mana[6]+"x<:hsmana6:528645647553789954> "+mana[7]+"x<:hsmana7plus:528645669624479746>";
    }
}