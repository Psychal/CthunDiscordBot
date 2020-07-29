package objects;

import commands.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class CommandManagerBot extends ListenerAdapter {

    private final Map<String, CommandInterfaceBot> commandmap = new HashMap<>();

    public CommandManagerBot(Random random){
        addCommand(new ThunCommand(random));
        addCommand(new StatsCommand());
        addCommand(new MathCommand());
        addCommand(new CardCommand(random));
        addCommand(new ReactCommand(random));
        addCommand(new MentionCommand());
        addCommand(new ChangelogCommand());
        addCommand(new HelpCommand(this));
        addCommand(new SearchCommand());
        addCommand(new DeckCommand());
        addCommand(new KeywordCommand());
        addCommand(new ImportCommand());
    }

    private void addCommand(CommandInterfaceBot command) {
        if (!commandmap.containsKey(command.getInvoke())) {
            commandmap.put(command.getInvoke(), command);
        }
    }

    public Collection<CommandInterfaceBot> getCommandValues(){
        return commandmap.values();
    }

    public CommandInterfaceBot getCommand(@NotNull String name){
        return commandmap.get(name);
    }

    private void handleCommand(MessageReceivedEvent event) {
        final String invoke;
        final String [] split = event.getMessage().getContentRaw().split("\\s+");

        if(split.length>1){
            invoke = String.join(" ",split[0].toLowerCase(),split[1].toLowerCase());
        }
        else{
            invoke = split[0].toLowerCase();
        }

        if(Arrays.toString(split).toLowerCase().contains("c'thun") && split.length > 1){
            commandmap.get("c'thun").handle(null,event);
        }

        if(commandmap.containsKey(invoke)){
            System.out.println("Invoke split string: "+Arrays.toString(split));
            final List<String> args = Arrays.asList(split);
            System.out.println("ARGS: "+args.toString());
            commandmap.get(invoke).handle(args, event);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //Ignores messages from bots and webhooks.
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage()) {
            return;
        }
        handleCommand(event);
        if(event.getChannelType().isGuild()){
            String guild = event.getGuild().getId();
        }
        String userId = event.getAuthor().getId();
        Map<String, SearchCommand.SearchMapValues> searchMap = SearchCommand.getSearchMap();
        if (searchMap != null && searchMap.containsKey(userId)) {
            SearchCommand.getSearchInputResult(event);
        }
        Map<Long, ImportCommand.DeckMapValues> deckMap = ImportCommand.getDeckMap();
        if (deckMap != null && deckMap.containsKey(event.getAuthor().getIdLong())) {
            try {
                ImportCommand.getSearchInputResult(event);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}