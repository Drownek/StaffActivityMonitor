package me.drownek.staffactivity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import eu.okaeri.platform.velocity.LightVelocityPlugin;
import me.drownek.platform.core.annotation.Scan;
import me.drownek.platform.core.plan.ExecutionPhase;
import me.drownek.platform.core.plan.Planned;
import me.drownek.staffactivity.data.activity.ActivityPlayerService;
import org.bstats.velocity.Metrics;

@Plugin(
	id = "staff_activity_monitor",
	name = "StaffActivityMonitor",
	description = "A simple, efficient, and highly customizable plugin for tracking staff activity.",
	version = "1.0.0",
	authors = { "Drownek" }
)
@Scan(deep = true, exclusions = "me.drownek.staffactivity.libs")
public final class StaffActivityPlugin extends LightVelocityPlugin {

	@Inject
	private Metrics.Factory metricsFactory;

	@Planned(ExecutionPhase.POST_STARTUP)
	void postStartup() {
		metricsFactory.make(this, 27398);
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