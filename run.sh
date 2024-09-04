javac -d bin src/client/*.java src/gamestate/*.java src/input/*.java src/map/*.java src/player/*.java src/serverClass/*.java src/serverHost/*.java src/UI/*.java src/utils/*.java

jar cfm MonProjet.jar manifest.txt -C bin . -C res/images .

java -jar FunOfTanks.jar
