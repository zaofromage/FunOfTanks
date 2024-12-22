package UI;

import java.awt.Color;
import client.Game;
import gamestate.Domination;
import gamestate.Finish;
import gamestate.GameMode;
import gamestate.Menu;
import gamestate.Playing;
import gamestate.TeamMode;
import model.gamestate.GameState;
import player.Skill;
import serverHost.Role;

public class FinishMenu extends PopUpMenu {

	public FinishMenu(int x, int y, Game game, Color winnerColor, Finish finish) {
		super(x, y, 500, 700, Color.cyan, game, finish.getPlayers());
		buttons.add(new Button(x+width/2-150, y+150, 300, 75, Color.RED, "READY", () -> {
			game.getPlayer().setReady(!game.getPlayer().isReady());
			game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
			buttons.get(previousLength).setColor(game.getPlayer().isReady() ? Color.green : Color.red);				
		}));
		if (game.getPlayer().getRole() == Role.HOST) {
			buttons.add(new Button(x+width/2-150, y+250, 300, 75, Color.green, "PLAY", () -> {
				if (game.getPlayer() != null) {
					game.getPlayer().getClient().send("play;");
					switch (GameMode.gameMode) {
					case FFA:
						game.setPlaying(
								new Playing(game.getPanel(), game.getPlayer(), players));							
						break;
					case TEAM:
						game.setPlaying(
								new TeamMode(game.getPanel(), game.getPlayer(), players));							
						break;
					case DOMINATION:
						game.setPlaying(new Domination(game.getPanel(), game.getPlayer(), players));
						break;
					}
					Skill.saveSkills(game.getPlayer());
					GameState.state = GameState.PLAYING;
				} else {
					Game.printErrorMessage("crée un joueur stp soit pas con");
				}
			}));
		}
		if (GameMode.gameMode != GameMode.FFA) {
			buttons.add(new Button(x+width/2-150, y+350, 300, 75, Color.blue, "SWITCH TEAM", () -> {
				game.getPlayer().setTeam(game.getPlayer().getTeam() == 1 ? 2:1);
				game.getPlayer().getClient().send("team;"+game.getPlayer().getName()+";"+game.getPlayer().getTeam());
			}));			
		}
		buttons.add(new Button(x+width/2-150, y+550, 300, 75, Color.blue, "BACK TO MENU", () -> {
			if (game.getPlayer() != null) {
				if (game.getPlayer().getClient() != null) {
					game.getPlayer().getClient().send("cancel;" + game.getPlayer().getName() + ";" + game.getPlayer().getRole());
				}
				game.getPlayer().close();
				game.setPlayer(null);
			}
			game.setMenu(new Menu(game));
			GameState.state = GameState.MENU;
			game.setFinish(null);
		}));
	}
	
	@Override
	public void update() {
		super.update();
		if (game.getPlayer().getRole() == Role.HOST) {
			buttons.get(previousLength+1).setEnabled(players.size() > 0 && players.stream().filter(p -> p.isReady()).count() == players.size());
		}
	}
}
