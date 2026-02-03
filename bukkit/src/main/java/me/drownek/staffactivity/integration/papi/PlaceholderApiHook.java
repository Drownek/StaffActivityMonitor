package me.drownek.staffactivity.integration.papi;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import me.drownek.platform.core.LightPlatform;

public class PlaceholderApiHook {

    private @Inject LightPlatform platform;

    @PostConstruct
    void setup() {
        new StaffActivityExpansion().register();
        platform.log("PlaceholderAPI hooked successfully!");
    }
}
