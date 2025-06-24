#!/bin/bash

javac -d bin src/**/*.java

jar cfm FunOfTanks.jar manifest.txt -C bin . -C res .

java -jar FunOfTanks.jar
