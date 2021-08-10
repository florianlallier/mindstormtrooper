package fr.florianlallier.mindstormtrooper.action;

import fr.florianlallier.mindstormtrooper.hardware.Sensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Detection implements Runnable {

	private int count;
	private int[] colors;
	private int colorGround;
	private int colorLine;

	public Detection(int colorGround, int colorLine) {
		this.count = 0;
		this.colors = new int[3];
		this.colorGround = colorGround;
		this.colorLine = colorLine;
	}

	public void run() {
		float[] sample = new float[3];
		int color;
		
		while (this.count < 3) {
			sensor.fetchSample(sample);
			color = sensor.distanceMin(sample);
			if (color != this.colorGround && color != this.colorLine) {
				this.colors[this.count] = color;
				this.count++;
			}
		}
	}

	public static void detection(Sensor sensor) {
		float[] sample = new float[3];
		sensor.fetchSample(sample);
		int color = sensor.distanceMin(sample);
		int lastColor = color;
		while ((Button.waitForAnyPress(1) & Button.ID_ENTER) == 0) {
			sample = new float[3];
			sensor.fetchSample(sample);
			color = sensor.distanceMin(sample);
			if (color != lastColor) { 
				LCD.clear();
				lastColor = color;
			}
			LCD.drawString(Sensor.findColorName(color), 1, 3);
		}
		Button.waitForAnyPress();
	}
}
