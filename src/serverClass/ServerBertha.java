package serverClass;

import java.awt.Color;
import player.Player;

public class ServerBertha extends ServerBullet {

	public ServerBertha(int x, int y, double o, String owner, int id) {
		super(x, y, o, owner, id);
		color = Color.blue;
	}

	@Override
	public void die(Player p) {
		p.blowup(x, y, 1.);
	}
}
