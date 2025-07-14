package me.drownek.staffactivity.core.action;

import eu.okaeri.persistence.document.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Action extends Document {
    private Instant time;
    private ActionType type;
    private String message;
}
