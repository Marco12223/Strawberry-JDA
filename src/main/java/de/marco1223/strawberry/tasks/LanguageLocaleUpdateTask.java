package de.marco1223.strawberry.tasks;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import io.swagger.client.ApiException;

import java.util.TimerTask;

public class LanguageLocaleUpdateTask extends TimerTask {
    @Override
    public void run() {
        try {
            LanguageHandler.availableLanguages = LanguageHandler.getAvailableLanguages().get("locales").getAsJsonArray();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
}
