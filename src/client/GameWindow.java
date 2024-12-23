package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import input.PlayerInputs;
import player.Skill;

public class GameWindow {
	private JFrame jframe;
	
	public GameWindow(GamePanel panel) {
		jframe = new JFrame();
		jframe.setTitle("FunOfTanks");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(panel);
		jframe.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				PlayerInputs.loadInputs();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				PlayerInputs.saveInputs();
				if (panel.getGame().getPlayer() != null) {
					Skill.saveSkills(panel.getGame().getPlayer());
				}
				jframe.dispose();
			}
		});
		jframe.setResizable(false);
		jframe.pack();
		jframe.setVisible(true);
	}
}