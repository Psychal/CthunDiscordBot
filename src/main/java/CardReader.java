import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

final class CardReader {
    static List<String[]> cardReader() throws IOException {
        List<String[]> cardList = new ArrayList<>();
        int indexArray = 0;
        String card;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CardReader.class.getClassLoader().getResourceAsStream("Cards.txt")));
             BufferedReader reader2 = new BufferedReader(new InputStreamReader(CardReader.class.getClassLoader().getResourceAsStream("CardsIMG.txt"))))
        {
            //[0]=Name, [1]=Lowercase-name,[2]=Desc, [3]=Info,[4]=Flavour, [5]=Img,
            String [] cardsArr = new String[6];
            while ((card = reader.readLine()) != null) {
                if(card.startsWith("NAME: ")) {
                    cardsArr = new String[6];
                    cardsArr[0] = card.replace("NAME: ","");
                    cardsArr[1] = card.toLowerCase().replace("name: ", "");
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
                }
                else if(card.startsWith("FLAVOUR: ")) {
                    cardsArr[4] = card.replace("FLAVOUR: ","")
                            .replace("<i>","*")
                            .replace("</i>","*");
                    cardList.add(cardsArr);
                }
            }

            while ((card = reader2.readLine()) != null) {
                if(card.startsWith("IMG: ")){
                    cardList.get(indexArray)[5] = card.replace("IMG: ", "");
                    indexArray++;
                }
            }
            return cardList;
        }
    }
}

//Deckstring Cardpair.
class CardPair {
    final int count;
    final int dbfid;

    CardPair(int countP, int dbfidP) {
        this.count = countP;
        this.dbfid = dbfidP;
    }
}
//Card info for Searchmap.
class SearchMapValues{
    final int count;
    final List<Integer> index;
    final Timer timer;

    SearchMapValues(int numbers, List<Integer> indexArray, Timer timers){
        this.count = numbers;
        this.index = indexArray;
        this.timer = timers;
    }
}
