using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Threading;

namespace DCBotWinInstaller
{
    class Program
    {

        static string updateURL = "https://github.com/zekroTJA/DiscordBot/blob/master/out/artifacts/DiscordBot_jar/DiscordBot.jar?raw=tru";

        static void Main(string[] args)
        {

            if (args.Length == 0)
            {
                Console.WriteLine("This tool is just for restarting and updating the bot on a windows system!\nPlease dont start it in normal console!");
                Console.Read();
                Environment.Exit(0);
            }

            switch (args[0].ToString())
            {
                case "-restart":
                    restart();
                    break;

                case "-update":
                    update();
                    break;

            }
        }

        static void restart()
        {
            Process.Start("java -jar DiscordBot.jar -restart");
        }

        static void update()
        {
            Thread.Sleep(3000);
            File.Delete("DiscordBot.jar");

            WebClient wc = new WebClient();
            wc.DownloadFile(updateURL, "DiscordBot.jar");

            Process.Start("java -jar DiscordBot.jar -update");
        }
    }
}
