###################################
# PLEASE DO NOT DELETE THIS FILE! #
#       (c) zekro 2017            #
###################################

import os
import urllib
import platform
import sys
import time

updateURL = "https://github.com/zekroTJA/DiscordBot/blob/master/out/artifacts/DiscordBot_jar/DiscordBot.jar?raw=true"
fileName = "DiscordBot.jar"


time.sleep(3)

if os.path.isfile(fileName):
    os.remove(fileName)
else:
    print "File " + fileName + " does not exist!"

urllib.urlretrieve(updateURL, fileName)

if platform.system() == "Linux":
    os.system("sudo screen -L -S zekroBot sudo java -jar DiscordBot.jar -update")
else:
    os.system("java -jar DiscordBot.jar")

sys.exit(0)