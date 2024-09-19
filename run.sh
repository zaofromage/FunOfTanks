javac -d bin src/client/*.java src/gamestate/*.java src/input/*.java src/map/*.java src/player/*.java src/serverClass/*.java src/serverHost/*.java src/UI/*.java src/utils/*.java

jar cfm FunOfTanks.jar manifest.txt -C bin . -C res/images .

sudo java -jar FunOfTanks.jar
