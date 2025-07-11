package me.drownek.staffactivity;

import com.velocitypowered.api.plugin.Plugin;

@Plugin(
	id = "staff_activity_monitor",
	name = "StaffActivityMonitor",
	description = "A simple, efficient, and highly customizable plugin for tracking staff activity.",
	version = "1.0.0",
	authors = { "Drownek" }
)
@Scan(deep = true, exclusions = "me.drownek.example.libs")
public final class StaffActivityPlugin extends LightVelocityPlugin {
	@Planned(ExecutionPhase.STARTUP)
	void startup() {
		log("Hello!");
	}
}