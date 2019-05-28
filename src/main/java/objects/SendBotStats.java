package objects;

import commands.MentionCommand;
import net.dv8tion.jda.core.JDA;
import okhttp3.*;
import org.discordbots.api.client.DiscordBotListAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendBotStats {
    private static final Logger logger = LoggerFactory.getLogger(SendBotStats.class);
    public static void sendBotStats(JDA builder){

        Runnable updateStatsRunnable = () -> {
            int serverCount = builder.getGuilds().size();
            discordBotList(serverCount);
            discordBotsGg(serverCount);
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updateStatsRunnable, 0, 30, TimeUnit.MINUTES);
    }

    private static void discordBotList(int serverCount){
        DiscordBotListAPI api;
        String apiToken = System.getenv("APIToken");

        api = new DiscordBotListAPI.Builder()
                .token(apiToken)
                .botId(String.valueOf(MentionCommand.CTHUNID))
                .build();
        try{
            api.setStats(serverCount).toCompletableFuture().get();
            logger.info("Server count: " + serverCount);
        }
        catch (InterruptedException | ExecutionException e){
            logger.error(e.getMessage());
        }
    }

    private static void discordBotsGg(int serverCount){
        String siteUrl = ("https://discord.bots.gg/api/v1/bots/474523344864280578/stats");
        org.json.JSONObject json = new org.json.JSONObject().put("guildCount", serverCount);

        String dbApiToken = System.getenv("DBApiToken");

        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization",dbApiToken)
                .post(requestBody)
                .url(siteUrl)
                .build();

        try {
            Response res = httpClient.newCall(request).execute();
            res.close();
        }
        catch (IOException e){
            logger.error("dbGG API",e.getMessage());
        }
    }
}
