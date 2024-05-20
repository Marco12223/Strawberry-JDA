package de.marco1223.strawberry.handlers.api.features;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.marco1223.strawberry.handlers.api.AuthHandler;
import io.swagger.client.ApiException;
import io.swagger.client.api.AutoroleManagementApi;

public class AutoroleHandler {

    private String guildId;

    public AutoroleHandler(String guildId) {
        this.guildId = guildId;
    }

    public JsonObject addAutorole(String roleId) {
        try {
            AutoroleManagementApi autoroleManagementApi = new AutoroleManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = autoroleManagementApi.autoroleControllerCreate(guildId, roleId);
            return gson.toJsonTree(response).getAsJsonObject();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject removeAutorole(String roleId) {
        try {
            AutoroleManagementApi autoroleManagementApi = new AutoroleManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = autoroleManagementApi.autoroleControllerDelete(guildId, roleId);
            return gson.toJsonTree(response).getAsJsonObject();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject getAutoroles() {
        try {
            AutoroleManagementApi autoroleManagementApi = new AutoroleManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = autoroleManagementApi.autoroleControllerGet(guildId);
            return gson.toJsonTree(response).getAsJsonObject();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

}
