package com.google.zxing.qrcode.color;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ColorQRCodeWriterTest {

	@Test
	public void testEncodeStringBarcodeFormatIntInt() {
		//fail("Not yet implemented");
		ColorQRCodeWriter color = new ColorQRCodeWriter();
		String str = "123123123";
		try {
			BitMatrix[] bitMatrix = color.encode(str, BarcodeFormat.QR_CODE, 100, 100);
			//assertTrue(bitMatrix != null);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
