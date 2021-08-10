package fr.florianlallier.mindstormtrooper.action;

import fr.florianlallier.mindstormtrooper.hardware.Sensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Calibration {

	private static final int SHOTS = 5;
	private static final int R = 0;
	private static final int G = 1;
	private static final int B = 2;

	/**
	 * 
	 * @param sensor
	 * @param color
	 */
	public static void capture(Sensor sensor, int color) {
		LCD.drawString(Sensor.findColorName(color), 5, 2);
		Menu.waitToStart();
		Delay.msDelay(1000); // 1 second
		float[][] samples = new float[SHOTS][3];
		
		for (int i = 0; i < SHOTS; i++) {
			sensor.fetchSample(samples[i]);
			LCD.drawString("(" + Math.round(samples[i][R] * 100) + ", " + Math.round(samples[i][G] * 100) + ", "
					+ Math.round(samples[i][B] * 100) + ")", 3, 5);
			Delay.msDelay(1000); // 1 second
			LCD.clear();
		}
		sensor.setColor(color, average(samples));
		sensor.write(color);
	}

	/**
	 * 
	 * @param sensor
	 */
	public static void captureAll(Sensor sensor) {
		for (int i = 0; i < Sensor.NB_COLORS; i++) {
			capture(sensor, i);
		}
	}

	/**
	 * 
	 * @param samples
	 * @return
	 */
	public static float[] average(float[][] samples) {
		float[] average = new float[3];

		for (int i = 0; i < 3; i++) {
			float value = 0;
			for (int j = 0; j < SHOTS; j++) {
				value += samples[j][i];
			}
			average[i] = value / SHOTS;
		}

		return average;
	}
}
