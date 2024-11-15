package gamestate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import UI.*;
import client.Game;
import client.GamePanel;
import player.Player;

public class Finish implements Statemethods {
	
	private PopUpMenu menu;
	private ArrayList<Player> players;
	
	public Finish(int winner, Color c, Game game, ArrayList<Player> players) {
		game.setPlaying(null);
		this.players = players;
		menu = new FinishMenu(GamePanel.dimension.width/2-500, 50, game, c, this);
	}

	@Override
	public void update() {
		menu.update();
	}

	@Override
	public void draw(Graphics g) {
		menu.draw(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (menu != null) {
			menu.mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (menu != null) {
			menu.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

}
