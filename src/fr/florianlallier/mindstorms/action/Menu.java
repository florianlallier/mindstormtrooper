package fr.florianlallier.mindstormtrooper.action;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import fr.florianlallier.mindstormtrooper.hardware.Motor;
import fr.florianlallier.mindstormtrooper.hardware.Sensor;

public class Menu {

	Motor motor;
	Sensor sensor;
	DrawPath drawPath;

	public Menu() {
		this.motor = new Motor();
		this.sensor = new Sensor();
		this.drawPath = new DrawPath();
	}
	
	public void close() {
		this.motor.close();
		this.sensor.close();
	}

	public void menuMain() {
		LCD.clear();
		int idButtonPressed, itemSelected, tmp;
		String[] menuItems = { "Calibration", "Detection", "FollowLine" };
		boolean[] menuItemSelected = { true, false, false };
		itemSelected = 0;
		while (true) {
			for (int i = 0; i < menuItems.length; i++) {
				if (menuItemSelected[i])
					LCD.drawString("=> " + menuItems[i], 0, i + 2);
				else
					LCD.drawString("   " + menuItems[i], 0, i + 2);
			}
			idButtonPressed = Button.waitForAnyPress();
			switch (idButtonPressed) {
			case Button.ID_DOWN:
				tmp = itemSelected;
				itemSelected = (itemSelected + 1) % 3;
				menuItemSelected[tmp] = false;
				menuItemSelected[itemSelected] = true;
				break;
			case Button.ID_UP:
				tmp = itemSelected;
				itemSelected = (itemSelected - 1) % 3;
				menuItemSelected[tmp] = false;
				menuItemSelected[itemSelected] = true;
				break;
			case Button.ID_ENTER:
				switch (itemSelected) {
				case 0:
					menuCalibrate();
					break;
				case 1:
					menuDetection();
					break;
				case 2:
					menuFollowLine();
					break;
				default:
				}
			case Button.ID_ESCAPE:
				close();
				System.exit(0);
			default:
			}
			LCD.clear();
		}
	}

	public void menuCalibrate() {
		LCD.clear();
		int idButtonPressed;
		for (int i = 0; i < 6; i++) {
			LCD.drawString("Do you wish to calibrate:", 0, 1);
			LCD.drawString(Sensor.findColorName(i) + " ?", 0, 2);
			LCD.drawString("ENTER  - Yes", 0, 3);
			LCD.drawString("DOWN   - No", 0, 4);
			LCD.drawString("ESCAPE - Back", 0, 5);
			idButtonPressed = Button.waitForAnyPress();
			switch (idButtonPressed) {
			case Button.ID_ENTER:
				Calibration.capture(sensor, i);
				// fonction ecrire dans le fichier
				break;
			case Button.ID_DOWN:
				break;
			case Button.ID_ESCAPE:
				i = (i == 0) ? i - 1 : i - 2;
				break;
			default:
				i--;
			}
			LCD.clear();
		}
		menuMain();
	}

	public void menuDetection() {
		LCD.clear();
		Detection.detection(sensor);
		menuMain();
	}

	public void menuFollowLine() {
		LCD.clear();
		boolean b = false;
		int idButtonPressed, itemSelected, colorGround, tmp;
		String[] menuItems = { "Aller Simple", "Aller-Retour" };
		boolean[] menuItemSelected = { true, false };
		itemSelected = 0;
		colorGround = 0;
		while (true) {
			for (int i = 0; i < menuItems.length; i++) {
				if (menuItemSelected[i])
					LCD.drawString("=> " + menuItems[i], 0, i + 2);
				else
					LCD.drawString("   " + menuItems[i], 0, i + 2);
			}
			idButtonPressed = Button.waitForAnyPress();
			switch (idButtonPressed) {
			case Button.ID_DOWN:
				tmp = itemSelected;
				itemSelected = (itemSelected + 1) % menuItems.length;
				menuItemSelected[tmp] = false;
				menuItemSelected[itemSelected] = true;
				break;
			case Button.ID_UP:
				tmp = itemSelected;
				itemSelected = (itemSelected - 1) % menuItems.length;
				menuItemSelected[tmp] = false;
				menuItemSelected[itemSelected] = true;
				break;
			case Button.ID_ENTER:
				while (!b) {
					LCD.clear();
					LCD.drawString("BLACK||GREEN", 2, 3);
					idButtonPressed = Button.waitForAnyPress();
					switch (idButtonPressed) {
					case Button.ID_LEFT:
						colorGround = Sensor.BLACK;
						b = true;
						break;
					case Button.ID_RIGHT:
						colorGround = Sensor.GREEN;
						b = true;
						break;
					default:
					}
				}
				LCD.clear();
				FollowLine fl = new FollowLine(motor, sensor, Sensor.WHITE, colorGround, Sensor.YELLOW, 
						Sensor.RED, 6, 4, 10, 13f);
				switch (itemSelected) {
				case 0:
					// allersimple
					fl.follow(true);
					break;
				case 1:
					// allerretour
					fl.follow(false);
					break;
				default:
				}
			case Button.ID_ESCAPE:
				menuMain();
			default:
			}
			LCD.clear();
		}
	}
	
	public static void waitToStart() {
		LCD.drawString("Press the button.", 1, 5);
		Button.ENTER.waitForPress();
		LCD.clear();
	}
}
