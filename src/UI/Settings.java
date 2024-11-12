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
		int i = 60;
		for (Map.Entry<String, Integer> item : PlayerInputs.getKeyBindings().entrySet()) {
			String val = null;
			if (item.getValue().equals("1")) {
				val = "Left CLick";
			} else if (item.getValue().equals("2")) {
				val = "Middle Click";
			} else if (item.getValue().equals("3")) {
				val = "Right CLick";
			} else {
				val = KeyEvent.getKeyText(item.getValue());
			}
			buttons.add(new Button(475, i, 325, 40, Color.gray, item.getKey() + " : " + val, () -> {
				if (waitForInput == null) {
					new Delay(300, () -> {
						if (waitForInput == null) {
							waitForInput = item.getKey();
							Game.printMessage("Enter an input");						
						}
					});					
				}
			}));
			i += 50;
		}
	}
	
	@Override
	public void update() {
		super.update();
		System.out.println(waitForInput);
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
				String val = null;
				if (item.getValue() == 1) {
					val = "Left CLick";
				} else if (item.getValue() == 2) {
					val = "Middle Click";
				} else if (item.getValue() == 3) {
					val = "Right CLick";
				} else {
					val = KeyEvent.getKeyText(item.getValue());
				}
				buttons.get(i).setText(item.getKey() + " : " + val);
				i++;
			}
		}
	}
}
