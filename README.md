# StaffActivityMonitor

A simple, efficient, and highly customizable Bukkit plugin for tracking staff activity.

[![Modrinth](https://img.shields.io/badge/Available_on-Modrinth-light_green)](https://modrinth.com/plugin/staffactivitymonitor/) [![SpigotMC](https://img.shields.io/badge/Available_on-SpigotMC-orange)](https://www.spigotmc.org/resources/staffactivitymonitor.126807/)

![](https://i.imgur.com/VtW3F26.png)

## Usage
All players with permission `staffactivity.staff` are tracked by the plugin.
That includes messages/commands sent and times at which the player was online.

## Commands

`/staffactivity top` - Opens the activity top GUI showing staff activity rankings

`/staffactivity view` - Opens the activity list GUI showing all staff activities

`/staffactivity view <player>` - Opens the activity view for a specific player

`/staffactivity reload` - Reloads the plugin configuration files

## Permissions

`staffactivity.staff` - Required permission for the plugin to track staff members

`staffactivity.commands` - Required permission to use all plugin commands

## PlaceholderAPI

This plugin supports PlaceholderAPI with a wide range of placeholders for displaying staff activity data. All placeholders require the player to have the `staffactivity.staff` permission and be tracked by the plugin. If the player is not tracked, the configured default value will be displayed.

### Time Placeholders
- `%staffactivity_total_time%` - Total time online across all servers
- `%staffactivity_daily_time%` - Time online today
- `%staffactivity_weekly_time%` - Time online in the last 7 days
- `%staffactivity_monthly_time%` - Time online in the last 30 days
- `%staffactivity_yearly_time%` - Time online this month
- `%staffactivity_average_session%` - Average session duration

### Session Count Placeholders
- `%staffactivity_session_count%` - Total number of sessions
- `%staffactivity_daily_session_count%` - Sessions today
- `%staffactivity_weekly_session_count%` - Sessions in the last 7 days
- `%staffactivity_monthly_session_count%` - Sessions in the last 30 days

### Commands Count Placeholders
- `%staffactivity_commands_count%` - Total commands executed
- `%staffactivity_daily_commands_count%` - Commands executed today
- `%staffactivity_weekly_commands_count%` - Commands executed in the last 7 days
- `%staffactivity_monthly_commands_count%` - Commands executed in the last 30 days

### Messages Count Placeholders
- `%staffactivity_messages_count%` - Total messages sent
- `%staffactivity_daily_messages_count%` - Messages sent today
- `%staffactivity_weekly_messages_count%` - Messages sent in the last 7 days
- `%staffactivity_monthly_messages_count%` - Messages sent in the last 30 days

### Ranking Placeholders
- `%staffactivity_rank%` - Player's rank in all-time activity leaderboard
- `%staffactivity_daily_rank%` - Player's rank in daily activity leaderboard
- `%staffactivity_weekly_rank%` - Player's rank in weekly activity leaderboard
- `%staffactivity_monthly_rank%` - Player's rank in monthly activity leaderboard

### Other Placeholders
- `%staffactivity_last_seen%` - Last activity timestamp

### Configuration
You can customize placeholder behavior in the config:
- `placeholders.timeFormat` - Format for time placeholders (default: `{days}d {hours}h {minutes}m`)
- `placeholders.lastSeenFormat` - Java DateTimeFormatter pattern for last_seen (default: `yyyy-MM-dd HH:mm:ss`)
- `placeholders.notTrackedValue` - Value shown for non-tracked players (default: `N/A`)

## Installation for proxy servers
* Install a Bukkit version on all non-proxy servers you wish to see staff data
  * enable proxyMode in config of each bukkit subservers, and set up the database as MYSQL or Postgres
* Install a Bungeecord/Velocity version on a proxy server
  * configure it to use the same database as non-proxy servers
* Now, proxy server will track staff data, and non-proxy servers will be able to show it through gui

## Libraries used

- [light-platform](https://github.com/Drownek/light-platform)
- [bukkit-utils](https://github.com/Drownek/bukkit-utils)
- [LiteCommands](https://github.com/Rollczi/LiteCommands)
- [TriumphGui](https://github.com/TriumphTeam/triumph-gui)

## Supported versions
All servers must run under Java 17+ in order for the plugin to work.

Plugin was tested under the following versions:
- 1.17.1
- 1.18.2
- 1.19.4
- 1.20.1
- 1.21.5
- 1.21.8

If you have some issues, feel free to reach me on discord: `drownek` or create an issue in GitHub repository!

## 📜 License

Project is licensed under [MIT](https://choosealicense.com/licenses/mit/).

This means that...

- ✅ You can freely use, copy, modify, and distribute this project, even for commercial purposes.
- 🧾 You **must include the original license and copyright notice** in any copies or substantial portions.
- ❌ The software is provided **"as is"**, without warranty of any kind. The author is **not liable** for any damages or issues caused by using it.
