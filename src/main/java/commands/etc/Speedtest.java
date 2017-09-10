package commands.etc;

import commands.Command;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zekro on 05.06.2017 / 00:10
 * DiscordBot/commands.etc
 * © zekro 2017
 */

public class Speedtest implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        SpeedTestSocket Dspeed = new SpeedTestSocket();
        SpeedTestSocket Uspeed = new SpeedTestSocket();
        StringBuilder sb = new StringBuilder();

        Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting downstream with 10MB file...").build()).complete();

        Dspeed.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                sb.append("Downstream:  " + (report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s\n");
                msg.editMessage(new EmbedBuilder().setDescription("**Speed test running...**\n\nTesting upstream with 1MB file...").build()).queue();
                Uspeed.startUpload("http://2.testdebit.info/", 1000000);
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
            }

            @Override
            public void onError(SpeedTestError speedTestError, String s) {
                System.out.println(speedTestError);
            }

        });

        Uspeed.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                sb.append("Upstream:    " + (report.getTransferRateBit().floatValue() / 1024 / 1024) + " MBit/s");
                msg.editMessage(new EmbedBuilder().setDescription("**Speed test finished.**\n\n```" + sb.toString() + "```").build()).queue();
            }

            @Override
            public void onProgress(float v, SpeedTestReport speedTestReport) {

            }

            @Override
            public void onError(SpeedTestError speedTestError, String s) {
                System.out.println(speedTestError);
            }

        });

        Dspeed.startDownload("http://2.testdebit.info/10M.iso");


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "USAGE:\n" +
                "**speed**  -  `Execute a speed test.`";
    }

    @Override
    public String description() {
        return "Tests the current connection speed of the bot.";
    }

    @Override
    public String commandType() {
        return STATICS.CMDTYPE.etc;
    }

    @Override
    public int permission() {
        return 0;
    }
}
