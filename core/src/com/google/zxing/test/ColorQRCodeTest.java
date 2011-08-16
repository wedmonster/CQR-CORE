package com.google.zxing.test;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.color.ColorQRCodeWriter;

public class ColorQRCodeTest {
	public static void main(String[] args){
		//ColorQRCodeWriter cq = new ColorQRCodeWriter();
		/*QRCodeWriter qw = new QRCodeWriter();
		String str = "123123123123123123123";
		
		BitMatrix bitMatrix;
		try {
			bitMatrix = qw.encode(str, BarcodeFormat.QR_CODE, 100, 100);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("test.png")));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		QRCodeReader qr = new QRCodeReader();
		/*
		Image img = Toolkit.getDefaultToolkit().getImage("test.png");
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
			
		BufferedImage buf = gc.createCompatibleImage(img.getWidth(null), img.getHeight(null));
		*/
		File file = new File("2.jpeg");		
		
		
		
		//ImageIO.write(getBufferedImage(image), "JPEG", baos);		
		
		
		try {
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//BufferedImage img = ImageIO.read(file);
			
			//ImageIO.write(img, "jpg", baos);
			//baos.flush();
			
			//byte[] data = baos.toByteArray();
			/*Image img = new ImageIcon("3.jpg").getImage();
			
			BufferedImage buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2 = buf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			*/
		      
			//PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource (data, 480, 320, 0, 0, 480, 320, false);
			//BufferedImageLuminanceSource BILS = new BufferedImageLuminanceSource(buf);
			//HybridBinarizer hybridBinarizer = new HybridBinarizer(BILS);
			//BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
			
			BufferedImage image = ImageIO.read(new File("color2.png"));
			BufferedImage red_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			BufferedImage green_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			BufferedImage blue_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			
			int RED = 0x00FF0000;
			int GREEN = 0x0000FF00;
			int BLUE = 0x000000FF;
			int WHITE = 0xFFFFFFFF;
			int BLACK = 0x00000000;
			
			
			for(int i = 0; i<image.getWidth(); i++){
				for(int j = 0; j<image.getHeight(); j++){
					int color = image.getRGB(i, j);
					//System.out.println(color);
					
					int red  = color & RED;
					//if(red <= BLACK/2) red = WHITE; 
					//if(red != BLACK) red = WHITE;
					red_image.setRGB(i, j, red);
					int green = color & GREEN;
					//if(green != BLACK) green = WHITE;
					green_image.setRGB(i, j, green);
					int blue = color & BLUE;
					blue = blue | 0x00111111;
					//if(blue != BLACK) blue = WHITE;
					blue_image.setRGB(i, j, blue);
				}
			}
			/*
			for(int i = 0; i<image.getWidth(); i++){
				for(int j = 0; j<image.getHeight(); j++){
					int color = image.getRGB(i, j);
					
					//System.out.println(color);
					//int r  = color & RED;
					//int g = color & GREEN;
					//int b = color & BLUE;
					
					int Y  = color & RED;
					int U = color & GREEN;
					int V = color & BLUE;
					
					int r = (int)(Y + 1.4075 * (V - 128));
	                int g = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
	                int b =(int) (Y + 1.7790 * (U - 128));
					
					//int y = (int)(0.299 * r + 0.587 * g + 0.114 * b);
					//int u = (int)((b - y) * 0.492f); 
					//int v = (int)((r - y) * 0.877f);
					//if(red != BLACK) red = WHITE;
				
					red_image.setRGB(i, j, r);
					
					//if(green != BLACK) green = WHITE;
					green_image.setRGB(i, j, g);
					
					//if(blue != BLACK) blue = WHITE;
					blue_image.setRGB(i, j, b);
				}
			}*/
			
			ImageIO.write(red_image, "png", new FileOutputStream(new File("red.png")));
			ImageIO.write(green_image, "png", new FileOutputStream(new File("green.png")));
			ImageIO.write(blue_image, "png", new FileOutputStream(new File("blue.png")));
			
			//BinaryBitmap binaryMap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(image)));
			
			BinaryBitmap redMap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(red_image)));
			BinaryBitmap greenMap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(green_image)));
			BinaryBitmap blueMap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(blue_image)));
			
						
			Result red_r = qr.decode(redMap);
			Result green_r = qr.decode(greenMap);
			Result blue_r = qr.decode(blueMap);
			
			
			System.out.println(red_r.getText());
			System.out.println(green_r.getText());
			System.out.println(blue_r.getText());
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChecksumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*
		try {
			BitMatrix[] bitMatrix = cq.encode(str, BarcodeFormat.QR_CODE, 100, 100);
			
			/*for(int i = 0; i<bitMatrix.length; i++){
				MatrixToImageWriter.writeToStream(bitMatrix[i], "png", new FileOutputStream(new File(i+".png")));
			}
			
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("color.png")));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
