package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.util.List;

public class ChangelogCommand implements CommandInterfaceBot {
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("```C'thun Changelog \n\n" +
                "# Changelog\n" +
                "Only the few most recent changelog entries will be listed here\n" +
                "\n" +
                "## [Unreleased]\n" +
                "- Add feature to import public decklist from hearthpwn.\n" +
                "- Add images for cards that do not currently have any, but which exist.\n" +
                "- Add artist name to all cards with images.\n" +
                "- Add unplayable removed cards.\n" +
                "- Implement a system for duplicate entries with different stats.\n\n" +
                "## - 2019-05-28\n" +
                "### Added\n" +
                "- Card entries from Rise of Shadows Patch 14.2\n" +
                "- Feature to help command. You can now append another command behind it for usage info.\n" +
                "- Keyword command. Lists the description of a keyword you search for.\n\n" +
                "### Changed\n" +
                "- Updated existing card entries affected by the 2019-05-22 nerf.\n" +
                "- Huge backend code changes, issues might occur.\n\n" +
                "### Removed\n" +
                "- Token, Elite and Collectible command have been removed and implement into the keyword command instead.\n\n" +
                "## - 2019-04-07\n" +
                "### Added\n" +
                "- Card entries from Rise of Shadows expansion (Patch 13.4).\n\n" +
                "## - 2019-03-02\n" +
                "### Changed\n"+
                "- Searching should be a little less specific now, and will only provide a list if more than 1 card is found.\n" +
                "- Tags: Token and Collectible, has been appended to names to make it easier to discern different cards.\n\n"+
                "## - 2019-02-10\n" +
                "### Changed\n" +
                "- Updated existing entries to Patch 13.2.0.28855(2019-02-05)\n" +
                "- Math command now support decimals, negative numbers, exponentiation operator and you can use X instead of * as multiplication operator."

        ).queue();
    }

    @Override
    public String getHelp() {
        return "Posts C'thun's changelog which lists only the most recent changes. Join the bot's support server to gain access to the full changelog.\n" +
                "Example usage: `"+getInvoke()+"`";
    }

    @Override
    public String getInvoke() {
        return "!thun changelog";
    }
}
