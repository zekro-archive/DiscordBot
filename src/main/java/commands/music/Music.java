package commands.music;

import audioCore.AudioInfo;
import audioCore.AudioPlayerSendHandler;
import audioCore.TrackManager;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zekro on 01.04.2017 / 10:37
 * DiscordBot/commands.music
 * © zekro 2017
 */
public class Music implements Command {

    private static final int PLAYLIST_LIMIT = 200;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();

    private static final String CD = "\uD83D\uDCBF";
    private static final String DVD = "\uD83D\uDCC0";
    private static final String MIC = "\uD83C\uDFA4 **|>** ";

    private static final String QUEUE_TITLE = "__%s has added %d new track%s to the Queue:__";
    private static final String QUEUE_DESCRIPTION = "%s **|>**  %s\n%s\n%s %s\n%s";
    private static final String QUEUE_INFO = "Info about the Queue: (Size - %d)";
    private static final String ERROR = "Error while loading \"%s\"";

    private void tryToDelete(Message m) {
        if (m.getGuild().getSelfMember().hasPermission(m.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            m.delete().queue();
        }
    }

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

    private void loadTrack(String identifier, Member author, Message msg, MessageReceivedEvent event) {


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
        tryToDelete(msg);
    }

    private boolean isDj(Member member) {
        return member.getRoles().stream().anyMatch(r -> r.getName().equals("DJ"));
    }

    private boolean isCurrentDj(Member member) {
        return getTrackManager(member.getGuild()).getTrackInfo(getPlayer(member.getGuild()).getPlayingTrack()).getAuthor().equals(member);
    }

    //private boolean isIdle(MessageSender chat, Guild guild) {
    //    if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
    //        chat.sendMessage("No music is being played at the moment!");
    //        return true;
    //    }
    //    return false;
    //}

    private void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    private void sendHelpMessage(MessageReceivedEvent event) {

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



    public Music() {
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        Guild guild = event.getGuild();
        switch (args.length) {
            case 0: // Show help message
                break;

            case 1:
                switch (args[0].toLowerCase()) {
                    case "help":
                    case "commands":
                        //sendHelpMessage(chat);
                        break;

                    case "now":
                    case "current":
                    case "nowplaying":
                    case "info": // Display song info
                        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) { // No song is playing
                            //chat.sendMessage("No song is being played at the moment! *It's your time to shine..*");
                        } else {
                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            //chat.sendEmbed("Track Info", String.format(QUEUE_DESCRIPTION, CD, getOrNull(track.getInfo().title),
                            //        "\n\u23F1 **|>** `[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`",
                            //        "\n" + MIC, getOrNull(track.getInfo().author),
                            //        "\n\uD83C\uDFA7 **|>**  " + MessageUtil.userDiscrimSet(getTrackManager(guild).getTrackInfo(track).getAuthor().getUser())));
                        }
                        break;

                    case "queue":
                        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
                            //chat.sendMessage("The queue is empty! Load a song with **"
                            //        + MessageUtil.stripFormatting(Info.PREFIX) + "music play**!");
                        } else {
                            StringBuilder sb = new StringBuilder();
                            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                            queue.forEach(audioInfo -> sb.append(buildQueueMessage(audioInfo)));
                            String embedTitle = String.format(QUEUE_INFO, queue.size());

                            if (sb.length() <= 1960) {
                                //chat.sendEmbed(embedTitle, "**>** " + sb.toString());
                            } else /* if (sb.length() <= 20000) */ {
                                try {
                                    sb.setLength(sb.length() - 1);
                                    HttpResponse response = (HttpResponse) Unirest.post("https://hastebin.com/documents").body(sb.toString()).asString();
                                    //chat.sendEmbed(embedTitle, "[Click here for a detailed list](https://hastebin.com/"
                                    //        + new JSONObject(response.getBody().toString()).getString("key") + ")");
                                } catch (UnirestException ex) {
                                    ex.printStackTrace();
                                }
                                /*
                            } else {
                                e.getChannel().sendTyping().queue();
                                File qFile = new File("queue.txt");
                                try {
                                    FileUtils.write(qFile, sb.toString(), "UTF-8", false);
                                    e.getChannel().sendFile(qFile, qFile.getName(), null).queue();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                if (!qFile.delete()) { // Delete the queue file after we're done
                                    qFile.deleteOnExit();
                                }
                                */
                            }
                        }
                        break;

                    case "skip":

                        if (isCurrentDj(event.getMember())) {
                            forceSkipTrack(guild);
                        } else {
                            AudioInfo info = getTrackManager(guild).getTrackInfo(getPlayer(guild).getPlayingTrack());
                            if (info.hasVoted(event.getAuthor())) {
                                //chat.sendMessage("\u26A0 You've already voted to skip this song!");
                            } else {
                                int votes = info.getSkips();
                                if (votes >= 3) { // Skip on 4th vote
                                    getPlayer(guild).stopTrack();
                                    //chat.sendMessage("\u23E9 Skipping current track.");
                                } else {
                                    info.addSkip(event.getAuthor());
                                    tryToDelete(event.getMessage());
                                    //chat.sendMessage("**" + MessageUtil.userDiscrimSet(e.getAuthor()) + "** has voted to skip this track! [" + (votes + 1) + "/4]");
                                }
                            }
                        }
                        break;

                    case "forceskip":
                        //if (isIdle(chat, guild)) return;

                        if (isCurrentDj(event.getMember()) || isDj(event.getMember())) {
                            forceSkipTrack(guild);
                        } else {
                            //chat.sendMessage("You don't have permission to do that!\n"
                            //        + "Use **" + MessageUtil.stripFormatting(Info.PREFIX) + "music skip** to cast a vote!");
                        }
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
                        //if (isIdle(chat, guild)) return;

                        if (isDj(event.getMember())) {
                            getTrackManager(guild).shuffleQueue();
                            //chat.sendMessage("\u2705 Shuffled the queue!");
                        } else {
                            //chat.sendMessage("\u26D4 You don't have the permission to do that!");
                        }
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
                            //chat.sendMessage("Please include a valid source.");
                        } else {
                            loadTrack(input, event.getMember(), event.getMessage(), event);
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

    @Override
    public String description() {
        return null;
    }
}
