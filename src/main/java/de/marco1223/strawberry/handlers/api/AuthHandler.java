package de.marco1223.strawberry.handlers.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.AuthorizationApi;

import java.util.HashMap;
import java.util.Map;

public class AuthHandler {

    private static String username;
    private static String password;
    private static String token;
    private static ApiClient apiClient;
    private static String basePath;

    public static String getToken() {
        return token;
    }

    private static void setToken(String token) {
        AuthHandler.token = token;
    }

    public static ApiClient getApiClient() {
        return apiClient;
    }

    public static void setApiClient(ApiClient apiClient) {
        AuthHandler.apiClient = apiClient;
    }

    public static void setPassword(String password) {
        AuthHandler.password = password;
    }

    public static void setUsername(String username) {
        AuthHandler.username = username;
    }

    public static void init(String username, String password, String basePath) throws ApiException {
        AuthHandler.basePath = basePath;
        AuthHandler.setUsername(username);
        AuthHandler.setPassword(password);
        AuthHandler.setApiClient(Configuration.getDefaultApiClient());
        ApiClient apiClient = AuthHandler.getApiClient();
        apiClient.setBasePath(basePath);
        apiClient.setAccessToken(AuthHandler.createToken());
    }

    public static String createToken() throws ApiException {

        AuthorizationApi authorizationApi = new AuthorizationApi();
        Gson gson = new Gson();
        Map<String, Object> loginCredentials = new HashMap<>();
        loginCredentials.put("username", username);
        loginCredentials.put("password", password);

        Object response = authorizationApi.authControllerLogin(loginCredentials);
        JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();

        try {
            jsonObject.get("access_token").getAsString();
        } catch (NullPointerException e) {
            throw new ApiException("Access token not found in response.");
        }

        AuthHandler.setToken(jsonObject.get("access_token").getAsString());

        return jsonObject.get("access_token").getAsString();

    }

    public static boolean isTokenValid() throws ApiException {

        AuthorizationApi authorizationApi = new AuthorizationApi();
        Gson gson = new Gson();
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", AuthHandler.getToken());
        Object response = authorizationApi.authControllerVerify(tokenMap);
        JsonObject jsonObject = gson.toJsonTree(response).getAsJsonObject();

        if (!jsonObject.get("valid").getAsBoolean()) {
            apiClient.setAccessToken(AuthHandler.createToken());
        }

        return jsonObject.get("valid").getAsBoolean();

    }

}
