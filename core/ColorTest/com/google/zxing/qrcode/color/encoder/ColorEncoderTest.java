package com.google.zxing.qrcode.color.encoder;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.encoder.Encoder;

public class ColorEncoderTest {

	@Test
	public void testSplitContent() {
		String str = "123123123";
		String[] contentArr = ColorEncoder.splitContent(str);
		assertTrue(contentArr[0].equals("123"));
		assertTrue(contentArr[1].equals("123"));
		assertTrue(contentArr[2].equals("123"));
		assertTrue(contentArr!=null);
		System.out.println("contentArr : "+contentArr[0]);
		System.out.println("contentArr : "+contentArr[1]);
		System.out.println("contentArr : "+contentArr[2]);
		Mode mode = Encoder.chooseMode(str, "ISO-8859-1");
		System.out.println("mode name : "+mode.getName());
		
		
		
	}

}
