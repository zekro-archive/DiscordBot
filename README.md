<img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/logo%20-%20title.png" />
<br/>

[![GitHub release](https://img.shields.io/github/release/zekrotja/DiscordBot.svg)](https://github.com/zekroTJA/DiscordBot/releases)&nbsp;
[![GitHub (pre-)release](https://img.shields.io/github/release/zekrotja/DiscordBot/all.svg)](https://github.com/zekroTJA/DiscordBot/releases)&nbsp;
<a href="https://discordapp.com/oauth2/authorize?client_id=272336949841362944&scope=bot&permissions=1882582134">
<img src="https://img.shields.io/badge/currently%20running%20on-1.36.2.1-3cd0d8.svg"></a><br>

-----

If you wnat to use the code of this project for your own ones, please read **[this](http://s.zekro.de/codepolicy)** before!

So you have some questions or want to join my developer community discord? Take a look! :^)
<br/><a href="http://discord.zekro.de"><img src="https://discordapp.com/api/guilds/307084334198816769/embed.png"/></a>

-----
### Get it!
<img src="https://img.shields.io/badge/Server%20Capacity-27%2F30-3cc482.svg"><br>
<a href="https://discordapp.com/oauth2/authorize?client_id=272336949841362944&scope=bot&permissions=1882582134"><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/add_to_discord.png?raw=true" width="300"/></a>

> INFO: Because of my low end vServer the bot is running on, there is currently a limited server capacity depending on current demand.

-----
### Features

```
Here are just listed some of the various functions and 57 commands of the bot.
```
**Here you can find a full list of commands:**<br><br>
<a href="https://docs.google.com/spreadsheets/d/1vDsZgn49s6D1OCfyJE0aAixgbMfHb1n6ybHPG8g2Ing/edit?usp=sharing" target="_blank"><img src="https://s3.amazonaws.com/cdn.freshdesk.com/data/helpdesk/attachments/production/1033926355/original/GoogleSheets.png" width="150"/></a><br><br>

- **Fun / Chat commands**
  - `-8ball` - typical 8ball yes/no generator
  - `-cat` - send cute cat pictures (also with a spam function to send them in time periods)
  - `-clear` - typical clear command to clear messages
  - `-joke` - throw a yomama joke :^)
  - `-quote` - quote messages from channels on the guild<br>
  &nbsp;![](https://image.prntscr.com/image/g3-ctAYBSlu1eS9qoFTSSQ.png)
  - `-stups` - nudge someone on the guild
  - `-vote` - create polls<br>
  &nbsp;![](https://image.prntscr.com/image/5_avzZNQRUijY2rUgc1XgQ.png)
  
  
- **Server administration**
  - `-kick` - kick someone from the server
  - `-vkick` - kick someone out of the voice channel, also for a specific time period
  - `-mute` - mute members in text channels
  - `-blacklist` - disallow users to use the bot
  
- **Other functions**
  - `-music` - Music player with many functions
  - `-scpacer` - Create spacer voice channels wich can not be joined
  - `-rand6` - Random operators for rainbow six<br>
  &nbsp;![](https://image.prntscr.com/image/WHZh5l76TKupvWUmoIQBpA.png)

-----
### Installation

Just download the latest version from **[Releases](https://github.com/zekroTJA/DiscordBot/releases)** and save it somewhere on your pc or server.

First start the JAR file with:
```bash
java -jar DiscordBot.jar
```
The bot will create a `SETTINGS.txt` file. Open it and enter your settings.
The most important is to set first the API token you'll get from [this page](https://discordapp.com/developers/applications/me) *(if you just created an bot account there).* Also its realy important to set yout Discord account ID as `BOT_OWNER_ID`! You can get your client id by right-clicking on your name in discord and select `Copy ID` *(for that, you need to enable developer mode in Discord!)*
![img](https://image.prntscr.com/image/Jmf2FssPSdKEb9jNOTra-g.png)
![img](https://image.prntscr.com/image/UrxT_eI7SbqmZIcbQs1QvQ.png)

Then restart the bot with
```bash
java -jar DiscordBot.jar
```

If you are running the bot on a Linux server via SSH, use **[screen](https://wiki.ubuntuusers.de/Screen/)** to run the bot as background process:
```bash
$ sudo screen -L -S zekroBot sudo java -jar DiscordBot.jar
```
*(`-L` generates a logfile `screenlog.0` and `-S zekroBot` set a name to the screen so you can reopen the screen with `sudo screen -r zekroBot`)*

You also can create a bash file like this to start (and restart) the bot:
```bash
# resume running screen (if there is a screen running) to stop it with [STRG] + [C]
sudo screen -r zekroBot
# cd to bot JAR location (enter YOUR path there)
cd Programs/zekroBot
# start the bot in screen
sudo screen -L -S zekroBot sudo java -jar DiscordBot.jar
```

After that, use the guild settings commands to configure the bot for your guild(s)
![img](https://image.prntscr.com/image/VKw6mpxPS8in40ZB4sTOMQ.png)

-----
### Latest Changelogs

**THIS LIST WILL NOT BE UPDATED ANYMORE! PLEASE LOOK IN <a href="https://github.com/zekroTJA/DiscordBot/releases">RELEASES</a> TO SEE LATEST CHANGE LOGS!**

| Released Version  | Changes |
|--|--|
| <center><b>1.28.0.0</b><br><img src="https://img.shields.io/badge/build-testing%20phase-3cd0d8.svg"/></center> | - *[NEW]* added command "-report" |
| <center>1.28.0.0<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[NEW]* added command "-broadcast"<br>- *[NEW]* added command "-guilds" |
| <center>1.27.1.0<br><img src="https://img.shields.io/badge/build-test%20in%20progress-orange.svg"/></center> | - *[NEW]* added log command<br>- *[UPDATE]* added bot onwer check to perms core<br>- *[UPDATE]* changed perms of bot cotrolling command to bot owner only |
| <center>1.26.0.0<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[NEW]* added "mute" command<br>- *[UPDATE]* optimized reroll function and added rule function to "rand6" command |
| <center>1.25.1.0<br><img src="https://img.shields.io/badge/build-unstable-orange.svg"/></center> | - *[NEW]* - added reroll function for "rand6" command<br>- *[NEW]* - added clear by timestamp and clear all function to "clear" command |
| <center>1.25.0.2<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[BUGFIX]* Hotfixed bug in "rand6" command |
| <center>1.25.0.0<br><img src="https://img.shields.io/badge/build-unstable-orange.svg"/></center> | - *[NEW]* added "rand6" command |
| <center>1.24.0.0<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[NEW]* added "quote" command<br>- *[NEW]* added "speed" / "speedtest" command |
| <center>1.23.0.2<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[BUGFIX]* in "bjoke" command |
| <center>1.23.0.1<br><img src="https://img.shields.io/badge/build-unstable-orange.svg"/></center> | - *[NEW]* added "-cmdlog" command<br>- *[UPDATE]* complete rework of the "-bjoke" command |
| <center>1.22.8.1<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[BUGFIX]* fixed missing cmd type in settings command |
| <center>1.22.8.0<br><img src="https://img.shields.io/badge/build-unstable-orange.svg"/></center> | - *[ADDED]* new command 'settings' to display all settings for current guild<br>- *[UPDATED]* Information will display after startup of the bot about guilds |
| <center>1.22.7.1<img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[BUG FIX]* in vkick command |
| <center>1.22.7.0<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[REWORKED]* Cat command rework<br>- *[UPDATED]* Extended vkick command with timeout function<br>- *[UPDATED]* Help command update |
| <center>1.22.6.1<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[REWORKED]* Complete rework of the vote command<br>- *[UPDATE]* Updated music command, added endless mode<br>- *[UPDATED]* Improved music player buffer |
| <center>1.22.5.1<br><img src="https://img.shields.io/badge/build-stable-green.svg"/></center> | - *[NEW]* Autorole Setting<br>- *[UPDATED]* Guild join private message on autorole reworked<br>- *[UPDATED]* Complete rework of the music help guide |
| 1.22.4.0<br><img src="https://img.shields.io/badge/build-stable-green.svg"/> | - *[NEW]* Server Specific Settings System <br>- *[NEW]* Settings Commands: -prefix, -permlvl, -joinmsg, -leavemsg, -botmsg <br>- *[UPDATED]* Permission Level System <br>- *[UPDATED]* User Info Command <br>- *[UPDATED]* Updated Music Command: -m channel, -m lockchannel |
| 1.21.0.0 | - *[IMPROVEMENT]* Improved help command<br>- *[BUGFIX]* automatic unpause after queueing music<br>- *[NEW]* added uptime command |
| 1.20.2.0 | Improved move all command |
| 1.20.1.0 | Better messages, overworked user info command, overworked clear command, added Moveall command |
| 1.19.18.0 | Overworked Info Command |
| 1.19.17.0 | Overworked some more messages, especially in music command |
| 1.19.15.0 | Overworked permission system, added command for my private dev server :^) |
| 1.19.14.2 | Improved some message designs, overworked server stats command |
| 1.19.12.0 | Better compatibility to linux systems, added volume setting in config file |
| 1.19.11.4 | Added playlist saving/loading to music command |
| 1.19.10.0 | Improved some music command methods, fixed some now playing bugs, added setting to allow users only to post music commands in music channel |
| 1.19.9.0 | Added multiple skips and "queuenext" command to audio player |
| 1.19.8.0 | Essential performance improvements |
| 1.19.7.1 | Updated some things in music player, bugfixes |
| 1.19.6.0 | Added now playing functions to music player |
| 1.19.5.0 | Added Pasue/Resume function to music bot |
| 1.19.4.2 | Bugfixes in music player, optimizations stuff |
| 1.19.4.0 | Updated music command so it's now usable :^) |
| 1.19.0.0 | Added music player, updated settings so now you can disable auto update |
| 1.18.0.4 | Added command -tttserver, added TTT Server online status listener |
| 1.17.0.0 | Added vkick command |
| 1.16.0.0 | Added kick command, changed some things in settings handling |
| 1.15.0.1 | Added restart command, updated code structure |
| 1.14.2.1 | Updated updating service, changed some stuff in API token handling |
| 1.14.1.0 | Added installation script, new admin command "-stop" |
| 1.14.0.0 | Updated JDA, added auto update service |

-----
### Future Intends

**SEE CURRENT TODO-LIST <a href="https://github.com/zekroTJA/DiscordBot/projects/1">HERE</a>**

-----
### Used libraries

- <a href="https://github.com/DV8FromTheWorld/JDA">JDA</a>
- <a href="https://github.com/mwanji/toml4j">Toml4j</a>
- <a href="https://github.com/sedmelluq/lavaplayer">lavaplayer</a>
- <a href="https://github.com/koraktor/steam-condenser-java">steam-condenser</a>
- <a href="https://github.com/bertrandmartel/speed-test-lib">JSpeedTest</a>

-----
### Mentions
Special thanks to <a href="https://github.com/jagrosh">@jagrosh</a> for helping me with fixing the combatibility bug of lava player on linux system.

Also a special thanks to Sophie, who helped me a lot developing some features of that bot and better performance coding. <3

Another lovely thanks to all of our "Trupp Lätzl"-Discord members and team for testing the bot and using it and also a special lovley thanks to all members on my Dev Discord helping me a lot with new experiences and a lot of helpful hints. :)
