package objects;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public interface CommandInterfaceBot {

    void handle (List<String> args, MessageReceivedEvent event);

    String getHelp();
    String getInvoke();

}
