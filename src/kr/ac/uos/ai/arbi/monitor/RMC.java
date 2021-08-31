package kr.ac.uos.ai.arbi.monitor;

import java.awt.Color;

public class RMC {
	private static final int MAJOR_VERSION = 2;
	private static final int MINOR_VERSION = 1;
	public static final int RMC_VERSION = getVersion(2, 1);
	public static final String RMC_VERSION_AS_STRING = getVersionAsString(2, 1);
	public static final String RMC_MAKER = "jetzt";
	public static final int INIT_FRAME_WIDTH = 500;
	public static final int INIT_FRAME_HEIGHT = 400;
	public static final int MAIN_FRAME_WIDTH = 750;
	public static final int MAIN_FRAME_HEIGHT = 650;
	public static final Color COLOR_VIOLET = Color.decode("#CC00FF");
	public static final Color COLOR_D_GREEN = Color.decode("#25703E");
	public static final int NAME_BOX_WIDTH = 80;
	public static final int NAME_BOX_HEIGHT = 30;
	public static final int NAME_BOX_HGAP = 40;
	public static final int SEQUENCE_VGAP = 80;

	private static int getVersion(int major, int minor) {
		return (major * 1000 + minor);
	}

	private static String getVersionAsString(int major, int minor) {
		StringBuilder builder = new StringBuilder();

		builder.append(major).append(".").append(minor);
		return builder.toString();
	}

	public static String getTitle() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgentFrameWork Monitor");
		return builder.toString();
	}
}
