package kr.ac.uos.ai.arbi.monitor.model;

public class Version {
	private int major;
	private int minor;
	private int micro;

	public Version() {
		init(0, 0, 0);
	}

	public Version(int major, int minor) {
		init(major, minor, 0);
	}

	public Version(int major, int minor, int micro) {
		init(major, minor, micro);
	}

	private void init(int major, int minor, int micro) {
		if (major < 0)
			major = 0;
		if (minor < 0)
			major = 0;
		if (micro < 0)
			major = 0;

		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	public int getMicro() {
		return this.micro;
	}

	public int getVersion() {
		return (this.major * 1000 + this.minor);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.major).append(".").append(this.minor).append(".").append(this.micro);
		return sb.toString();
	}

}
