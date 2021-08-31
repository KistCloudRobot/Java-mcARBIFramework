package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImageFactory {

	private static final ImageFactory instance = new ImageFactory();
	private Map<String, Image> imageMap;
	private Lock lock;
	
	public static ImageFactory getInstance() {
		return instance;
	}
	
	private ImageFactory() {
		this.imageMap = new HashMap();
		this.lock = new ReentrantLock();
	}
	
	public Image newImage(String imageName) {
		this.lock.lock();
		try {
			Image img = (Image) this.imageMap.get(imageName);
			Image localImage1;
			if(img != null) {
				localImage1 = img;
				return localImage1;
			}
			URL url = super.getClass().getResource(getImageFilename(imageName));
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			img = toolkit.getImage(url);
			toolkit.prepareImage(img, -1, -1, null);
			this.imageMap.put(imageName, img);
			return img;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			this.lock.unlock();
		}
	}
	
	private String getImageFilename(String imageName) {
		StringBuilder sb = new StringBuilder();
		sb.append("images/").append(imageName);
		return sb.toString();
	}
}
