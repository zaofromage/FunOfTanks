#!/bin/bash

jpackage \
  --type deb \
  --input ../ \
  --name funoftanks \
  --main-jar FunOfTanks.jar \
  --main-class client.Main \
  --icon ../images/funoftanks.png \
  --java-options "-Xmx512m" \
  --linux-shortcut \
  --dest dist/linux

