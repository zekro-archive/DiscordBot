package commands.essentials;

import commands.Command;
import javafx.scene.paint.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.MSGS;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zekro on 03.12.2017 / 14:15
 * DiscordBot.commands.essentials
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Id implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        TextChannel tc = event.getTextChannel();
        Guild guild = event.getGuild();

        if (args.length < 1) {
           tc.sendMessage(MSGS.error().setDescription(help()).build()).queue();
           return;
        }

        String query = String.join(" ", args).toLowerCase();

        List<Role> roles = guild.getRoles().stream().filter(r -> r.getName().toLowerCase().contains(query)).collect(Collectors.toList());
        List<TextChannel> tchans = guild.getTextChannels().stream().filter(c -> c.getName().toLowerCase().contains(query)).collect(Collectors.toList());
        List<VoiceChannel> vchans = guild.getVoiceChannels().stream().filter(c -> c.getName().toLowerCase().contains(query)).collect(Collectors.toList());
        List<Member> membs = guild.getMembers().stream().filter(m ->
                (m.getNickname() != null ? m.getNickname() : m.getEffectiveName()).toLowerCase().contains(query) || m.getEffectiveName().toLowerCase().contains(query)
        ).collect(Collectors.toList());

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(java.awt.Color.green)
                .setDescription(String.format("Found results for search query ```%s```", query))
                .addField("Roles", roles.size() == 0 ? "*No matches found*" : roles.stream().map(e -> String.format("%s  -  `%s`", e.getName(), e.getId())).collect(Collectors.joining("\n")), false)
                .addField("Voice Channels", vchans.size() == 0 ? "*No matches found*" : vchans.stream().map(e -> String.format("%s  -  `%s`", e.getName(), e.getId())).collect(Collectors.joining("\n")), false)
                .addField("Text Channels", tchans.size() == 0 ? "*No matches found*" : tchans.stream().map(e -> String.format("%s  -  `%s`", e.getName(), e.getId())).collect(Collectors.joining("\n")), false)
                .addField("Members", membs.size() == 0 ? "*No matches found*" : membs.stream().map(e -> String.format("%s  -  `%s`", e.getEffectiveName(), e.getUser().getId())).collect(Collectors.joining("\n")), false);

        tc.sendMessage(eb.build()).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "**USAGE:**\n`-id <search query>`";
    }

    @Override
    public String description() {
        return "Get the ID of server elements by name query";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.essentials;
    }

    @Override
    public int permission() {
        return 0;
    }
}
