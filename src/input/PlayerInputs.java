package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerInputs {
	
	public static int aim = MouseEvent.BUTTON1;
	public static int blocMode = MouseEvent.BUTTON3;
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
	
	public static HashMap<String, Integer> getKeyBindings() {
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		for (Field f : PlayerInputs.class.getDeclaredFields()) {
			try {
				res.put(f.getName(), (int) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return res;
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
			for (Map.Entry<String, Integer> item : PlayerInputs.getKeyBindings().entrySet()) {
				fw.write(item.getKey() + ":" + item.getValue() + ";");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
