package fr.florianlallier.mindstormtrooper.action;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DrawPath {
	
	private BufferedWriter file;
	private String path;
	
	public DrawPath() {
		this.path = "";
		try {
			this.file = new BufferedWriter(new FileWriter("path.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void close() {
		try {
			this.file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param forward
	 * @param leftSpeed
	 * @param rightSpeed
	 * @param frequency
	 */
	public void write(boolean forward, float leftSpeed, float rightSpeed, int frequency) {
		try {
			this.file.write(forward + "-" + leftSpeed + "-" + rightSpeed + "-" + frequency);
			this.file.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 0 - left
	 * 1 - right
	 * 
	 * @param dir - direction
	 */
	public void incrementPath(int dir) {
		this.path += dir;
	}
	
	public String toString() {
		return this.path;
	}
}
