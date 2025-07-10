package me.drownek.staffactivity.data.activity;

import eu.okaeri.persistence.document.Document;
import lombok.Getter;
import lombok.Setter;
import me.drownek.staffactivity.data.action.Action;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ActivityEntry extends Document {
    private Instant startTime;
    private Instant endTime;
    private Map<Instant, Action> actions;

    public ActivityEntry(Instant startTime) {
        this.startTime = startTime;
        actions = new HashMap<>();
    }
}
