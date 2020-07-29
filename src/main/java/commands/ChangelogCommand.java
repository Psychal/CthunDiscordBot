package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
                "- Add images for cards that do not currently have any, but which exist.\n" +
                "- Add artist name to all cards with images.\n" +
                "- Add unplayable removed cards.\n" +
                "- Implement a system for duplicate entries with different stats.\n\n" +
                "## - 2020-07-29\n" +
                "### Added\n" +
                "- Artist name to multiple card entries\n" +
                "- Import command now provides deck string alongside the deck embed.\n" +
                "- New keywords.\n\n" +
                "## - 2020-07-19\n" +
                "### Changed\n" +
                "- Card entries updated to Patch 17.6.0.53261\n\n"+
                "## - 2020-05-30\n" +
                "### Changed\n" +
                "- Card entries updated to Patch 17.2.2.48705\n\n" +
                "## - 2020-04-10\n" +
                "### Fixed\n" +
                "- Demon hunter decks not working\n" +
                "- Ashes of outland cards having wrong image\n\n" +
                "### Changed\n" +
                "- Card entries affected by nerfs (Patch 17.0.0.45310)\n\n" +
                "## - 2020-03-29\n" +
                "### Added\n" +
                "- Card entries from Ashes of Outland (Patch 17.0.0.44222)" +
                "## - 2020-02-01\n" +
                "### Added\n" +
                "- Card entries from Galakrond's Awakening (Patch 16.2.0.39954)\n\n" +
                "## - 2019-12-13\n" +
                "### Added\n" +
                "- Card entries from Descent of Dragons expansion (Patch 16.0.0.37060)\n\n"+
                "## - 2019-11-29\n" +
                "### Added\n" +
                "- Import command. Imports decks from hearthpwn by username or direct link.\n\n" +
                "### Fixed\n" +
                "- DM function on the help command.\n\n```"

        ).queue();
    }

    @Override
    public String getHelp() {
        return "Posts C'thun's changelog which lists only the most recent changes. Full changelog available in the support server.\n" +
                "Example usage: `"+getInvoke()+"`";
    }

    @Override
    public String getInvoke() {
        return "!thun changelog";
    }
}
