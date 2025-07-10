package me.drownek.staffactivity.data.activity;

import eu.okaeri.persistence.document.Document;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("FieldMayBeFinal")
@Getter
public class ActivityPlayer extends Document {
    private List<ActivityEntry> entries = new ArrayList<>();

    public UUID getUuid() {
        return getPath().toUUID();
    }
}
