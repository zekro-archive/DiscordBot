<img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/logo%20-%20title.png" />
<br/>

<a href="https://github.com/zekroTJA/DiscordBot/tree/master/out/artifacts/DiscordBot_jar">
<img src="https://img.shields.io/badge/Stable%20Build-1.22.8.1-green.svg"></a>
&nbsp;
<a href="https://github.com/zekroTJA/DiscordBot/tree/master/out/artifacts/DiscordBot_jar">
<img src="https://img.shields.io/badge/Test%20Build-1.23.0.2-orange.svg"></a><br>

-----

So you have some questions or want to join my developer community discord? Take a look! :^)
<br/><a href="http://discord.zekro.de"><img src="https://discordapp.com/api/guilds/307084334198816769/embed.png"/></a>

-----
### Get it!

<a href="https://discordapp.com/oauth2/authorize?client_id=272336949841362944&scope=bot&permissions=1882582134"><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/add_to_discord.png?raw=true" width="300"/></a>

-----
### Features

- **Server administration** </br> Typical commands like `-kick`, `-vkick` (voice channel kick), `-clear` and some more!

- **Fun commands** </br> Chat commands like `-vote`, `-8ball` (yes/no - generator), `-cat` (also with the feature to spam cats in a defined period of time with command `-cat spam <time>`) and some server info commands like `-userinfo` or `-stats` (Server statistics)
</br><img src="http://image.prntscr.com/image/755bdfce30de4ea8bd40d174d77a53f5.png"/>

- **Warframe alerts** </br> Create a text channel where the bot will post warframe alerts in (name of channel defined in SETTINGS.txt). You can also enter there the ID of a public google docs document where you can enter item filters for the alerts.
</br><img src="http://image.prntscr.com/image/cc2e323ef8c04123971062fcbe493024.png"/>

- **Voicelog** </br> Do you want to have a nice voice channel log like in teamspeak chat? So just create a text channel (name definable in SETTINGS.txt) and the bot will log all voice channel activity in that channel.
</br><img src="http://image.prntscr.com/image/2aef2f6f55de4aaaa806fe2fbf57988d.png"/>

- **Music** </br> Yay! Now you can also play msuic with the command `-music play <yt-url>` (or `-m play <yt-url>`)! Get more information about in in commands document or use `-music help`.

- **TTT Server Listener** </br> You can enter your Gmod Server IP and port in the settings file to auto send a message when the server is online for members. Also usable with command `-tttserver`.

-----
### Installation

If you want to install this bot for yourself, use this command on your linux system to install:
```bash
cd ~ && wget "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/install.py" && python install.py && rm install.py
```
Included in the installation, there will be a settings file in the bots direction named *SETTINGS.txt*. Open it and enter yout Discord API token, you will get from <a href="https://discordapp.com/developers/applications/me" target="_blank">this page<a/>.

```bash
sudo nano ~/Programs/zekroBot/SETTINGS.txt
```

If everything of the installation is done, start the bot with:
```bash
bash zb
```

Finally, you can add the bot to your server.
Replace "ENTERYOURIDHERE" with your id you got from the API Page.
<img src="http://image.prntscr.com/image/20c2b10a189049eeab0191452483838a.png"/>
```
https://discordapp.com/oauth2/authorize?client_id=ENTERYOURIDHERE&scope=bot
```

-----
### Commands

<a href="https://docs.google.com/spreadsheets/d/1vDsZgn49s6D1OCfyJE0aAixgbMfHb1n6ybHPG8g2Ing/edit?usp=sharing" target="_blank"><img src="https://s3.amazonaws.com/cdn.freshdesk.com/data/helpdesk/attachments/production/1033926355/original/GoogleSheets.png" width="200"/></a>

-----
### Latest Changelogs

| Released Version  | Changes |
|--|--|
| <center><b>1.23.0.2</b><br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/untested.png"/></center> | - *[BUGFIX]* in "bjoke" command |
| <center>1.23.0.1<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/untested.png"/></center> | - *[NEW]* added "-cmdlog" command<br>- *[UPDATE]* complete rework of the "-bjoke" command |
| <center>1.22.8.1<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/></center> | - *[BUGFIX]* fixed mission cmd type in settings command |
| <center>1.22.8.0<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/untested.png"/></center> | - *[ADDED]* new command 'settings' to display all settings for current guild<br>- *[UPDATED]* Information will display after startup of the bot about guilds |
| <center>1.22.7.1<img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/></center> | - *[BUG FIX]* in vkick command |
| <center>1.22.7.0<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/></center> | - *[REWORKED]* Cat command rework<br>- *[UPDATED]* Extended vkick command with timeout function<br>- *[UPDATED]* Help command update |
| <center>1.22.6.1<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/></center> | - *[REWORKED]* Complete rework of the vote command<br>- *[UPDATE]* Updated music command, added endless mode<br>- *[UPDATED]* Improved music player buffer |
| <center>1.22.5.1<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/></center> | - *[NEW]* Autorole Setting<br>- *[UPDATED]* Guild join private message on autorole reworked<br>- *[UPDATED]* Complete rework of the music help guide |
| 1.22.4.0<br><img src="https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/tested.png"/> | - *[NEW]* Server Specific Settings System <br>- *[NEW]* Settings Commands: -prefix, -permlvl, -joinmsg, -leavemsg, -botmsg <br>- *[UPDATED]* Permission Level System <br>- *[UPDATED]* User Info Command <br>- *[UPDATED]* Updated Music Command: -m channel, -m lockchannel |
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

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/un_checked.png" height="14" />
&nbsp;&nbsp; Improve update and restart commands</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; Music endless queue mode</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/un_checked.png" height="14" />
&nbsp;&nbsp; Bug report / suggestion command</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/un_checked.png" height="14" />
&nbsp;&nbsp; Twitter Tweets to textchannel function</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; Rework vote command</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; Rework cat command</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; Rework badjoke command</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; Server specific autorole system</p>

<p><img src="https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/.websrc/checked.png" height="14" />
&nbsp;&nbsp; "playshuffle" Command to directly shuffle the queue while starting it</p>

-----
### Used libraries

- <a href="https://github.com/DV8FromTheWorld/JDA">JDA</a>
- <a href="https://github.com/mwanji/toml4j">Toml4j</a>
- <a href="https://github.com/sedmelluq/lavaplayer">lavaplayer</a>
- <a href="https://github.com/koraktor/steam-condenser-java">steam-condenser</a>

-----
### Mentions
Special thanks to <a href="https://github.com/jagrosh">@jagrosh</a> for helping me with fixing the combatibility bug of lava player on linux system.

Also a special thanks to Sophie, who helped me a lot developing some features of that bot and better performance coding. <3

Another lovely thanks to all of our "Trupp Lätzl"-Discord members and team for testing the bot and using it and also a special lovley thanks to all members on my Dev Discord helping me a lot with new experiences and a lot of helpful hints. :)
