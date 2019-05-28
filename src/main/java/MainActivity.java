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
}

