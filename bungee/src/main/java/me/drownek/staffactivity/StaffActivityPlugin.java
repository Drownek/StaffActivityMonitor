package me.drownek.staffactivity;

import me.drownek.platform.bungee.LightBungeePlugin;
import me.drownek.platform.core.annotation.Scan;
import me.drownek.platform.core.plan.ExecutionPhase;
import me.drownek.platform.core.plan.Planned;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;

@Scan(deep = true, exclusions = "me.drownek.staffactivity.libs")
public final class StaffActivityPlugin extends LightBungeePlugin {

	@Planned(ExecutionPhase.POST_STARTUP)
	void postStartup() {
		log("Plugin loaded successfully!");
	}

	@Planned(ExecutionPhase.SHUTDOWN)
	void shutdown(
		ActivityPlayerService activityPlayerService
	) {
		activityPlayerService.completeAllActiveEntries();
		log("Plugin unloaded successfully!");
	}
}