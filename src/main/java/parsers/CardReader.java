package parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardReader {
    private static final List<String[]> cardList = new ArrayList<>();

    public static void cardReader() throws IOException {
        cardText();
        cardImage();
    }

    private static void cardText()throws IOException{
        String card;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(CardReader.class.getClassLoader().getResourceAsStream("Cards.txt")))))
        {
            //[0]=Name, [1]=Lowercase-name,[2]=Desc, [3]=Info,[4]=Flavour, [5]=Img,
            String [] cardsArr = new String[6];
            while ((card = reader.readLine()) != null) {
                if(card.startsWith("NAME: ")) {
                    cardsArr = new String[6];
                    cardsArr[0] = card.replace("NAME: ","")
                            .replace("Pi?ata","Pinata")
                            .replace("D?j?","Deja")
                            .replace("?s","'s");
                    cardsArr[1] = card.toLowerCase().replace("name: ", "")
                            .replace("pi?ata","pinata")
                            .replace("d?j?","deja")
                            .replace("?s","'s");
                }
                else if(card.startsWith("DESC: ")) {
                    cardsArr[2] = card.replace("DESC: ","");
                }
                else if(card.startsWith("INFO: ")) {
                    cardsArr[3] = card.replace("INFO: ","")
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
                            .replace("Set:","\n**Set:**");

                    if (cardsArr[3].contains("Token")){
                        cardsArr[0] = cardsArr[0] + " (Token)";
                        cardsArr[1] = cardsArr[1] + " (token)";
                    }
                    if (cardsArr[3].contains("Collectible")){
                        cardsArr[0] = cardsArr[0] + " (Collectible)";
                        cardsArr[1] = cardsArr[1] + " (collectible)";
                    }
                }
                else if(card.startsWith("FLAVOUR: ")) {
                    cardsArr[4] = card.replace("FLAVOUR: ","")
                            .replace("<i>","*")
                            .replace("</i>","*")
                            .replace("?s","'s")
                            .replace("?S","'S")
                            .replace("?re","'re");

                    cardList.add(cardsArr);
                }
            }
         }
    }
    private static void cardImage()throws IOException{
        int indexArray = 0;
        String card;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(CardReader.class.getClassLoader().getResourceAsStream("CardsIMG.txt")))))
        {
            while ((card = reader.readLine()) != null) {
                if(card.startsWith("IMG: ")){
                    cardList.get(indexArray)[5] = card.replace("IMG: ", "");
                    indexArray++;
                }
            }
        }
    }

    public static List<String[]> getCardList(){
        return cardList;
    }

}



