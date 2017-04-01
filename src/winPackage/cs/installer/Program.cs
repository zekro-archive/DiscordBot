using System;
using System.Diagnostics;
using System.IO;
using System.Net;

namespace DCBotWinInstaller
{
    class statics
    {
        public static string updateURL = "https://github.com/zekroTJA/DiscordBot/blob/master/out/artifacts/DiscordBot_jar/DiscordBot.jar?raw=true";
        public static string settingsURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/SETTINGS.txt";
        public static string cmdURL = "https://github.com/zekroTJA/DiscordBot/blob/master/wincmd.exe?raw=true";
    }

    class Program
    {
        public static string installPath = "";

        static void Main(string[] args)
        {

            Console.WriteLine(

                "\n    ######################\n" +
                "    # ZEKROS DISCORD BOT #\n" +
                "    # INSTALLATION       #\n" +
                "    # (C) 2017 by zekro  #\n" +
                "    ######################\n"
                );


            Console.WriteLine("Please enter a path to install...");
            installPath = Console.ReadLine();
            if (!Directory.Exists(installPath))
            {
                Console.WriteLine("Creating installation path...");
                Directory.CreateDirectory(installPath);
            }

            downloadComponents();
            createStartFile();

            Process.Start(installPath + "/SETTINGS.txt");

            Console.WriteLine("\n\nINSTALLATION FINISHED!\nUse 'RUN.bat' in '" + installPath + "' to start the bot!");
            Console.Read();

        }

        static void downloadComponents()
        {
            WebClient wc = new WebClient();
            
            try {
                Console.WriteLine("Downloading 'DiscordBot.jar'...");
                wc.DownloadFile(statics.updateURL, installPath + "/DiscordBot.jar");
                Console.WriteLine("Download completed.");
            }
            catch (Exception e) { Console.WriteLine("[ERROR]" + e.Message); }

            try
            {
                Console.WriteLine("Downloading 'SETTINGS.txt'...");
                wc.DownloadFile(statics.settingsURL, installPath + "/SETTINGS.txt");
                Console.WriteLine("Download completed.");
            }
            catch (Exception e) { Console.WriteLine("[ERROR]" + e.Message); }
            
            try
            {
                Console.WriteLine("Downloading 'wincmd.exe'..."); ;
                wc.DownloadFile(statics.cmdURL, installPath + "/wincmd.exe");
                Console.WriteLine("Download completed.");
            }
            catch (Exception e) { Console.WriteLine("[ERROR]" + e.Message); }
        }

        static void createStartFile()
        {
            StreamWriter sw = new StreamWriter(installPath + "/RUN.bat");
            sw.WriteLine("@echo off");
            sw.WriteLine("title zekroBot");
            sw.WriteLine("java -jar DiscordBot.jar");
            sw.WriteLine("pause");
            sw.Close();
        }
    }
}
