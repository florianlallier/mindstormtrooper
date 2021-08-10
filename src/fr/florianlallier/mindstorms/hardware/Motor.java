package fr.florianlallier.mindstormtrooper.hardware;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Motor {

	private static final float WHEEL_RADIUS = 2.1f; // in centimeters
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;

	public Motor() {
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	}

	/**
	 * Close the motor regulator.
	 */
	public void close() {
		this.leftMotor.close();
		this.rightMotor.close();
	}

	/**
	 * 
	 * @return
	 */
	public float[] getSpeed() {
		float[] speed = new float[2];

		speed[LEFT] = degreeToCentimeter(leftMotor.getSpeed());
		speed[RIGHT] = degreeToCentimeter(rightMotor.getSpeed());

		return speed;
	}

	/**
	 * 
	 * @param leftSpeed
	 * @param rightSpeed
	 */
	public void setSpeed(float leftSpeed, float rightSpeed) {
		leftMotor.setSpeed(centimeterToDegree(leftSpeed));
		rightMotor.setSpeed(centimeterToDegree(rightSpeed));
	}

	/**
	 * Causes motor to rotate forward until stop() is called.
	 */
	public void forward() {
		leftMotor.forward();
		rightMotor.forward();
	}

	/**
	 * Causes motor to rotate backwards until stop() is called.
	 */
	public void backward() {
		leftMotor.backward();
		rightMotor.backward();
	}

	/**
	 * 
	 */
	public void rotation() {
		leftMotor.forward();
		rightMotor.backward();
	}

	/**
	 * Causes motor to stop, pretty much instantaneously. In other words, the
	 * motor doesn't just stop; it will resist any further motion.
	 */
	public void stop() {
		leftMotor.stop();
		rightMotor.stop();
	}

	/**
	 * 
	 * @param degree
	 * @return
	 */
	public static float degreeToCentimeter(float degree) {
		return (float) ((degree / 360) * (2 * Math.PI * WHEEL_RADIUS));
	}

	/**
	 * 
	 * @param centimeter
	 * @return
	 */
	public static float centimeterToDegree(float centimeter) {
		return (float) ((centimeter * 360) / (2 * Math.PI * WHEEL_RADIUS));
	}
}
