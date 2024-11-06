package UI;

import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

import client.Game;
import input.PlayerInputs;
import utils.Delay;

public class Settings extends PopUpMenu {
	
	private String waitForInput = null;

	public Settings(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow);
		int i = 200;
		for (Map.Entry<String, Integer> item : PlayerInputs.getKeyBindings().entrySet()) {
			buttons.add(new Button(500, i, 250, 50, Color.gray, item.getKey() + " : " + item.getValue(), () -> {
				new Delay(100, () -> waitForInput = item.getKey() );
			}));
			i += 50;
		}
	}
	
	@Override
	public void update() {
		super.update();
	}

	public void mouseClicked(MouseEvent e) {
		changeInput(e.getButton());
	}

	public void keyPressed(KeyEvent e) {
		changeInput(e.getKeyCode());
	}
	
	private void changeInput(int input) {
		if (waitForInput != null) {
			try {
				Field f = PlayerInputs.class.getDeclaredField(waitForInput);
				f.setAccessible(true);
				f.set(null, input);
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			waitForInput = null;
			int i = 0;
			for (Map.Entry<String, Integer> item : PlayerInputs.getKeyBindings().entrySet()) {
				buttons.get(i).setText(item.getKey() + " : " + item.getValue());;
				i++;
			}
		}
	}
}
