package parsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardReader {
    private static final List<String[]> cardList = new ArrayList<>();
    public static int size;
    private static JSONObject jObj;

    public static void cardReader() throws IOException, ParseException {
        cardJson();
        setSize();
    }

    private static void setSize() {
        size = getCardList().size();
    }

    private static void cardJson() throws IOException, ParseException {
        String [] cardsArr;

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(Objects.requireNonNull(CardReader.class.getClassLoader().getResourceAsStream("cthuncardjson.json"))));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray jsonArray = (JSONArray) jsonObject.get("cards");

        for (Object o : jsonArray){
            jObj = (JSONObject) o;
            cardsArr = new String[6];
            cardsArr[0] = getCardName();
            cardsArr[1] = getCardName().toLowerCase();
            cardsArr[2] = getCardDesc();
            cardsArr[3] = getCardInfo();
            cardsArr[4] = getCardFlavour();
            cardsArr[5] = getCardImg();

            if (cardsArr[3].contains("Battlegrounds")){
                cardsArr[0] = cardsArr[0] + " (Battlegrounds)";
                cardsArr[1] = cardsArr[1] + " (battlegrounds)";
            }
            else if (cardsArr[3].contains("Token")){
                cardsArr[0] = cardsArr[0] + " (Token)";
                cardsArr[1] = cardsArr[1] + " (token)";
            }
            else if (cardsArr[3].contains("Collectible")){
                cardsArr[0] = cardsArr[0] + " (Collectible)";
                cardsArr[1] = cardsArr[1] + " (collectible)";
            }

            cardList.add(cardsArr);
        }
    }

    private static String getCardName(){
        return (String) jObj.get("name");
    }
    private static String getCardDesc(){
        return (String) jObj.get("desc");
    }
    private static String getCardInfo(){
        String arcaneDust = "";
        String faction = "";
        String craftCost = "";
        String infoType = "";
        String artist = "";
        String infoSet = "";
        String rarity = "";
        String race = "";
        String infoClass = "";
        StringBuilder infoTags = new StringBuilder();
        String tier = "";

        JSONObject infoObj = (JSONObject) jObj.get("info");
        if (infoObj.containsKey("arcane_dust")){
            arcaneDust = "**Arcane Dust Gained:** " + (String) infoObj.get("arcane_dust") + "\n";
        }
        if (infoObj.containsKey("faction")){
            faction = "**Faction:** " + (String) infoObj.get("faction") + "\n";
        }
        if (infoObj.containsKey("craft_cost")){
            craftCost = "**Crafting Cost:** " + (String) infoObj.get("craft_cost") + "\n";
        }
        if (infoObj.containsKey("type")){
            infoType = "**Type:** " + (String) infoObj.get("type") + "\n";
        }
        if (infoObj.containsKey("artist")){
            artist = "**Artist:** " + (String) infoObj.get("artist") + "\n";
        }
        if (infoObj.containsKey("set")){
            infoSet = "**Set:** " + (String) infoObj.get("set") + "\n";
        }
        if (infoObj.containsKey("rarity")){
            rarity = "**Rarity:** " + (String) infoObj.get("rarity") + "\n";
        }
        if (infoObj.containsKey("race")){
            race = "**Race:** " + (String) infoObj.get("race") + "\n";
        }
        if (infoObj.containsKey("class")){
            infoClass = "**Class:** " + (String) infoObj.get("class") + "\n";
        }
        if (infoObj.containsKey("tier")){
            tier = "**Tier:** " + (String) infoObj.get("tier") + "\n";
        }

        JSONArray infoTagsArr = (JSONArray) infoObj.get("tags");
        for (Object o : infoTagsArr){
            infoTags.append((String) o).append("\n");
        }
        return infoType + infoClass + rarity + infoSet + race + faction + tier + craftCost + arcaneDust + artist + infoTags.toString();
    }
    private static String getCardFlavour(){
        return (String) jObj.get("flavour");
    }
    private static String getCardImg(){
        return (String) jObj.get("image");
    }

    public static List<String[]> getCardList(){
        return cardList;
    }
}



