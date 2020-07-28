package parsers;

import org.json.simple.parser.ParseException;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.*;

class ParseDeck {
    String hsFormat;
    int version;
    List<CardPair> cardsArrList = new ArrayList<>();
    List<Integer> heroArrList = new ArrayList<>();

    ParseDeck(String deckInput) throws ParseException {
        ByteBuffer buf = getBuffer(decodeInput(deckInput));
        parseBuffer(buf);
    }

    private byte[] decodeInput(String deckInput){
        return Base64.getDecoder().decode(deckInput);
    }
    private ByteBuffer getBuffer(byte[] deck) {
        //VarInts placed in Buffer for decoding and usage.
        return ByteBuffer.wrap(deck);
    }
    private void parseBuffer(ByteBuffer buf) throws ParseException, BufferUnderflowException {
        //Header block: Reserved byte, Version, Format.
        buf.get(); //Reserved byte
        version = getVersion(buf);
        hsFormat = getHsFormat(buf);
        //Card block: Heroes, Cards
        addHero(buf);
        addCards(buf);
    }
    private int getVersion(ByteBuffer buf) throws ParseException {
        //Version(Should always be 1)
        int version = buf.get();
        if(version !=1)
        {
            throw new ParseException(version);
        }
        return version;
    }
    //Format(1=Wild, 2=Standard)
    private String getHsFormat(ByteBuffer buf){
        int formatVal = DecodeVarInt.decodeVarInt(buf);
        return formatVal == 1 ? "Wild": (formatVal == 2 ? "Standard" : "Invalid format");
    }

    // Heroes(amount, dbfid(s)).
    private void addHero(ByteBuffer buf){
        int heroCount = DecodeVarInt.decodeVarInt(buf);
        for(int i = 0; i<heroCount; i++){
            heroArrList.add(DecodeVarInt.decodeVarInt(buf));
        }
    }

    //Cards: Single-cards(amount, dbfid(s)), 2-cards(amount, dbfid(s)), n-cards(dbfid, amount).
    private void addCards(ByteBuffer buf){
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
                cardsArrList.add(new CardPair(count,dbfId));
            }
        }
    }
    //Deckstring Cardpair.
    public static class CardPair {
        public final int count;
        public final int dbfid;

        CardPair(int countP, int dbfidP) {
            this.count = countP;
            this.dbfid = dbfidP;
        }
    }
}