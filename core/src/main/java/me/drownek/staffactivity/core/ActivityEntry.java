package me.drownek.staffactivity.core;

import eu.okaeri.persistence.document.Document;
import lombok.Getter;
import lombok.Setter;
import me.drownek.staffactivity.core.action.Action;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ActivityEntry extends Document {
    private Instant startTime;
    private Instant endTime;
    private Map<Long, Action> actions;

    /**
     * Creates a new ActivityEntry with the specified start time and an empty set of actions.
     *
     * @param startTime the time when the activity begins
     */
    public ActivityEntry(Instant startTime) {
        this.startTime = startTime;
        actions = new HashMap<>();
    }
}
