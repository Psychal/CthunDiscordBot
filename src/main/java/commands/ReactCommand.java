package commands;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReactCommand implements CommandInterfaceBot {
    private final Random random;

    public ReactCommand(Random random){
        this.random = random;
    }
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        List <CthunEmotes> emotes = new ArrayList<>();
        addEmotes(emotes,event);
        int n = random.nextInt(emotes.size());
        CthunEmotes emoji = emotes.get(n);
        if (emoji.emote != null){
            event.getMessage().addReaction(emoji.emote).queue();
        }
        else {
            event.getMessage().addReaction(emoji.unicode).queue();
        }
    }

    private class CthunEmotes{
        private String unicode;
        private Emote emote;
        private CthunEmotes (Emote emote) {
            this.emote = emote;
        }
        private CthunEmotes (String unicode){
            this.unicode = unicode;
        }
    }

    private void addEmotes(List<CthunEmotes> emoteList, MessageReceivedEvent event){
        emoteList.add(new CthunEmotes(event.getJDA().getEmoteById(520295664278700059L)));
        emoteList.add(new CthunEmotes(event.getJDA().getEmoteById(520288408690491412L)));
        emoteList.add(new CthunEmotes(event.getJDA().getEmoteById(520071874969862144L)));
        emoteList.add(new CthunEmotes(event.getJDA().getEmoteById(579480694229696513L)));

        emoteList.add(new CthunEmotes("\uD83D\uDC40"));
        emoteList.add(new CthunEmotes("\uD83D\uDC41"));
        emoteList.add(new CthunEmotes("\u2620"));
        emoteList.add(new CthunEmotes("\uD83D\uDC80"));
        emoteList.add(new CthunEmotes("\uD83D\uDDE1"));
    }

    @Override
    public String getHelp() {
        return "Every time you mention C'thun's name, you'll invoke one of his reactions.";
    }

    @Override
    public String getInvoke() {
        return "c'thun";
    }
}
