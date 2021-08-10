package fr.florianlallier.mindstormtrooper.action;

import fr.florianlallier.mindstormtrooper.hardware.Motor;
import fr.florianlallier.mindstormtrooper.hardware.Sensor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class FollowLine {
	
	private Motor motor;
	private Sensor sensor;
	private DrawPath drawPath;
	
	private int colorLine;
	private int colorGround;
	private int colorStart;
	private int colorStop;
	
	private int plus;
	private int moins;
	private int frequency;
	private float speed;
	
	public FollowLine(Motor motor, Sensor sensor, DrawPath drawPath, int colorLine, int colorGround, 
			int colorStart, int colorStop, int plus, int moins, int frequency,
			float speed) {
		this.motor = motor;
		this.sensor = sensor;
		this.drawPath = drawPath;
		this.colorLine = colorLine;
		this.colorGround = colorGround;
		this.colorStart = colorStart;
		this.colorStop = colorStop;
		this.plus = plus;
		this.moins = moins;
		this.frequency = frequency;
		this.speed = speed;
	}
	
	/**
	 * 
	 * @param back
	 */
	public void follow(boolean back) {
		float[] sample = new float[3];
		int color;
		float distanceColorLine, distanceColorGround, tmp;
		float distanceMax = Sensor.distance(sensor.getColor(colorLine), sensor.getColor(colorGround));

		Menu.waitToStart();
		leaveStartZone(colorStart);
		motor.setSpeed(speed, speed);
		motor.forward();
		while (true) {
			do {
				sensor.fetchSample(sample);
				distanceColorLine = Sensor.distance(sample, sensor.getColor(colorLine));
				distanceColorGround = Sensor.distance(sample, sensor.getColor(colorGround));
				color = sensor.distanceMin(sample);
				motor.setSpeed(speed, speed);
				motor.forward();
				if (distanceColorLine < distanceColorGround) { // turn left
					tmp = Math.round(((distanceColorGround - distanceColorLine) / distanceMax) * 4);
					LCD.clear();
					LCD.drawString(((Float) tmp).toString(), 1, 5);
					motor.setSpeed(motor.getSpeed()[Motor.LEFT] + plus, motor.getSpeed()[Motor.RIGHT] - moins);
					motor.forward();
					drawPath.incrementPath(0);
				} else { // turn right
					tmp = Math.round(((distanceColorLine - distanceColorGround) / distanceMax) * 4);
					LCD.clear();
					LCD.drawString(((Float) tmp).toString(), 1, 5);
					motor.setSpeed(motor.getSpeed()[Motor.LEFT] - moins, motor.getSpeed()[Motor.RIGHT] + plus);
					motor.forward();
					drawPath.incrementPath(1);
				}
				Delay.msDelay(frequency);
			} while (color != colorStop);

			if (back) { // round trip
				back = false;
				int colorTmp = colorStart;
				colorStart = colorStop;
				colorStop = colorTmp;
				leaveStopZone(colorStart, colorLine);
				motor.setSpeed(speed, speed);
				motor.forward();
				continue;
			} else {
				motor.stop();
				break;
			}
		}
	}

	/**
	 * 
	 * @param sensor
	 * @param motor
	 * @param colorStart
	 */
	private void leaveStartZone(int colorStart) {
		int color;
		float[] sample = new float[3];
		motor.setSpeed(10f, 10f);
		motor.forward();
		do {
			sensor.fetchSample(sample);
			color = sensor.distanceMin(sample);
		} while (color == colorStart);
	}

	/**
	 * 
	 * @param sensor
	 * @param motor
	 * @param colorStop
	 * @param colorLine
	 */
	private void leaveStopZone(int colorStop, int colorLine) {
		int color;
		float[] sample = new float[3];
		motor.setSpeed(10f, 10f);
		do {
			sensor.fetchSample(sample);
			color = sensor.distanceMin(sample);
			motor.rotation();
			Delay.msDelay(5);
		} while (color != colorLine);
	}
}
