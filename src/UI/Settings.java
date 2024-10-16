package UI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import client.Game;
import input.PlayerInputs;

public class Settings extends PopUpMenu {
	
	private Integer keyCode = null;

	public Settings(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow);
		buttons.add(new Button(400, 200, 150, 50, Color.gray, "UP : " + PlayerInputs.up, () -> {
			
		}));
	}
	
	@Override
	public void update() {
		super.update();
		System.out.println(keyCode);
	}


	public void mouseClicked(MouseEvent e) {
		keyCode = e.getButton();
	}

	public void keyPressed(KeyEvent e) {
		keyCode = e.getKeyCode();
	}
}
