###################################
#   USE THIS TO FOR INSTALLATION  #
#        (c) zekro 2017           #
###################################

import os
import urllib
import platform
import sys

updateURL = "https://github.com/zekroTJA/DiscordBot/blob/master/out/artifacts/DiscordBot_jar/DiscordBot.jar?raw=true"
updateScriptURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/update.py"
restartScriptURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/restart.py"
settingsURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/SETTINGS.txt"
startFileURL = "https://raw.githubusercontent.com/zekroTJA/DiscordBot/master/startfile.sh"

DEFintsallPath = "Programs/zekroBot/"


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

class messages:
    DL_FAIL = bcolors.FAIL + "An error occured while downloading! Please check your network connection!" + bcolors.ENDC
    DL_COMPLETED = bcolors.OKGREEN + "Download completed." + bcolors.ENDC


print "##############################\n" \
      "# " + bcolors.BOLD + bcolors.OKBLUE + "ZEKROS DISCORD BOT" + bcolors.ENDC + "         #\n" \
      "# INSTALLATION               #\n" \
      "# (C) 2017 by zekro          #\n" \
      "##############################\n\n"

print "Please enter a path to install. Enter nothing for default path (~/Programs/zekroBot/)..."
installPath = raw_input()
installPath += "/"

if installPath == "":
    installPath = DEFintsallPath
    print "Default path chosen."

else:
    if not os.path.exists(installPath):
        print "Entered path " + installPath + " does not exist. Create path now?"
        if raw_input("(y/n)") == "y":
            os.makedirs(installPath)
            print "Path created."
        else:
            sys.exit(0);
    else:
        print "Path " + installPath + " chosen."


if platform.system() != "Linux":
    print "\n[ERROR] Please only use that installation script on Linux based system!"
    sys.exit(0)

print "\nDownloading/Updating screen package..."
os.system("sudo apt-get install screen")

if not os.path.exists(installPath):
    print "\n[INFO] Creating path..."
    os.makedirs(installPath)

print "\nDownloading 'DiscordBot.jar'..."
try:
    urllib.urlretrieve(updateURL, installPath + "DiscordBot.jar")
    print messages.DL_COMPLETED
except:
    print messages.DL_FAIL


print "\nDownloading 'update.py'..."
try:
    urllib.urlretrieve(updateScriptURL, installPath + "update.py")
    print messages.DL_COMPLETED
except:
    print messages.DL_FAIL

print "\nDownloading 'restart.py'..."
try:
    urllib.urlretrieve(restartScriptURL, installPath + "restart.py")
    print messages.DL_COMPLETED
except:
    print messages.DL_FAIL

print "\nDownloading 'SETTINGS.txt'..."
try:
    urllib.urlretrieve(settingsURL, installPath + "SETTINGS.txt")
    print messages.DL_COMPLETED
except:
    print messages.DL_FAIL

print "\nDownloading 'zb' (Startfile)..."
try:
    urllib.urlretrieve(startFileURL, "zb")
    print messages.DL_COMPLETED
except:
    print messages.DL_FAIL

print "\n" + bcolors.OKGREEN + "Installation finished!"

print "\nDo you want to open SETTINS.txt now?"
if raw_input() == "y":
    os.system("sudo nano " + installPath + "/SETTINGS.txt")
else:
    print "\n" + bcolors.OKBLUE + "Please open the file '~/Programs/zekroBot/SETTINGS.txt' \n" \
                                  "and enter your Discord API token!"

sys.exit(0)
