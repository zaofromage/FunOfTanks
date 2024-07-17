package utils;

import client.Game;

public class StringFormat {
	
	public static boolean isIP(String str) {
		String[] bytes = str.split(".");
		if (bytes.length != 4) return false;
		for (String b : bytes) {
			try {
				Integer.parseInt(b);
			} catch (NumberFormatException e) {
				Game.printErrorMessage("Try a valid IP address !");
				return false;
			}
		}
		return true;
	}
}
