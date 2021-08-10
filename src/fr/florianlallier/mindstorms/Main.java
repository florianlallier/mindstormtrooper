package fr.florianlallier.mindstormtrooper;

import lejos.hardware.Button;
import fr.florianlallier.mindstormtrooper.action.Menu;
import fr.florianlallier.mindstormtrooper.hardware.Motor;
import fr.florianlallier.mindstormtrooper.hardware.Sensor;

public class Main {
	
	/**
	 * ☠
	 */
	public static void exit() {
		new Thread() {
			public void run() {
				while (true) {
					if (Button.ESCAPE.isDown()) {
						System.exit(0);
					}
				}
			}
		}.start();
	}
	
	public static void main(String[] args) {
		/* Thread exit */
		exit();
		
		/* Menu */
		new Menu().menuMain();
	}
}
