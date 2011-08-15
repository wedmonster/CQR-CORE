package com.google.zxing.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.color.ColorQRCodeWriter;

public class ColorQRCodeTest {
	public static void main(String[] args){
		ColorQRCodeWriter cq = new ColorQRCodeWriter();
		
		String str = "123123123123123123123";
		
		try {
			BitMatrix[] bitMatrix = cq.encode(str, BarcodeFormat.QR_CODE, 100, 100);
			
			/*for(int i = 0; i<bitMatrix.length; i++){
				MatrixToImageWriter.writeToStream(bitMatrix[i], "png", new FileOutputStream(new File(i+".png")));
			}*/
			
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
		}
	}
}
