package com.ensense.insense.services.common.image;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompare {
	 private static Logger logger = Logger.getLogger(ImageCompare.class);
	 
	protected BufferedImage img1 = null;
	protected BufferedImage img2 = null;
	protected BufferedImage imgc = null;
	protected int comparex = 0;
	protected int comparey = 0;
	protected int factorA = 0;
	protected int factorD = 10;
	protected boolean match = false;
	protected int debugMode = 0; // 1: textual indication of change, 2: difference of factors

	/* create a runable demo thing. */
	public static void main(String[] args) throws Exception {
		// Create a compare object specifying the 2 images for comparison.
		ImageCompare ic = new ImageCompare("C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_Public_PROD_2015-Sep-24_11-17-36\\firefox\\PublicSite\\about-us\\how-we-serve-you.jpeg", 
				"C:\\Tools\\mint\\Application\\ResultsDirectory\\PublicSite_Public_PROD_2015-Sep-24_11-00-10\\firefox\\PublicSite\\about-us\\how-we-serve-you.jpeg");
		// Set the comparison parameters. 
		//   (num vertical regions, num horizontal regions, sensitivity, stabilizer) //Only for main method, for app set it in line 86.
		ic.setParameters(24, 18, 0, 500);
		//ic.setParameters(24, 18, 0, 1000);
		//ic.setParameters(48, 36, 0, 1000); //working
		//ic.setParameters(16, 12, 0, 1000); //Before
		//ic.setParameters(48, 36, 0, 5);
		// Display some indication of the differences in the image.
		ic.setDebugMode(2);
		// Compare.
		ic.compare();
		// Display if these images are considered a match according to our parameters.
		System.out.println("Match: " + ic.match());
		// If its not a match then write a file to show changed regions.
		if (!ic.match()) {
			saveJPG(ic.getChangeIndicator(), "C:\\Tools\\mint\\changes.jpg");
		}
	}
	
	// constructor 1. use filenames
	public ImageCompare(String file1, String file2) throws Exception {
		this(loadJPG(file1), loadJPG(file2));
	}
 
	// constructor 2. use awt images.
	public ImageCompare(Image img1, Image img2) {
		this(imageToBufferedImage(img1), imageToBufferedImage(img2));
	}
 
	// constructor 3. use buffered images. all roads lead to the same place. this place.
	public ImageCompare(BufferedImage img1, BufferedImage img2) {
		this.img1 = img1;
		this.img2 = img2;
		autoSetParameters();
	}
	
	//constructor 4. Empty constructor, when this is used should call compareImage(String file1, String file2);
	public ImageCompare(){
		
	}
	
	public boolean compareImage(String file1, String file2) throws Exception{
		this.img1 = imageToBufferedImage(loadJPG(file1));
		this.img2 = imageToBufferedImage(loadJPG(file2));
		//autoSetParameters();
		
		return compareImage();
	}
	public boolean compareImage(){
		setParameters(32, 24, 0, 500);
		//setParameters(32, 24, 0, 1000);
		compare();
		
		return match();
	}
	// like this to perhaps be upgraded to something more heuristic in the future.
	protected void autoSetParameters() {
		comparex = 10;
		comparey = 10;
		factorA = 10;
		factorD = 10;
	}
	
	// set the parameters for use during change detection.
	public void setParameters(int x, int y, int factorA, int factorD) {
		this.comparex = x;
		this.comparey = y;
		this.factorA = factorA;
		this.factorD = factorD;
	}
	
	// want to see some stuff in the console as the comparison is happening?
	public void setDebugMode(int m) {
		this.debugMode = m;
	}
	
	// compare the two images in this object.
	public void compare() {
		// setup change display image
		imgc = imageToBufferedImage(img2);
		Graphics2D gc = imgc.createGraphics();
		gc.setColor(Color.RED);
		// convert to gray images.
		img1 = imageToBufferedImage(GrayFilter.createDisabledImage(img1));
		img2 = imageToBufferedImage(GrayFilter.createDisabledImage(img2));
		// how big are each section
		int blocksx = (int)(img1.getWidth() / comparex);
		int blocksy = (int)(img1.getHeight() / comparey);
	
		int blocksx1 = (int)(img2.getWidth() / comparex);
		int blocksy1 = (int)(img2.getHeight() / comparey);
		
		// set to a match by default, if a change is found then flag non-match
		this.match = true;
		// loop through whole image and compare individual blocks of images

		for (int y = 0; y < (comparey-1); y++) {
			if (debugMode > 0) System.out.print("|");
			for (int x = 0; x < (comparex-1); x++) {
				int b1 = getAverageBrightness(img1.getSubimage(x*blocksx, y*blocksy, blocksx - 1, blocksy - 1));
				int b2 = getAverageBrightness(img2.getSubimage(x*blocksx1, y*blocksy1, blocksx1 - 1, blocksy1 - 1));
				int diff = Math.abs(b1 - b2);
				if (diff > factorA) { // the difference in a certain region has passed the threshold value of factorA
					// draw an indicator on the change image to show where change was detected.
					gc.drawRect(x*blocksx, y*blocksy, blocksx - 1, blocksy - 1);
					this.match = false;
				}
				if (debugMode == 1) System.out.print((diff > factorA ? "X" : " "));
				if (debugMode == 2) System.out.print(diff + (x < comparex - 1 ? "," : ""));
			}
			if (debugMode > 0) System.out.println("|");
		}
	}
	
	// return the image that indicates the regions where changes where detected.
	public BufferedImage getChangeIndicator() {
		return imgc;
	}
	
	// returns a value specifying some kind of average brightness in the image.
	protected int getAverageBrightness(BufferedImage img) {
		Raster r = img.getData();
		int total = 0;
		for (int y = 0; y < r.getHeight(); y++) {
			for (int x = 0; x < r.getWidth(); x++) {
				total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0);
			}
		}
		return (int)(total / ((r.getWidth()/factorD)*(r.getHeight()/factorD)));
	}
	

	// returns true if image pair is considered a match
	public boolean match() {
		return this.match;
	}

	// buffered images are just better.
	protected static BufferedImage imageToBufferedImage(Image img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, null, null);
		return bi;
	}
	
	// write a buffered image to a jpeg file.
	public static void saveJPG(Image img, String filename) throws IOException {
		BufferedImage bi = imageToBufferedImage(img);
		FileOutputStream out = null;
		try { 
			out = new FileOutputStream(filename);
			ImageIO.write(bi, "jpg", out);
		} catch (IOException io) { 
			logger.error("Exception while saving JPG, File Not Found :"+filename); 
		}finally{
			out.close();
		}
	}
	
	// read a jpeg file into a buffered image
	protected static Image loadJPG(String filename) throws Exception{
		FileInputStream in = null;
		try { 
			in = new FileInputStream(filename);
		} catch (java.io.FileNotFoundException io) { 
			logger.error("Exception while reading the JPG, File Not Found :"+filename);
			throw io;
		}
		
		
		BufferedImage bi = null;
		try { 
			bi = ImageIO.read(in);
			
		} catch (IOException io) {
			logger.error("IOException while reading the JPG.");
			throw io;
		}finally{
			in.close();
		}
		return bi;
	}
	
}
