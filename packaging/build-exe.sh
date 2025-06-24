#!/bin/bash

jpackage \
  --type exe \
  --input ../ \
  --name FunOfTanks \
  --main-jar FunOfTanks.jar \
  --main-class game.Main \
  --icon ../images/funoftanks.ico \
  --java-options "-Xmx512m" \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut \
  --dest dist/win

