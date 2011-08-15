package com.google.zxing.qrcode.color;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.color.encoder.ColorEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public class ColorQRCodeWriter extends QRCodeReader{

	public BitMatrix[] encode(String contents, BarcodeFormat format, int width, int height) throws WriterException{
		return encode(contents, format, width, height, null);
	}
	
	public BitMatrix[] encode(String contents, BarcodeFormat format, int width, 
			int height, Hashtable hints) throws WriterException{

	    if (contents == null || contents.length() == 0) {
	      throw new IllegalArgumentException("Found empty contents");
	    }

	    if (format != BarcodeFormat.QR_CODE) {
	      throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
	    }

	    if (width < 0 || height < 0) {
	      throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
	          height);
	    }

	    ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
	    if (hints != null) {
	      ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel) hints.get(EncodeHintType.ERROR_CORRECTION);
	      if (requestedECLevel != null) {
	        errorCorrectionLevel = requestedECLevel;
	      }
	    }

	    QRCode[] code = new QRCode[3];	 
	    for(int i = 0; i<code.length; i++)
	    	code[i] = new QRCode();
	    
	    ColorEncoder.encode(contents, errorCorrectionLevel, hints, code);
		
		return renderResult(code, width, height);
	}
	
	private static BitMatrix[] renderResult(QRCode[] code, int width, int height) {
		
		BitMatrix[] output = new BitMatrix[3];
		
		
		for(int i = 0; i<3; i++){
		    ByteMatrix input = code[i].getMatrix();
		    int inputWidth = input.getWidth();
		    int inputHeight = input.getHeight();
		    int qrWidth = inputWidth + (QRCodeWriter.QUIET_ZONE_SIZE << 1);
		    int qrHeight = inputHeight + (QRCodeWriter.QUIET_ZONE_SIZE << 1);
		    int outputWidth = Math.max(width, qrWidth);
		    int outputHeight = Math.max(height, qrHeight);
	
		    int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
		    // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
		    // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
		    // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
		    // handle all the padding from 100x100 (the actual QR) up to 200x160.
		    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
		    int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
	
		    //BitMatrix output = new BitMatrix(outputWidth, outputHeight);
		    //output[i].set(outputWidth, outputHeight);
		    output[i] = new BitMatrix(outputWidth, outputHeight);
	
		    for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
		      // Write the contents of this row of the barcode
		      for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
		        if (input.get(inputX, inputY) == 1) {
		          output[i].setRegion(outputX, outputY, multiple, multiple);
		        }
		      }
		    }	    
		}

	    return output;
	  }

}
