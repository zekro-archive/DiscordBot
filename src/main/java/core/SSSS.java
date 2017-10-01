package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.STATICS;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zekro on 17.05.2017 / 14:12
 * DiscordBot/core
 * © zekro 2017
 */

public class SSSS /* Stands for "SERVER SPECIFIC SETTINGS SYSTEM" :^) */ {

    public static void checkFolders(List<Guild> guilds) {

        guilds.forEach(guild -> {
            File f = new File("SERVER_SETTINGS/" + guild.getId());
            if (!f.exists() || !f.isDirectory()) {
                f.mkdirs();
            }
        });
    }

    public static void listSettings(MessageReceivedEvent event) {

        Guild g = event.getGuild();

        HashMap<String, String> sets = new HashMap<>();
        sets.put("PREFIX                ", getPREFIX(g));
        sets.put("SERVER_JOIN_MSG       ", getSERVERJOINMESSAGE(g));
        sets.put("SERVER_LEAVE_MSG      ", getSERVERJOINMESSAGE(g));
        sets.put("MUSIC_CHANNEL         ", getMUSICCHANNEL(g));
        sets.put("LOCK_MUSIC_CHANNEL    ", getLOCKMUSICCHANNEL(g) ? "TRUE" : "FALSE");
        sets.put("AUTOROLE              ", getAUTOROLE(g));
        sets.put("VKICK_CHANNEL         ", getVKICKCHANNEL(g));
        sets.put("RAND6_OPS_URL         ", getR6OPSID(g));
        sets.put("WARFRAMEALERTS_CHANNEL", getWARFRAMELAERTSCHAN(g));

        HashMap<String, String> setsMulti = new HashMap<>();
        setsMulti.put("PERMROLES_LVL1", String.join(", ", getPERMROLES_1(g)));
        setsMulti.put("PERMROLES_LVL2", String.join(", ", getPERMROLES_2(g)));
        setsMulti.put("BLACKLIST", String.join(", ", getBLACKLIST(g)));

        StringBuilder sb = new StringBuilder()
                .append("**GUILD SPECIFIC SETTINGS**\n\n")
                .append("```")
                .append("SETTINGS KEY            -  VALUE\n\n");

        sets.forEach((k, v) -> sb.append(
                String.format("%s  -  \"%s\"\n", k, v))
        );

        sb.append("\n- - - - -\n\n");
        setsMulti.forEach((k, v) -> sb.append(
                String.format("%s:\n\"%s\"\n\n", k, v))
        );

        event.getTextChannel().sendMessage(new EmbedBuilder().setDescription(sb.append("```").toString()).build()).queue();
    }




    public static String getPREFIX(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/prefix");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return STATICS.PREFIX;
    }

    public static void setPREFIX(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/prefix");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getSERVERJOINMESSAGE(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/serverjoinmessage");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }  catch (Exception e) {}
        return "OFF";
    }

    public static void setSERVERJOINMESSAGE(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/serverjoinmessage");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getSERVERLEAVEMESSAGE(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/serverleavemessage");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return "OFF";
    }

    public static void setSERVERLEAVEMESSAGE(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/serverleavemessage");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getMUSICCHANNEL(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/musicchannel");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return "";
    }

    public static void setMUSICCHANNEL(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/musicchannel");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String[] getPERMROLES_1(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/permroles_1");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine().split(", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return STATICS.PERMS;

    }

    public static void setPERMROLES_1(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/permroles_1");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String[] getPERMROLES_2(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/permroles_2");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine().split(", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return STATICS.FULLPERMS;
    }

    public static void setPERMROLES_2(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/permroles_2");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean getLOCKMUSICCHANNEL(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/lockmusicchannel");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine().equals("true");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return false;
    }

    public static void setLOCKMUSICCHANNEL(boolean entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/lockmusicchannel");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry + "");
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getAUTOROLE(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/autorole");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return "";
    }

    public static void setAUTOROLE(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/autorole");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getVKICKCHANNEL(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/vkickchannel");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return "";
    }

    public static void setVKICKCHANNEL(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/vkickchannel");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getR6OPSID(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/r6opsID");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return "OFF";
    }

    public static void setR6OPSID(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/r6opsID");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getWARFRAMELAERTSCHAN(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/warframealertschan");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return STATICS.warframeAlertsChannel;
    }

    public static void setWARFRAMELAERTSCHAN(String entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/warframealertschan");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            r.write(entry);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getBLACKLIST(Guild guild) {

        try {
            File f = new File("SERVER_SETTINGS/" + guild.getId() + "/blacklist");
            if (f.exists()) {
                try {
                    return new BufferedReader(new FileReader(f)).lines().map(s -> s.replace("\n", "")).collect(Collectors.toList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {}
        return new ArrayList<>();
    }

    public static void setBLACKLIST(List<String> entry, Guild guild) {

        File f = new File("SERVER_SETTINGS/" + guild.getId() + "/blacklist");
        try {
            if (!f.exists())
                f.createNewFile();
            BufferedWriter r = new BufferedWriter(new FileWriter(f));
            entry.forEach(l -> {
                try {
                    r.write(l + "\n");
                } catch (IOException e) {}
            });
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
