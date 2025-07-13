package me.drownek.staffactivity.core.action;

import eu.okaeri.persistence.document.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Action extends Document {
    private ActionType type;
    private String message;
}
