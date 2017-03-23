###################################
#   USE THIS TO FOR INSTALLATION  #
#        (c) zekro 2017           #
###################################

import os
import urllib
import platform
import sys

updateURL = "https://github.com/zekroTJA/DiscordBot/blob/master/out/artifacts/DiscordBot_jar/DiscordBot.jar?raw=true"
updateScriptURL = "https://github.com/zekroTJA/DiscordBot/blob/master/update.py"
startFileURL = "https://github.com/zekroTJA/DiscordBot/blob/master/startfile.sh"

intsallPath = "Programs/zekroBot/"

if platform.system() != "Linux":
    print "\n[ERROR] Please only use that installation script on Linux based system!"
    sys.exit(0)

print "\n[INFO] Downloading/Updating screen package..."
os.system("sudo apt-get install screen")

if not os.path.exists(intsallPath):
    print "\n[INFO] Creating path..."
    os.makedirs(intsallPath)

print "\n[INFO] Downloading 'DiscordBot.jar'..."
urllib.urlretrieve(updateURL, intsallPath + "DiscordBot.jar")

print "\n[INFO] Downloading 'update.py'..."
urllib.urlretrieve(updateScriptURL, intsallPath + "update.py")

print "\n[INFO] Downloading 'zb' (Startfile)..."
urllib.urlretrieve(startFileURL, "update.py")

print "\n[INFO] Installation finished!"

sys.exit(0)