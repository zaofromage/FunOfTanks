package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	
	public GameWindow(GamePanel panel) {
		setTitle("FunOfTanks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
//				PlayerInputs.loadInputs();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
//				PlayerInputs.saveInputs();
//				if (panel.getGame().getPlayer() != null) {
//					Skill.saveSkills(panel.getGame().getPlayer());
//				}
				dispose();
			}
		});
		setResizable(false);
		pack();
		setVisible(true);
	}
}
