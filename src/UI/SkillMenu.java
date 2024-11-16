package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import player.Player;
import player.Skill;

public class SkillMenu {
	
	private Player player;
	private HashMap<Skill, Rectangle> skills = new HashMap<>();
	public static int width = 300;
	public static int height = 500;
	private int skillSize = 100;
	private Skill selected = null;
	private Rectangle skill1;
	private Rectangle skill2;
	private Rectangle skill3;
	
	public SkillMenu(int x, int y, Player player, Skill[] ss) {
		this.player = player;
		int yWrap = skillSize*-1;
		int i = 0;
		for (Skill s : ss) {
			if (i%3 == 0) {
				yWrap += skillSize/5 + skillSize;
			}
			skills.put(s, new Rectangle(x + skillSize/5 + (skillSize/5+skillSize)*(i%3), (int) (y+skillSize*1.5)+yWrap, skillSize, skillSize));
			i++;
		}
		skill1 = new Rectangle(x+skillSize/5, y, skillSize, skillSize);
		skill2 = new Rectangle(x+(skillSize/5)*2+skillSize, y, skillSize, skillSize);
		skill3 = new Rectangle(x+(skillSize/5)*3+skillSize*2, y, skillSize, skillSize);
		Skill.loadSkills(player, ss);
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		if (player.getSkill1() != null)
			player.getSkill1().draw(g, skill1.x, skill1.y, skillSize);
		else
			g.drawRect(skill1.x, skill1.y, skillSize, skillSize);
		if (player.getSkill2() != null)
			player.getSkill2().draw(g, skill2.x, skill2.y, skillSize);
		else
			g.drawRect(skill2.x, skill2.y, skillSize, skillSize);
		if (player.getSkill3() != null)
			player.getSkill3().draw(g, skill3.x, skill3.y, skillSize);
		else
			g.drawRect(skill3.x, skill3.y, skillSize, skillSize);
		for (Map.Entry<Skill, Rectangle> e : skills.entrySet()) {
			if (selected != null && selected.equals(e.getKey())) {
				g.setColor(Color.green);
				g.fillRect(e.getValue().x-5, e.getValue().y-5, skillSize+10, skillSize+10);
			}
			e.getKey().draw(g, e.getValue().x, e.getValue().y, skillSize);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (skill1.contains(e.getX(), e.getY())) {
				player.setSkill1(selected);
			} else if (skill2.contains(e.getX(), e.getY())) {
				player.setSkill2(selected);
			} else if (skill3.contains(e.getX(), e.getY())) {
				player.setSkill3(selected);
			}
			selected = null;
			for (Map.Entry<Skill, Rectangle> s : skills.entrySet()) {
				if (s.getValue().contains(e.getX(), e.getY())) {
					selected = s.getKey();
				}
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		
	}
}
