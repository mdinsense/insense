package com.ensense.insense.core.utils;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageCreationUtil {

	private static final Logger logger = Logger.getLogger(ImageCreationUtil.class);
	
	public static void takeScreenShotMethod(String imageFilePath) {
		try {
			// Thread.sleep(10000);
			BufferedImage image = new Robot()
					.createScreenCapture(new Rectangle(Toolkit
							.getDefaultToolkit().getScreenSize()));
			ImageIO.write(image, "jpg", new File(imageFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void convertPngtoJpeg(File pngFile, String jpegFilePath)
			throws Exception {
		BufferedImage bufferedImage;

		try {

			// read image file
			bufferedImage = ImageIO.read(pngFile);

			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
					Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(jpegFilePath));

		} catch (IOException e) {
			logger.error("Error while creating JPEG file", e);
			e.printStackTrace();
			throw new IOException(e);
		}

	}

}
