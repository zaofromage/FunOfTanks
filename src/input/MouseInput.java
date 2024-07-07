package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import client.GamePanel;

public class MouseInput implements MouseListener, MouseMotionListener {

	private GamePanel panel;
	public MouseInput(GamePanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("drag");

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//panel.setRectPos(e.getX(), e.getY());

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("click");

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("press");

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("release");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("enter");

	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("exit");

	}

}