package de.marco1223.strawberry.tasks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import io.swagger.client.ApiException;

import java.util.TimerTask;

public class LanguageUpdateTask extends TimerTask {

    public static Boolean isRunning = false;

    @Override
    public void run() {

        try {
            JsonObject availableLanguages = LanguageHandler.getAvailableLanguages();

            for (JsonElement locale : availableLanguages.get("locales").getAsJsonArray()) {
                JsonObject languageData = LanguageHandler.getLanguageData(locale.getAsString());
                LanguageHandler.LanguageData.add(locale.getAsString(), languageData);
            }

            isRunning = true;

        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
}
