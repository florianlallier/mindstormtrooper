package fr.florianlallier.mindstormtrooper.hardware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.HiTechnicColorSensor;

public class Sensor {
	
	private static final int PORT_NUMBER = 1;
	private static final int R = 0;
	private static final int G = 1;
	private static final int B = 2;
	
	public static final int NB_COLORS = 7;
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int YELLOW = 2;
	public static final int RED = 3;
	public static final int GREEN = 4;
	public static final int BLUE = 5;
	public static final int ORANGE = 6;
	
	private HiTechnicColorSensor sensor;
	private float[][] colors;
	
	public Sensor() {
		sensor = new HiTechnicColorSensor(LocalEV3.get().getPort("S" + PORT_NUMBER));
		colors = new float[NB_COLORS][3];
		read();
	}
	
	/**
	 * 
	 */
	public void read() {
		for (int i = 0; i < NB_COLORS; i++) {
			try {
				String name = findColorName(i).toLowerCase() + ".txt";
				BufferedReader file = new BufferedReader(new FileReader(name));
				for (int j = 0; j < 3; j++) {
					colors[i][j] = Integer.parseInt(file.readLine());
				}
				file.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param color
	 */
	public void write(int color) {
		try {
			String name = findColorName(color).toLowerCase() + ".txt";
			BufferedWriter file = new BufferedWriter(new FileWriter(name));
			for (int i = 0; i < 3; i++) {
				file.write(String.valueOf(colors[color][i]));
				file.newLine();
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	public float[] getColor(int color) {
		return colors[color];
	}
	
	/**
	 * 
	 * @param color
	 * @param samples
	 */
	public void setColor(int color, float[] samples) {
		colors[color][R] = samples[R];
		colors[color][G] = samples[G];
		colors[color][B] = samples[B];
	}
	
	/**
	 * 
	 */
	public void close() {
		sensor.close();
	}
	
	/**
	 * Fetches a sample from the sensor.
	 * 
	 * @param sample - The array to store the sample in.
	 */
	public void fetchSample(float[] sample) {
		sensor.getRGBMode().fetchSample(sample, 0);
	}

	/**
	 * 
	 * @param sample
	 * @return
	 */
	public int distanceMin(float[] sample) {
		int color = 0;
		float distance, distanceMin = deltaE(sample, colors[0]);
		
		for (int i = 1; i < NB_COLORS; i++) {
			if ((distance = deltaE(sample, colors[i])) < distanceMin) {
				color = i;
				distanceMin = distance;
			}
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param sampleOne
	 * @param sampleTwo
	 * @return
	 */
	public static float distance(float[] sampleOne, float[] sampleTwo) {
		float redValue = sampleTwo[R] - sampleOne[R];
		float greenValue = sampleTwo[G] - sampleOne[G];
		float blueValue = sampleTwo[B] - sampleOne[B];
		
		return (float) Math.sqrt((redValue * redValue) + (greenValue * greenValue) + (blueValue * blueValue));
	}
	
	/**
	 * 
	 * @param RGBvalues
	 * @return
	 */
	public float[] RGB2XYZ(float[] RGBvalues) { //chaque valeur RGB de 0 à 1
		float[] XYZtmp = new float[3];
		float[] XYZvalues = new float[3];
		
		for (int i = 0; i < 3; i++) {
			if (RGBvalues[i] > 0.04045) {
				XYZtmp[i] = ((float) Math.pow((RGBvalues[i] + 0.055) / 1.055, 2.4)) * 100f;
			} else {
				XYZtmp[i] = (RGBvalues[i] / 12.92f) * 100f;
			}
		}
		
		XYZvalues[R] = XYZtmp[R] * 0.4124f + XYZtmp[G] * 0.3576f + XYZtmp[B] * 0.1805f;
		XYZvalues[G] = XYZtmp[R] * 0.2126f + XYZtmp[G] * 0.7152f + XYZtmp[B] * 0.0722f;
		XYZvalues[B] = XYZtmp[R] * 0.0193f + XYZtmp[G] * 0.1192f + XYZtmp[B] * 0.9505f;
		
		return XYZvalues;	
	}
	
	/**
	 * 
	 * @param XYZvalues
	 * @return
	 */
	public float[] XYZ2Lab(float[] XYZvalues) {
		float[] XYZtmp = new float[3];
		float[] Labvalues = new float[3];
		
		XYZtmp[R] = XYZvalues[R] / 95.047f;
		XYZtmp[G] = XYZvalues[G] / 100f;	
		XYZtmp[B] = XYZvalues[B] / 108.883f;
		
		for (int i = 0; i < 3; i++) {
			if (XYZtmp[i] > 0.008856) {
				XYZtmp[i] = (float) Math.pow(XYZtmp[i], 1f / 3f);
			} else {
				XYZtmp[i] = (7.787f * XYZtmp[i]) + (16f / 116f);
			}
		}
		Labvalues[R] = (116f * XYZtmp[B]) - 116f;
		Labvalues[G] = 500 * (XYZtmp[R] - XYZtmp[G]);
		Labvalues[B] = 200 * (XYZtmp[G] - XYZtmp[B]);
		
		return Labvalues;
	}
	
	/**
	 * 
	 * @param RGBvalues
	 * @return
	 */
	public float[] RGB2Lab(float[] RGBvalues) {
		return XYZ2Lab(RGB2XYZ(RGBvalues));
	}
	
	/**
	 * 
	 * @param color1
	 * @param color2
	 * @return
	 */
	public float deltaE(float[] color1, float[] color2) { //color en RGB
		float[] c1 = RGB2Lab(color1);
		float[] c2 = RGB2Lab(color2);
		
		return (float) Math.sqrt(((c1[R] - c2[R]) * (c1[R] - c2[R])) + ((c1[G] - c2[G]) * (c1[G] - c2[G]))
				+ ((c1[B] - c2[B]) * (c1[B] - c2[B])));
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	public static String findColorName(int color) {
		switch(color) {
			case WHITE:
				return "WHITE";
			case BLACK:
				return "BLACK";
			case YELLOW:
				return "YELLOW";
			case RED:
				return "RED";
			case GREEN:
				return "GREEN";
			case BLUE:
				return "BLUE";
			case ORANGE:
				return "ORANGE"
			default:
				return null;
		}
	}
}