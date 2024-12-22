package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controller.player.PlayerController;
import input.PlayerInputs;
import model.Game;
import model.player.Dir;
import view.GameView;

public class GameController implements KeyListener, MouseListener {
	
	private Game model;
	private GameView view;
	
	private PlayerController player;
	
	public GameController(Game game, GameView view) {
		this.model = game;
		this.view = view;
		view.getPanel().addKeyListener(this);
		view.getPanel().addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == PlayerInputs.left) {
			model.getPlayer().getTank().move(Dir.LEFT);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
