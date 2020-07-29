import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.*;
import javax.security.auth.login.LoginException;

import objects.SendBotStats;
import objects.Uptime;
import org.json.simple.parser.ParseException;
import parsers.CardReader;
import objects.CommandManagerBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private static final Random random = new Random();

    public static void main(String[] args) throws LoginException, IOException, SQLException, ParseException {
        String botToken = System.getenv("BotToken");

        try{
            JDA builder = JDABuilder.createDefault(botToken)
                    .addEventListeners(new CommandManagerBot(random))
                    .setActivity(Activity.playing("!thun help"))
                    .build();
            builder.awaitReady();
            new SendBotStats().sendBotStats(builder);
        }
        catch(InterruptedException e){
            logger.error(e.getMessage());
        }
        CardReader.cardReader();
        Uptime.setStart(System.currentTimeMillis());

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
