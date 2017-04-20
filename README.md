# Project: "zekroBot" 

Current version: ```1.19.9.0```
</br><img src="http://image.prntscr.com/image/9ba947b3f2c74c86b62ccaced5f783fe.png" />

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
### Used libraries

- <a href="https://github.com/DV8FromTheWorld/JDA">JDA</a>
- <a href="https://github.com/mwanji/toml4j">Toml4j</a>
- <a href="https://github.com/sedmelluq/lavaplayer">lavaplayer</a>
- <a href="https://github.com/koraktor/steam-condenser-java">steam-condenser</a>
