package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GUI {
	public static Dimension getScreenSize() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}
}
