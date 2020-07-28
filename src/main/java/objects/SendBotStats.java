package objects;

import commands.MentionCommand;
import net.dv8tion.jda.api.JDA;
import okhttp3.*;
import org.discordbots.api.client.DiscordBotListAPI;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendBotStats {
    private static final Logger logger = LoggerFactory.getLogger(SendBotStats.class);
    private OkHttpClient httpClient = new OkHttpClient();
    private static String token = "";
    private static String siteUrl = "";
    public void sendBotStats(JDA builder){

        Runnable updateStatsRunnable = () -> {
            int serverCount = builder.getGuilds().size();
            int userCount = builder.getUsers().size();
            botlistSpace(serverCount);
            discordBotListCom(serverCount,userCount);
            divineDiscordBots(serverCount);
            discordBotList(serverCount);
            discordBotsGg(serverCount);
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updateStatsRunnable, 0, 30, TimeUnit.MINUTES);
    }

    private void botlistSpace(int serverCount){
        token = System.getenv("BLSPACE_TOKEN");
        siteUrl = "https://api.botlist.space/v1/bots/"+MentionCommand.CTHUNID;
        JSONObject json = new JSONObject().put("server_count",serverCount);
        apiCall(token,siteUrl,json);
    }
    private void discordBotListCom(int serverCount, int userCount){
        token = System.getenv("DBLCOM_TOKEN");
        siteUrl = "https://discordbotlist.com/api/bots/"+MentionCommand.CTHUNID+"/stats";
        JSONObject json = new JSONObject().put("guilds",serverCount);
        json.put("users",userCount);
        apiCall("Bot "+token,siteUrl,json);
    }
    private void divineDiscordBots(int serverCount){
       token = System.getenv("DIVINE_TOKEN");
        siteUrl =("https://divinediscordbots.com/bot/"+MentionCommand.CTHUNID+"/stats");
        JSONObject json = new JSONObject().put("server_count",serverCount);
        apiCall(token,siteUrl,json);
    }
    private void discordBotsGg(int serverCount){
        token = System.getenv("DBApiToken");
        siteUrl = ("https://discord.bots.gg/api/v1/bots/"+MentionCommand.CTHUNID+"/stats");
        JSONObject json = new JSONObject().put("guildCount", serverCount);
        apiCall(token,siteUrl,json);
    }

    private void discordBotList(int serverCount){
       token = System.getenv("APIToken");
        DiscordBotListAPI api;
        api = new DiscordBotListAPI.Builder()
                .token(token)
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

    private void apiCall(String token, String url, JSONObject json){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json.toString().getBytes(StandardCharsets.US_ASCII));
        Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .post(requestBody)
                .url(url)
                .build();

        try {
            Response res = httpClient.newCall(request).execute();
            if(!res.isSuccessful()){
            logger.info(url+"\nResponse code: "+res.code()+"\nResponse: "+ Objects.requireNonNull(res.body()).string() + "\nHeaders: "+ res.headers());
            }
            res.close();
        }
        catch (IOException e){
            logger.error(e+e.getMessage());
        }
    }
}