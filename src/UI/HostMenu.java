package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import client.Game;
import gamestate.Domination;
import gamestate.GameMode;
import gamestate.Playing;
import gamestate.TeamMode;
import model.gamestate.GameState;
import player.Player;
import player.Skill;
import serverHost.Role;
import serverHost.Server;
import utils.Calcul;

public class HostMenu extends PopUpMenu {

	private TextInput name;

	private String ip;
	
	public static Font playerFont = new Font("SansSerif", Font.BOLD, 30);
	private Font ipFont;
	

	public HostMenu(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow, game, game.getMenu().getPlayers());
		ipFont = new Font("SansSerif", Font.PLAIN, 25);
		buttons.add(new Button(x+width/2-150, y+150, 300, 75, Color.cyan,
				"CREATE PLAYER", () -> {
					if (name.getText().length() > 0) {
						Player p = new Player(name.getText(), Role.HOST, game, true);
						game.setPlayer(p);
						players.add(p);
						ip = "IP : " + getNetworkIPAddress() + " PORT : "
								+ Server.PORT;
						game.getPlayer().getClient().send("newplayer;" + game.getPlayer().getName());
						buttons.get(previousLength+1).setEnabled(true);
						buttons.get(previousLength+3).setEnabled(true);
						Skill.skills = Skill.getAllSkills(p);
						Skill.loadSkills(p, Skill.getAllSkills(p));
					} else {
						Game.printErrorMessage("Please enter a name !");
					}
				}));
		buttons.add(
				new Button(x+width/2-150, y+250, 300, 75, Color.red, "READY", () -> {
					game.getPlayer().setReady(!game.getPlayer().isReady());
					game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
					buttons.get(previousLength+1).setColor(game.getPlayer().isReady() ? Color.green : Color.red);
				}));
		buttons.get(previousLength+1).setEnabled(false);
		buttons.add(new Button(x+width/2-150, y+350, 300, 75, Color.green, "PLAY",
				() -> {
					Game.fade();
					if (game.getPlayer() != null) {
						game.getPlayer().getClient().send("play;");
						game.getPlayer().setReady(false);
						game.getPlayer().getClient().send("ready;" + game.getPlayer().getName() + ";" + game.getPlayer().isReady());
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
						game.setMenu(null);
					} else {
						Game.printErrorMessage("crée un joueur stp soit pas con");
					}
				}));
		buttons.add(new Button(x+width/2-150, y+450, 300, 75, Color.blue, "SWITCH TEAM", () -> {
			game.getPlayer().setTeam(game.getPlayer().getTeam() == 1 ? 2:1);
			game.getPlayer().getClient().send("team;"+game.getPlayer().getName()+";"+game.getPlayer().getTeam());
		}));
		buttons.get(previousLength+3).setEnabled(false);
		buttons.add(new Button(x+width/2-150, y+550, 300, 75, Color.white, "SWITCH MODE", () -> {
			GameMode.gameMode = GameMode.gameMode == GameMode.FFA ? GameMode.DOMINATION:GameMode.FFA;
			game.getPlayer().getClient().send("mode;"+GameMode.gameMode.toString());
		}));
		name = new TextInput(x + 50, y + 50, 180, 30, "name ", new Font("SansSerif", Font.PLAIN, 20), 15);
		name.setSelected(true);
	}

	@Override
	public void update() {
		super.update();
		name.update();
		buttons.get(previousLength).setEnabled(game.getPlayer() == null);
		buttons.get(previousLength+2).setEnabled(players.size() > 0 && players.stream().filter(p -> p.isReady()).count() == players.size());
		buttons.get(previousLength+3).setEnabled(GameMode.gameMode != GameMode.FFA);
		buttons.get(previousLength+4).setEnabled(game.getPlayer() != null);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		name.draw(g);
		if (ip != null) {
			g.setFont(ipFont);
			g.drawString(ip, x+width/2 - g.getFontMetrics().stringWidth(ip) / 2, 720);
		}
	}
	
	public static String getNetworkIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                
                // On ignore les interfaces inactives ou de boucle locale
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    // On vérifie que l'adresse est IPv4 et non loopback
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Adresse IP réseau non trouvée";
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		name.onClick(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		name.keyPressed(e);
	}
}
