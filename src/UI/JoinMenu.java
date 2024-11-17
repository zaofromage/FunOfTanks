package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import client.Game;
import gamestate.GameMode;
import player.Player;
import player.Skill;
import serverHost.Role;

public class JoinMenu extends PopUpMenu {

	private TextInput name;
	private TextInput ip;
	private TextInput port;
	
	public JoinMenu(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow, game, game.getMenu().getPlayers());
		buttons.add(new Button(x+width/2-150, 250, 300, 75, Color.cyan,
				"CREATE PLAYER", () -> {
					Player p = new Player(name.getText(), Role.GUEST, ip.getIP(), port.getPort(), game, true);
					game.setPlayer(p);
					players.add(p);
					game.getPlayer().getClient().send("newplayer;" + game.getPlayer().getName());
					buttons.get(previousLength+1).setEnabled(true);
					Skill.skills = Skill.getAllSkills(p);
					Skill.loadSkills(p, Skill.getAllSkills(p));
				}));
		buttons.add(
				new Button(x+width/2-150, 350, 300, 75, Color.red, "READY", () -> {
					game.getPlayer().setReady(!game.getPlayer().isReady());
					game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
					buttons.get(previousLength+1).setColor(game.getPlayer().isReady() ? Color.green : Color.red);
				}));
		buttons.get(previousLength+1).setEnabled(false);
		buttons.add(new Button(x+width/2-150, 500, 300, 75, Color.blue, "SWITCH TEAM", () -> {
			game.getPlayer().setTeam(game.getPlayer().getTeam() == 1 ? 2:1);
			game.getPlayer().getClient().send("team;"+game.getPlayer().getName()+";"+game.getPlayer().getTeam());
		}));
		name = new TextInput(x + 50, y + 50, 180, 30, "name ", new Font("SansSerif", Font.PLAIN, 20), 15);
		ip = new TextInput(x + 50, y + 100, 200, 30, "ip ", new Font("SansSerif", Font.PLAIN, 20), 16);
		port = new TextInput(x + 50, y + 150, 180, 30, "port ", new Font("SansSerif", Font.PLAIN, 20), 4);
		ip.setText(new StringBuilder("192.168."));
		port.setText(new StringBuilder("4551"));
		name.setSelected(true);
	}

	@Override
	public void update() {
		super.update();
		name.update();
		ip.update();
		port.update();
		buttons.get(previousLength).setEnabled(game.getPlayer() == null);
		buttons.get(previousLength+2).setEnabled(GameMode.gameMode != GameMode.FFA);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		name.draw(g);
		ip.draw(g);
		port.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		name.onClick(e);
		ip.onClick(e);
		port.onClick(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		name.keyPressed(e);
		ip.keyPressed(e);
		port.keyPressed(e);
	}
}
