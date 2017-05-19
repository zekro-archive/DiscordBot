package commands.music;

import audioCore.AudioInfo;
import audioCore.AudioPlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.Command;
import core.SSSS;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * Created by zekro on 01.04.2017 / 10:37
 * DiscordBot/commands.music
 * © zekro 2017
 */

public class Music implements Command {

    private static String clueURL = "https://youtu.be/IITW4P52gC4";

    private static final String NOTE = ":musical_note:  ";

    private static Guild guild;

    private static final int PLAYLIST_LIMIT = 1000;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();

    private boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    private AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            p = createPlayer(guild);
        }
        return p;
    }

    private TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    private AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = myManager.createPlayer();
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
        players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    private void reset(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }

    public void loadTrackNext(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                queuedTracks.remove(currentTrack);
                getTrackManager(guild).purgeQueue();
                getTrackManager(guild).queue(currentTrack.getTrack(), author);
                getTrackManager(guild).queue(track, author);
                queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                    Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                    queuedTracks.remove(currentTrack);
                    getTrackManager(guild).purgeQueue();
                    getTrackManager(guild).queue(currentTrack.getTrack(), author);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    public void loadTrack(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                getTrackManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    private boolean isDj(Member member) {
        return member.getRoles().stream().anyMatch(r -> r.getName().equals("DJ"));
    }

    private boolean isCurrentDj(Member member) {
        return getTrackManager(member.getGuild()).getTrackInfo(getPlayer(member.getGuild()).getPlayingTrack()).getAuthor().equals(member);
    }

    private boolean isIdle(Guild guild, MessageReceivedEvent event) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.getTextChannel().sendMessage("No music is being played at the moment!").queue();
            return true;
        }
        return false;
    }

    private void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    private void sendHelpMessage(MessageReceivedEvent event) {
        helpOut(event);
    }

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private String getOrNull(String s) {
        return s.isEmpty() ? "N/A" : s;
    }

    private AudioEventListener audioEventListener = new AudioEventAdapter() {
        @Override
        public void onTrackStart(AudioPlayer player, AudioTrack track) {
            if (guild.getTextChannelsByName(SSSS.getMUSICCHANNEL(guild), true).size() > 0) {
                guild.getTextChannelsByName(SSSS.getMUSICCHANNEL(guild), true).get(0).getManager().setTopic(
                        track.getInfo().title
                ).queue();

                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                ArrayList<AudioInfo> tracks = new ArrayList<>();
                queue.forEach(audioInfo -> tracks.add(audioInfo));

                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(NOTE + "   **Now Playing**   ")
                        .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false)
                        .addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                guild.getTextChannelsByName(SSSS.getMUSICCHANNEL(guild), true).get(0).sendMessage(
                        eb.build()
                ).queue();
            }
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (player.getPlayingTrack() == null) {
                        if (guild.getTextChannelsByName(SSSS.getMUSICCHANNEL(guild), true).size() > 0) {
                            guild.getTextChannelsByName(SSSS.getMUSICCHANNEL(guild), true).get(0).getManager().setTopic(
                                    "-music help"
                            ).queue();
                        }
                    }
                }
            }, 500);

        }
    };

    public Music() {
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        if (SSSS.getLOCKMUSICCHANNEL(guild) && !event.getTextChannel().getName().equals(SSSS.getMUSICCHANNEL(guild))) {
            Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(":warning:  " + event.getAuthor().getAsMention() + ", please only send music commands in the #" + STATICS.musicChannel + " channel!").build()).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    msg.delete().queue();
                    event.getMessage().delete().queue();
                }
            }, 3000);
            return;
        }

        guild = event.getGuild();

        EmbedBuilder eb = new EmbedBuilder();

        getPlayer(guild).removeListener(audioEventListener);
        getPlayer(guild).addListener(audioEventListener);

        if (STATICS.music_volume > 0)
            getPlayer(guild).setVolume(STATICS.music_volume);

        switch (args.length) {
            case 0:
                sendHelpMessage(event);
                break;

            case 1:
            case 2:
                switch (args[0].toLowerCase()) {

                    case "help":
                        sendHelpMessage(event);
                        break;

                    case "channel":
                        if (core.Perms.check(2, event)) return;
                        if (args.length < 2) {
                            helpOut(event);
                            return;
                        }
                        SSSS.setMUSICCHANNEL(args[1].toLowerCase(), guild);
                        event.getTextChannel().sendMessage(MSGS.success.setDescription("Music channel successfully changed to `" + args[1].toLowerCase() + "`.").build()).queue();

                        break;

                    case "lockchannel":
                        if (core.Perms.check(2, event)) return;
                        if (args.length < 2) {
                            helpOut(event);
                            return;
                        }
                        try {
                            SSSS.setLOCKMUSICCHANNEL(Boolean.parseBoolean(args[1]), guild);
                            event.getTextChannel().sendMessage(MSGS.success.setDescription("Music channel lock successfully set to `" + Boolean.parseBoolean(args[1]) + "`.").build()).queue();
                        } catch (Exception e) {
                            helpOut(event);
                        }
                        break;

                    case "n":
                    case "now":
                    case "current":
                    case "nowplaying":
                    case "info":
                        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
                            event.getTextChannel().sendMessage(NOTE + "No music currently playing!").queue();
                        } else {
                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            AudioTrackInfo info = track.getInfo();
                            eb
                                    .setColor(Color.orange)
                                    .setDescription(":musical_note:   **Current Track Info**")
                                    .addField(":cd:  Title", info.title, false)
                                    .addField(":stopwatch:  Duration", "`[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`", false)
                                    .addField(":microphone:  Channel / Author", info.author, false);
                            event.getTextChannel().sendMessage(
                                    eb.build()
                            ).queue();
                        }
                        break;

                    case "queue":
                        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
                            event.getTextChannel().sendMessage(NOTE + "The queue ist currently empty!").queue();
                        } else {

                            int SideNumbInput = 1;
                            if (args.length > 1)
                                SideNumbInput = Integer.parseInt(args[1]);

                            StringBuilder sb = new StringBuilder();
                            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                            ArrayList<String> tracks = new ArrayList<>();
                            List<String> tracksSublist;
                            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                            if (tracks.size() > 20)
                                tracksSublist = tracks.subList((SideNumbInput-1)*20, (SideNumbInput-1)*20+20);
                            else
                                tracksSublist = tracks;

                            tracksSublist.forEach(s -> sb.append(s));
                            int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                            int sideNumb = SideNumbInput;

                            eb.setColor(Color.GREEN).setDescription(
                                    NOTE + "**QUEUE**\n\n" +
                                    "*[" + queue.size() + " Tracks | Side " + sideNumb + "/" + sideNumbAll + "]*\n\n" +
                                    sb
                            );

                            event.getTextChannel().sendMessage(
                                  eb.build()
                            ).queue();

                        }
                        break;

                    case "s":
                    case "skip":
                        if (isCurrentDj(event.getMember()) || isDj(event.getMember())) {
                            for (int skip = (args.length > 1 ? Integer.parseInt(args[1]) : 1); skip > 0; skip--) {
                                if (isIdle(guild, event)) return;
                                forceSkipTrack(guild);
                            }
                        } else {
                            event.getTextChannel().sendMessage(":warning:  Sorry, but you need to be a DJ to skip tracks!").queue();
                        }
                        break;

                    case "stop":

                        getTrackManager(guild).purgeQueue();
                        forceSkipTrack(guild);
                        guild.getAudioManager().closeAudioConnection();

                        break;

                    case "reset":
                        if (!isDj(event.getMember())) {
                            //chat.sendMessage("You don't have the required permissions to do that! [DJ role]");
                        } else {
                            reset(guild);
                            //chat.sendMessage("\uD83D\uDD04 Resetting the music player..");
                        }
                        break;

                    case "rand":
                    case "shuffle":
                        if (isIdle(guild, event)) return;

                        if (isDj(event.getMember())) {
                            getTrackManager(guild).shuffleQueue();
                            event.getTextChannel().sendMessage(NOTE + "Shuffled queue.  :twisted_rightwards_arrows: ").queue();
                        } else {
                            //chat.sendMessage("\u26D4 You don't have the permission to do that!");
                        }
                        break;

                    case "pause":
                    case "resume":
                        if (getPlayer(guild).isPaused()) {
                            getPlayer(guild).setPaused(false);
                            event.getTextChannel().sendMessage(
                                    NOTE + "Player resumed."
                            ).queue();
                        } else {
                            getPlayer(guild).setPaused(true);
                            event.getTextChannel().sendMessage(
                                    NOTE + "Player paused."
                            ).queue();
                        }
                        break;


                    case "save":

                        String input = STATICS.input;

                        if (input == null || input.length() <= 0) {
                            event.getTextChannel().sendMessage(":warning: Sorry, but no playlist is currently in queue to save!").queue();
                            return;
                        } else if (args.length < 2) {
                            event.getTextChannel().sendMessage(":warning: Please enter a valid name for your playlist!").queue();
                            return;
                        }

                        File saveFile = new File("saves_playlists/" + args[1]);

                        PrintWriter writer = new PrintWriter(saveFile);
                        writer.write(input);
                        writer.close();

                        event.getTextChannel().sendMessage(NOTE + "  Playlist \"" + args[1] + "\" successfully saved!").queue();

                        break;

                    case "listsaved":
                    case "list":
                    case "saved":

                        try {

                            File[] saves = new File("saves_playlists/").listFiles();
                            StringBuilder list = new StringBuilder();

                            if (saves.length > 0) {
                                Arrays.stream(saves).forEach(file -> list.append("- **" + file.getName() + "**\n"));
                                event.getTextChannel().sendMessage(
                                        NOTE + "   **SAVED PLAYLISTS**   " + NOTE + "\n\n" + list.toString()
                                ).queue();
                            } else {
                                event.getTextChannel().sendMessage(":warning:  Sorry, but there are no playlists saved yet!").queue();
                            }

                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(":warning:  Sorry, but there are no playlists saved yet!").queue();
                        }

                        break;


                    case "load":

                        if (args.length < 2) {
                            event.getTextChannel().sendMessage(":warning: Please enter a valid name for your playlist you want to load!").queue();
                            return;
                        }

                        try {
                            File savedFile = new File("saves_playlists/" + args[1]);
                            BufferedReader reader = new BufferedReader(new FileReader(savedFile));
                            String out = reader.readLine();

                            loadTrack(out, event.getMember(), event.getMessage());
                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                            event.getTextChannel().sendMessage(":warning:  Sorry, bit the playlist \"" + args[1] + "\" does not exist!").queue();
                        }
                        break;


                    case "clue":
                        loadTrack(clueURL, event.getMember(), event.getMessage());
                        if (getPlayer(guild).isPaused())
                            getPlayer(guild).setPaused(false);
                        break;
                }

            default:
                String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (input != null && input.contains("http"))
                    STATICS.input = input;

                switch (args[0].toLowerCase()) {

                    case "yp":
                    case "ytplay": // Query YouTube for a music video
                        input = "ytsearch: " + input;

                    case "p":
                    case "play": // Play a track
                        if (args.length <= 1) {
                            event.getTextChannel().sendMessage(":warning:  Please include a valid source.").queue();
                        } else {
                            loadTrack(input, event.getMember(), event.getMessage());

                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                    case "playshuffle":
                    case "ps":
                        if (args.length <= 1) {
                            event.getTextChannel().sendMessage(":warning:  Please include a valid source.").queue();
                        } else {
                            loadTrack(input, event.getMember(), event.getMessage());

                            getTrackManager(guild).shuffleQueue();

                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                    case "pn":
                    case "playnext":
                        if (args.length <= 1) {
                            event.getTextChannel().sendMessage(":warning:  Please include a valid source.").queue();
                        } else {
                            loadTrackNext(input, event.getMember(), event.getMessage());

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                }
                break;
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }


    public void helpOut(MessageReceivedEvent event) {

        String pre = SSSS.getPREFIX(guild);

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(0, 82, 255))
                        .setDescription(NOTE + "  **MUSIC PLAYER DESCRIPTION**\n___")

                        .addField(pre + "m play <INPUT>",
                                        "**`Short:`   " + pre + "m p <INPUT>**\n" +
                                        "`INPUT` = YouTube/SoundCloud/Twitch/Bandcamp - URL of single song or playlist\n\n" +
                                        "*If queue empty:*    Start playlist.\n" +
                                        "*If queue playing:*    Add input at the end of the queue.\n" +
                                        "___", false)

                        .addField(pre + "m ytplay <INPUT>",
                                "**`Short:`   " + pre + "m yp <INPUT>**\n" +
                                        "`INPUT` = YouTube Search string for a youtube video\n\n" +
                                        "*If queue empty:*    Start playing the first result of the YouTube search.\n" +
                                        "*If queue playing:*    Adds the first result of the YouTube search to the queue.\n" +
                                        "___", false)

                        .addField(pre + "m playshuffle <INPUT>",
                                "**`Short:`   " + pre + "m ps <INPUT>**\n" +
                                        "`INPUT` = YouTube Search string for a youtube video\n\n" +
                                        "*If queue empty:*    Like `m play`, after queueing the queue will be shuffled.\n" +
                                        "*If queue playing:*    Like `m play`, after queueing the queue will be shuffled.\n" +
                                        "___", false)

                        .addField(pre + "m playnext <INPUT>",
                                "**`Short:`   " + pre + "m pn <INPUT>**\n" +
                                        "`INPUT` = YouTube/SoundCloud/Twitch/Bandcamp - URL of single song or playlist\n\n" +
                                        "Enqueue a track or playlist after the current playing track. Queue will play following songs defaultly after enqueued track/s.\n" +
                                        "___", false)

                        .addField(pre + "m queue <OPTIONAL INPUT>",
                                        "`OPTIONAL INPUT` = Side of queue if there are more than 20 tracks queued. *Without:* First side of queue.\n\n" +
                                        "Shows the current playing queue.\n" +
                                        "___", false)

                        .addField(pre + "m skip <OPTIONAL INPUT>",
                                "**`Short:`   " + pre + "m s <OPTIONAL INPUT>**\n" +
                                        "`OPTIONAL INPUT` = Number of skips. *Without:* Only skips the now playing track.\n\n" +
                                        "Enqueue a track or playlist after the current playing track. Queue will play following songs defaultly after enqueued track/s.\n" +
                                        "___", false)

                        .addField(pre + "m shuffle",
                                "**`Short:`   " + pre + "m rand**\n\n" +
                                        "Shuffle the current queue.\n" +
                                        "___", false)

                        .addField(pre + "m now",
                                "**`Short:`   " + pre + "m n**\n\n" +
                                        "Shows information about the now playing track and displays the next track in queue.\n" +
                                        "___", false)

                        .addField(pre + "m pause  |  m resume",
                                "Pauses/resumes the current player. Bot stays in voice channel. (Both command can be used for resume and pause.)\n" +
                                        "___", false)

                        .addField(pre + "m stop",
                                "Stop the player. Queue will be reset. Bot leaves voice channel.\n" +
                                        "___", false)

                        .addField(pre + "m save <INPUT>",
                                        "`INPUT` = Name of the save.\n\n" +
                                        "Save the last added playlists/tracks URL to load it with `m load` command.\n" +
                                        "___", false)

                        .addField(pre + "m list",
                                        "Show a list of all saved playlists/tracks.\n" +
                                        "___", false)

                        .addField(pre + "m load <INPUT>",
                                        "`INPUT` = Name of the save.\n\n" +
                                        "Add a saved playlist/track to the queue or start the player with it.\n" +
                                        "___", false)

                        .addField(pre + "m channel <INPUT>",
                                        "`INPUT` = Text channel name.\n\n" +
                                        "Sets the text channel where the bot will post now playing information (also in channel description).\n" +
                                        "*Hint: Enter a not existing voice channel to disable this function.*\n" +
                                        "___", false)

                        .addField(pre + "m lockchannel <INPUT>",
                                "`INPUT` = true / false\n\n" +
                                        "Toggle if members should only be allowed to post music commands in the set music channel.\n" +
                                        "___", false)

                .build()
        ).queue();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.music;
    }
}
