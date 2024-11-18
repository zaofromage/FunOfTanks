package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

public class PlayerInputs {
	
	public static int aim = MouseEvent.BUTTON1;
	public static int blocMode = MouseEvent.BUTTON3;
	public static int grabMode = MouseEvent.BUTTON2;
	public static int build = MouseEvent.BUTTON1;
	public static int up = KeyEvent.VK_Z;
	public static int down = KeyEvent.VK_S;
	public static int right = KeyEvent.VK_D;
	public static int left = KeyEvent.VK_Q;
	public static int skill1 = KeyEvent.VK_E;
	public static int skill2 = KeyEvent.VK_R;
	public static int skill3 = KeyEvent.VK_A;
	public static int dash = KeyEvent.VK_SPACE;
	public static int escape = KeyEvent.VK_ESCAPE;
	public static int leaderBoard = KeyEvent.VK_TAB;
	
	public static Field[] getKeyBindings() {
        return PlayerInputs.class.getDeclaredFields();
	}
	
	public static void reset() {
		aim = MouseEvent.BUTTON1;
		blocMode = MouseEvent.BUTTON3;
		grabMode = MouseEvent.BUTTON2;
		build = MouseEvent.BUTTON1;	
		up = KeyEvent.VK_Z;
		down = KeyEvent.VK_S;
		right = KeyEvent.VK_D;
		left = KeyEvent.VK_Q;
		skill1 = KeyEvent.VK_E;
		skill2 = KeyEvent.VK_R;
		skill3 = KeyEvent.VK_A;
		dash = KeyEvent.VK_SPACE;
		escape = KeyEvent.VK_ESCAPE;	
		leaderBoard = KeyEvent.VK_TAB;
	}
	
	public static void loadInputs() {
		try {
			File f = new File("res/inputs");
			if (f.exists()) {
				FileReader fr = new FileReader(f);
				BufferedReader r = new BufferedReader(fr);
				String line = r.readLine();
				String[] items = line.split(";");
				for (String s : items) {
					String key = s.split(":")[0];
					int val = Integer.parseInt(s.split(":")[1]);
					Field field = PlayerInputs.class.getDeclaredField(key);
					field.setAccessible(true);
					field.set(null, val);
				}
				r.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveInputs() {
		try {
			File f = new File("res/inputs");
			if (f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f);
			for (Field item : PlayerInputs.getKeyBindings()) {
				fw.write(item.getName() + ":" + item.get(null) + ";");
			}
			fw.close();
		} catch (IOException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
