package de.marco1223.strawberry.handlers.music;

import dev.arbjerg.lavalink.client.player.Track;

import java.util.ArrayList;

import static de.marco1223.strawberry.Strawberry.queue;

public class QueueHandler {
    private final Long guildId;

    public QueueHandler(Long guildId) {
        this.guildId = guildId;
    }

    public boolean addQueue(Track track) {
        if (queue.containsKey(guildId)) {
            ArrayList<Track> currentQueue = queue.get(guildId);
            if (currentQueue.size() < 220) {
                currentQueue.add(track);
            } else {
                return false;
            }
        } else {
            ArrayList<Track> newQueue = new ArrayList<>();
            newQueue.add(track);
            queue.put(guildId, newQueue);
        }
        return true;
    }

    public void removeTrackByIndex(int index) {
        if (queue.containsKey(guildId)) {
            queue.get(guildId).remove(index);
        }
    }

    public void clearQueue() {
        if (queue.containsKey(guildId)) {
            queue.get(guildId).clear();
        }
    }

    public Track getNextTrack() {
        try {
            if (queue.containsKey(guildId)) {
                return queue.get(guildId).get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Boolean isQueueEmpty() {
        try {
            if (queue.containsKey(guildId)) {
                return queue.get(guildId).isEmpty();
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public Track getTrackInfoByIndex(int index) {
        try {
            if (queue.containsKey(guildId)) {
                return queue.get(guildId).get(index);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public ArrayList<Track> getQueue() {
        return queue.get(guildId);
    }

}
