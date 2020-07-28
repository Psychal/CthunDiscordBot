package objects;

public class Card {
    public final String name;
    public final int count;
    public final int mana;

    public Card(String name, int count, int mana) {
        this.name = name;
        this.count = count;
        this.mana = mana;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getMana() {
        return mana;
    }
}
