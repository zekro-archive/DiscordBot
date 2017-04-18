package commands.music;

import audioCore.AudioInfo;
import audioCore.AudioPlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zekro on 01.04.2017 / 10:37
 * DiscordBot/commands.music
 * © zekro 2017
 */
public class Music implements Command {

    private static String clueURL = "https://youtu.be/IITW4P52gC4";

    private static final String NOTE = ":musical_note:  ";

    private static Guild guild;

    private static final int PLAYLIST_LIMIT = 500;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();

    private static final String CD = "\uD83D\uDCBF";
    private static final String DVD = "\uD83D\uDCC0";
    private static final String MIC = "\uD83C\uDFA4 **|>** ";

    private static final String QUEUE_TITLE = "__%s has added %d new track%s to the Queue:__";
    private static final String QUEUE_DESCRIPTION = "%s **|>**  %s\n%s\n%s %s\n%s";
    private static final String QUEUE_INFO = "Info about the Queue: (Size - %d)";
    private static final String ERROR = "Error while loading \"%s\"";

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

    public void loadTrack(String identifier, Member author, Message msg, MessageReceivedEvent event) {


        Guild guild = author.getGuild();
        getPlayer(guild); // Make sure this guild has a player.

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
        event.getTextChannel().sendMessage(help()).queue();
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

            if (guild.getTextChannelsByName(STATICS.musicChannel, true).size() > 0) {
                guild.getTextChannelsByName(STATICS.musicChannel, true).get(0).getManager().setTopic(
                        "NOW: " + track.getInfo().title
                ).queue();

                guild.getTextChannelsByName(STATICS.musicChannel, true).get(0).sendMessage(
                        NOTE + "**Now playing** \n" + "*[" + getTimestamp(track.getDuration()) + "]* `  " + track.getInfo().title + "  `\n"
                ).queue();
            }
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
            if (guild.getTextChannelsByName(STATICS.musicChannel, true).size() > 0) {
                guild.getTextChannelsByName(STATICS.musicChannel, true).get(0).getManager().setTopic(
                        "-music help"
                ).queue();
            }
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

        guild = event.getGuild();

        getPlayer(guild).removeListener(audioEventListener);
        getPlayer(guild).addListener(audioEventListener);

        getPlayer(guild).setVolume(STATICS.music_volume);

        switch (args.length) {
            case 0: // Show help message
                break;

            case 1:
            case 2:
                switch (args[0].toLowerCase()) {

                    case "help":
                        sendHelpMessage(event);
                        break;

                    case "now":
                    case "current":
                    case "nowplaying":
                    case "info": // Display song info
                        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
                            event.getTextChannel().sendMessage(NOTE + "No music currently playing!").queue();
                        } else {
                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            event.getTextChannel().sendMessage(NOTE + "**TRACK INFO**\n\n" + String.format(QUEUE_DESCRIPTION, CD, getOrNull(track.getInfo().title),
                                    "\n\u23F1 **|>** `[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`",
                                    "\n" + MIC, getOrNull(track.getInfo().author),
                                    "\n\uD83C\uDFA7 **|>**  " )).queue();
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
                            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));
                            List<String> tracksSublist = tracks.subList((SideNumbInput-1)*20, (SideNumbInput-1)*20+20);
                            tracksSublist.forEach(s -> sb.append(s));
                            int sideNumbAll = tracks.size() / 20;
                            int sideNumb = SideNumbInput;

                            event.getTextChannel().sendMessage(
                                    NOTE + "**QUEUE**\n\n" +
                                    "*[" + queue.size() + " Tracks | Side " + sideNumb + "/" + sideNumbAll + "]*\n\n" +
                                    sb
                            ).queue();

                        }
                        break;

                    case "skip":
                        if (isIdle(guild, event)) return;

                        if (isCurrentDj(event.getMember()) || isDj(event.getMember())) {
                            forceSkipTrack(guild);
                        } else {
                            //chat.sendMessage("You don't have permission to do that!\n"
                            //        + "Use **" + MessageUtil.stripFormatting(Info.PREFIX) + "music skip** to cast a vote!");
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

                    case "clue":
                        loadTrack(clueURL, event.getMember(), event.getMessage(), event);
                        break;
                }

            default:
                String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                switch (args[0].toLowerCase()) {
                    case "ytplay": // Query YouTube for a music video
                        input = "ytsearch: " + input;
                        // no break;

                    case "play": // Play a track
                        if (args.length <= 1) {
                            event.getTextChannel().sendMessage(":warning:  Please include a valid source.").queue();
                        } else {
                            loadTrack(input, event.getMember(), event.getMessage(), event);


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
        return
                ":musical_note:  **MUSIC PLAYER**  :musical_note: \n\n" +
                "` -music play <yt/soundcloud - URL> `  -  Start playing a track / Add a track to queue / Add a playlist to queue\n" +
                "` -music ytplay <Search string for yt> `  -  Same like *play*, just let youtube search for a track you enter\n" +
                "` -music queue <Side>`  -  Show the current music queue\n" +
                "` -music skip `  -  Skip the current track in queue\n" +
                "` -music now `  -  Show info about the now playing track\n" +
                "` -music stop `  -  Stop the music player"
        ;
    }

    @Override
    public String description() {
        return null;
    }
}
