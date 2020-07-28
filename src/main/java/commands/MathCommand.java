package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import parsers.EvaluateMath;

public class MathCommand implements CommandInterfaceBot  {

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String userInput = event.getMessage().getContentRaw().toLowerCase().replace("!thun math", "").trim();
        String userId = event.getAuthor().getId();
        String userMention = "<@" + userId + ">";

        try{
            String numSum = getSum(userInput);
            event.getChannel().sendMessage(userMention + " The result is: " + numSum).queue();
        }
        catch (NumberFormatException ne){
            event.getChannel().sendMessage(userMention + " Invalid math expression.").queue();
        }
        catch (ArithmeticException | IllegalArgumentException e){
            event.getChannel().sendMessage(userMention + " " + e.getMessage()).queue();
        }

    }
    private String getSum(String userInput){
        Locale enLocale = new Locale("en","US");
        NumberFormat nf = NumberFormat.getNumberInstance(enLocale);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("###,###.###");
        EvaluateMath calculate = new EvaluateMath();
        return df.format(calculate.evaluateMath(userInput));
    }

    @Override
    public String getHelp() {
        return "Solves an expression and returns the result.\nSupported operators are: `+`,`-`,  `*` `x`,  `/`and  `^`\n" +
                "Negative and decimal numbers are also supported.\n" +
                "Example usage: `"+getInvoke()+" 1+2x3`";
    }

    @Override
    public String getInvoke() {
        return "!thun math";
    }
}
