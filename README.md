# StaffActivityMonitor
[![Modrinth](https://img.shields.io/badge/Available_on-Modrinth-light_green)](https://modrinth.com/plugin/staffactivitymonitor/) [![SpigotMC](https://img.shields.io/badge/Available_on-SpigotMC-orange)](https://www.spigotmc.org/resources/staffactivitymonitor.126807/)

A simple, efficient, and highly customizable Bukkit plugin for tracking staff activity.

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
