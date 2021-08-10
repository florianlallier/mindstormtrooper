package fr.florianlallier.mindstormtrooper.action;

import java.io.File;

import lejos.hardware.Sound;

public class Music {

	/**
	 * Play a wav file. Must be mono, from 8kHz to 48kHz, and 8-bit or 16-bit.
	 * 
	 * @param file - the 8-bit or 16-bit PWM (WAV) sample file.
	 */
	public static void play(File file) {
		Sound.playSample(file);
	}
	
	/**
	 * Beeps twice for victory.
	 */
	public static void victory() {
		Sound.twoBeeps();
	}
}
