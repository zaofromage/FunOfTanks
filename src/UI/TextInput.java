package UI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import client.Game;
import utils.StringFormat;

public class TextInput {

	private int x, y, width, height;
	private String desc;
	private StringBuilder text;
	private int cursor;
	private Font font;
	private int textWidth;
	private int offset;

	private boolean selected;
	private boolean displayCursor;

	private final int maxCharacter;
	private final Font labelFont;
	
	private final ArrayList<Integer> validKeyCode;

	public TextInput(int x, int y, int width, int height, String desc, Font f, int max) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = f;
		this.desc = desc;
		maxCharacter = max;
		text = new StringBuilder("");
		cursor = 0;
		selected = false;
		labelFont = new Font("SansSerif", Font.PLAIN, 35);
		displayCursor = true;
		validKeyCode = new ArrayList<Integer>(Arrays.asList(new Integer[] { 523, 0, 150, 152, 222, 519}));
	}

	public void update() {

	}

	public void draw(Graphics g) {
		g.setFont(labelFont);
		offset = g.getFontMetrics().stringWidth(desc);
		g.drawString(desc, x, y + height / 2 + g.getFontMetrics().getHeight() / 4);
		g.drawRoundRect(x + offset, y, width, height, 5, 5);
		g.setFont(font);
		g.drawString(text.toString(), x + offset + 5, y + height - g.getFontMetrics().getHeight() / 4);
		textWidth = g.getFontMetrics().stringWidth(text.toString());
		if (displayCursor && selected) {
			if (text.length() > 0) {
				g.fillRect(x + g.getFontMetrics().stringWidth(text.substring(0, cursor)) + offset + 5, y + 5, 2,
						height - 8);
			} else {
				g.fillRect(x + offset + 5, y + 5, 2, height - 8);
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (selected) {
			if (e.getKeyCode() == 37) {
				if (cursor > 0)
					cursor--;
			} else if (e.getKeyCode() == 39) {
				if (cursor < text.length())
					cursor++;
			} else if (e.getKeyCode() == 8) {
				if (text.length() > 0) {
					text.deleteCharAt(cursor - 1);
					if (cursor > 0) {
						cursor--;
					}
				}
			} else {
				if (e.getKeyCode() > 32 && e.getKeyCode() < 127 || validKeyCode.contains(e.getKeyCode())) {
					if (cursor < maxCharacter && textWidth < width - font.getSize()) {
						text.insert(cursor, e.getKeyChar());
						cursor++;
					}
				}
			}
		}
	}

	public String getText() {
		return text.toString();
	}

	public String getIP() {
		return text.toString();
		//return StringFormat.isIP(text.toString()) ? text.toString() : null;
	}

	public int getPort() {
		try {
			int port = Integer.parseInt(text.toString());
			return port;
		} catch (NumberFormatException e) {
			Game.printErrorMessage("try a valid port !");
			return -1;
		}
	}

	public void onClick(MouseEvent e) {
		if (new Rectangle(x, y, width + offset, height).contains(e.getX(), e.getY())) {
			cursor = text.length();
			selected = true;
		} else {
			selected = false;
		}
	}
	
	public void setText(StringBuilder t) {
		text = t;
	}
	
	public void setSelected(boolean s) {
		selected = s;
	}
}
