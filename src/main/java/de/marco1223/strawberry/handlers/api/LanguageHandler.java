package de.marco1223.strawberry.handlers.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.swagger.client.ApiException;
import io.swagger.client.api.LanguageManagementApi;

public class LanguageHandler {

    public static final JsonObject LanguageData = new JsonObject();
    public static JsonArray availableLanguages = new JsonArray();

    public static String Language(String locale, String key) {
        try {
            return searchInJson(LanguageData.get(locale.replace("\"", "")).getAsJsonObject(), key);
        } catch (Exception e) {
            System.out.println(e);
            return key;
        }
    }

    private static String searchInJson(JsonObject jsonObject, String searchKey) {
        try {
            String[] keys = searchKey.split("\\.");
            JsonObject currentObject = jsonObject;
            for (int i = 0; i < keys.length - 1; i++) {
                if (currentObject.has(keys[i])) {
                    JsonElement nextElement = currentObject.get(keys[i]);
                    if (nextElement.isJsonObject()) {
                        currentObject = nextElement.getAsJsonObject();
                    } else if (nextElement.isJsonArray()) {
                        JsonArray jsonArray = nextElement.getAsJsonArray();
                        for (JsonElement element : jsonArray) {
                            if (element.isJsonObject()) {
                                currentObject = element.getAsJsonObject();
                                break;
                            }
                        }
                    }
                } else {
                    return searchKey;
                }
            }
            return currentObject.get(keys[keys.length - 1]).getAsString();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                return searchKey;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isLocaleAvailable(String locale) {
        for (JsonElement element : availableLanguages) {
            if (element.getAsString().equals(locale.replace("\"", ""))) {
                return true;
            }
        }
        return false;
    }

    public static JsonObject getAvailableLanguages() throws ApiException {

        LanguageManagementApi languageManagementApi = new LanguageManagementApi();
        AuthHandler.isTokenValid();
        Gson gson = new Gson();
        Object response = languageManagementApi.langControllerGetAvailableLanguages();

        return gson.toJsonTree(response).getAsJsonObject();

    }

    public static JsonObject getLanguageData(String locale) throws ApiException {

        if (isLocaleAvailable(locale)) {
            LanguageManagementApi languageManagementApi = new LanguageManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = languageManagementApi.langControllerGetLanguageData(locale);

            return gson.toJsonTree(response).getAsJsonObject();
        } else {
            return null;
        }

    }

    public static JsonObject updateGuildLanguage(String guildId, String locale) throws ApiException {

        if (isLocaleAvailable(locale)) {
            LanguageManagementApi languageManagementApi = new LanguageManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = languageManagementApi.langControllerUpdateGuildLanguage(guildId, locale);

            return gson.toJsonTree(response).getAsJsonObject();
        } else {
            return null;
        }

    }

    public static String getGuildLocale(String guildId) {
        try {
            LanguageManagementApi languageManagementApi = new LanguageManagementApi();
            AuthHandler.isTokenValid();
            Gson gson = new Gson();
            Object response = languageManagementApi.langControllerGetGuildLanguage(guildId);

            return gson.toJsonTree(response).getAsJsonObject().get("locale").getAsString();
        } catch (Exception e) {
            return "en-US";
        }
    }

}
