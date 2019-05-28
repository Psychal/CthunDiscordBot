import commands.SearchCommand;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.Color;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import objects.SendBotStats;
import parsers.CardReader;
import objects.CommandManagerBot;

import parsers.EvaluateMath;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static final Random random = new Random();

    public static void main(String[] args) throws LoginException, IOException, SQLException {
        String botToken = System.getenv("BotToken");

        try{
            JDA builder = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .addEventListener(new CommandManagerBot(random))
                    .setGame(Game.playing("!thun help"))
                    .build();
            builder.awaitReady();
            SendBotStats.sendBotStats(builder);
        }
        catch(InterruptedException e){
            logger.error(e.getMessage());
        }
        CardReader.cardReader();

        //Database work-in-progress.
        Connection connection = DatabaseConnection.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
    }
//        //Test block
//        if(userId.equals("178930497399816193") && userInput1.equalsIgnoreCase("!thun test")) {
//            int guilds = event.getJDA().getGuilds().size();
//            long guildID = event.getGuild().getIdLong();
//
//
//            try {
//                Connection connection = DatabaseConnection.getConnection();
//                Statement stmt = connection.createStatement();
//                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO guilds(guildID) VALUES (?)");
//                pstmt.setLong(1,guildID);
//                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS guilds (guildID bigint PRIMARY KEY)");
//                pstmt.executeUpdate();
//                pstmt.close();
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//            event.getChannel().sendMessage("Testing complete. <a:cthunanim:520295664278700059>").queue();
//        }
//        if(userId.equals("178930497399816193") && userInput1.equalsIgnoreCase("!thun test2")){
//            try {
//                Connection connection = DatabaseConnection.getConnection();
//                Statement stmt = connection.createStatement();
//                ResultSet rs = stmt.executeQuery("SELECT guildID FROM guilds");
//                while (rs.next()) {
//                    System.out.println("Read from DB: " + rs.getLong("guildID"));
//                }
//            }
//            catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(event.getMember().hasPermission(Permission.MANAGE_SERVER) && userInput1.equalsIgnoreCase("!thun test3")){
//            String manageServer = "You have manage server permission.";
//            String admin = "\nYou also have administrator permissions.";
//            String owner = "\nYou're also the owner of the server. WOW!";
//
//            event.getMember().isOwner();
//            event.getMember().hasPermission(Permission.MANAGE_SERVER);
//            event.getMember().hasPermission(Permission.ADMINISTRATOR);
//            if (event.getMember().hasPermission(Permission.MANAGE_SERVER)){
//                if (event.getMember().hasPermission(Permission.ADMINISTRATOR)){
//                    if (event.getMember().isOwner()){
//                        event.getChannel().sendMessage(manageServer+admin+owner).queue();
//                    }
//                    else {
//                        event.getChannel().sendMessage(manageServer+admin).queue();
//                    }
//                }
//                else {
//                    event.getChannel().sendMessage(manageServer).queue();
//                }
//            }
//        }
}

