package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.util.List;
import java.util.Random;

public class ThunCommand implements CommandInterfaceBot {
    private final Random random;

    public ThunCommand(Random random){
        this.random = random;
    }
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
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
                "https://i.imgur.com/W2TIogP.gif",
                "Cthulhu? Who is that?",
                "It's all fun and games until someone loses an eye.",
                "Y'knath k'th'rygg k'yi mrr'ungha gr'mula.",
                "At the bottom of the ocean even light must die.",
                "Even death may die." ,
                "All places, all things have souls. All souls can be devoured.",
                "There is a little lamb lost in dark woods.",
                "The silent, sleeping, staring houses in the backwoods always dream. It would be merciful to tear them down.",
                "The void sucks at your soul. It is content to feast slowly.",
                "There is no sharp distinction between the real and the unreal." ,
                "The stars sweep chill currents that make men shiver in the dark." ,
                "What can change the nature of a man?",
                "Look around. They will all betray you. Flee screaming into the black forest.",
                "Do you dream while you sleep or is it an escape from the horrors of reality?",
                "In the land of Ny'alotha there is only sleep.",
                "In the sleeping city of Ny'alotha walk only mad things.",
                "Ny'alotha is a city of old, terrible, unnumbered crimes.",
                "The drowned god's heart is black ice.",
                "In the sunken city, he lays dreaming.",
                "Have you had the dream again? A black goat with seven eyes that watches from the outside.",
                "It is standing right behind you. Do not move. Do not breathe.",
                "The fish know all the secrets. They know the cold. They know the dark.",
                "The giant rook watches from the dead trees. Nothing breathes beneath his shadow.",
                "The tortured spirits of your ancestors cling to you, screaming in silence. Apparently they are quite numerous.",
                "You resist. You cling to your life as if it actually matters. You will learn.",
                "I can taste the essence of your soul... it is sweet...",
                "Pineapple or no pineapple on pizza? Let the chaos commence!",
                "Of course, no ancient unknowable evil is complete without a dedicated host of capable cultists.",
                "If you only have one eye, do you blink or wink?",
                "Eye hope my puns aren't getting too cornea.",
                "<:cthuntentacle:579480694229696513> <:cthuneye:520071874969862144> <:cthuntentacle:579480694229696513> *Death is close...*"
        };
        int n = random.nextInt(quotes.length);
        event.getChannel().sendMessage(quotes[n]).queue();
    }

    @Override
    public String getHelp() {
        return "C'thun will provide a sentence of flavour text.";
    }

    @Override
    public String getInvoke() {
        return "!thun";
    }
}