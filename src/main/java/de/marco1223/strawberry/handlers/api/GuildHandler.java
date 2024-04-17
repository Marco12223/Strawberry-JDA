package de.marco1223.strawberry.handlers.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.client.ApiException;
import io.swagger.client.api.GuildManagementApi;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildHandler {

    public static JsonObject getGuild(String guildId) throws ApiException {

        GuildManagementApi guildManagementApi = new GuildManagementApi();
        AuthHandler.isTokenValid();
        Gson gson = new Gson();

        Object response = guildManagementApi.guildControllerGet(guildId);

        return gson.toJsonTree(response).getAsJsonObject();

    }

    public static JsonObject createGuild(Guild guild) throws ApiException {

        GuildManagementApi guildManagementApi = new GuildManagementApi();
        AuthHandler.isTokenValid();
        Gson gson = new Gson();
        Map<String, Object> guildData = new HashMap<>();
        guildData.put("guildId", guild.getId());
        Object response = guildManagementApi.guildControllerCreate(guildData);

        return gson.toJsonTree(response).getAsJsonObject();

    }

    public static boolean deleteGuild(String guildId) throws ApiException {

        GuildManagementApi guildManagementApi = new GuildManagementApi();
        AuthHandler.isTokenValid();
        Gson gson = new Gson();
        Object response = guildManagementApi.guildControllerDelete(guildId);
        JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();

        return jsonObject.get("statusCode").getAsInt() == 200;

    }

    public static boolean guildExists(String guildId) {

        try {
            GuildManagementApi guildManagementApi = new GuildManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = guildManagementApi.guildControllerExists(guildId);
            JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();

            return jsonObject.get("statusCode").getAsInt() == 200;
        } catch (ApiException e) {
            return false;
        }

    }

}
