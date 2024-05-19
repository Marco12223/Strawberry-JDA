package de.marco1223.strawberry.api;

import com.google.gson.Gson;
import de.marco1223.strawberry.Strawberry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class Status {

    @GetMapping("/status")
    public String status() {

        Gson gson = new Gson();
        HashMap<String, String> status = new HashMap<>();
        status.put("guilds", String.valueOf(Strawberry.getShardManager().getGuilds().size()));
        status.put("users", String.valueOf(Strawberry.getShardManager().getUsers().size()));
        status.put("shards", String.valueOf(Strawberry.getShardManager().getShards().size()));

        return gson.toJson(status);

    }

}
