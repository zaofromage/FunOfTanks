package UI;

import java.lang.reflect.Field;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import client.Game;
import input.PlayerInputs;
import utils.Delay;

public class Settings extends PopUpMenu {
	
	private String waitForInput = null;

	public Settings(int x, int y, Game game) {
		super(x, y, 500, 700, Color.yellow, game);
		Field[] fields = PlayerInputs.getKeyBindings();
		int i = 60;
		for (Field item : fields) {
			String val = null;
			try {
				if ((int)item.get(null) == 1) {
					val = "Left CLick";
				} else if ((int) item.get(null) == 2) {
					val = "Middle Click";
				} else if ((int)item.get(null) == 3) {
					val = "Right CLick";
				} else {
					val = KeyEvent.getKeyText((int) item.get(null));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			buttons.add(new Button(x+width/2-200, i, 400, 40, Color.gray, item.getName() + " : " + val, () -> {
				if (waitForInput == null) {
					new Delay(300, () -> {
						if (waitForInput == null) {
							waitForInput = item.getName();						
						}
					});					
				}
			}));
			i += 50;
		}
		buttons.add(new Button(x+width+50, y+height/2-25, 100, 50, Color.RED, "reset", () -> {
			PlayerInputs.reset();
			System.out.println(fields.length);
			int j = 0;
			for (Field item : fields) {
				String val = null;
					try {
						if ((int)item.get(null) == 1) {
							val = "Left CLick";
						} else if ((int)item.get(null) == 2) {
							val = "Middle Click";
						} else if ((int)item.get(null) == 3) {
							val = "Right CLick";
						} else {
							val = KeyEvent.getKeyText((int)item.get(null));
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				buttons.get(previousLength+j).setText(item.getName() + " : " + val);
				j++;
			}
		}));
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (waitForInput != null) {
			g.drawString("Enter an input", x+width+50, y+height/2);
		}
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
			Field[] fields = PlayerInputs.getKeyBindings();
			for (int i = 0; i < fields.length; i++) {
				Field item = fields[i];
				String val = null;
				try {
					if ((int)item.get(null) == 1) {
						val = "Left CLick";
					} else if ((int) item.get(null) == 2) {
						val = "Middle Click";
					} else if ((int)item.get(null) == 3) {
						val = "Right CLick";
					} else {
						val = KeyEvent.getKeyText((int) item.get(null));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				buttons.get(previousLength+i).setText(item.getName() + " : " + val);
			}
		}
	}
}
