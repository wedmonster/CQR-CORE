package com.google.zxing.qrcode.color.encoder;

import  java.util.Hashtable;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.MatrixUtil;
import com.google.zxing.qrcode.encoder.QRCode;

public final class ColorEncoder {
	
	
	protected static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";
	
	public static void encode(String contents, ErrorCorrectionLevel ecLevel, QRCode[] code) throws WriterException{
		encode(contents, ecLevel, null, code);
	}
	
	public static void encode(String content, ErrorCorrectionLevel ecLevel, Hashtable hints, QRCode[] code) throws WriterException{
		String encoding = hints == null ? null : (String) hints.get(EncodeHintType.CHARACTER_SET);
		if(encoding == null){
			encoding = DEFAULT_BYTE_MODE_ENCODING;
			
			//Step 1: Choose the mode
			Mode mode = Encoder.chooseMode(content, encoding);			
			
			//Step 2: Splits that contents into 3 strings.
			String[] contentArr = splitContent(content);
			
			//Step 3. Append "bytes" into "dataBits" in appropriate encoding.
			
			BitArray dataBits[] = new BitArray[3];
			for(int i = 0; i<dataBits.length; i++)
				dataBits[i] = new BitArray();
									
			for(int i = 0; i<3; i++){				
				Encoder.appendBytes(contentArr[i], mode, dataBits[i], encoding);				
			}
			
			//Step4. Initialize QR code that can contain MAX "dataBits".			
			int maxNumInpuBytes = maxInputBytes(dataBits);
			initQRCode(maxNumInpuBytes, ecLevel, mode, code);
			
			// Step 5: Build another bit vector that contains header and data.
			BitArray[] headerAndDataBits = new BitArray[3];
			for(int i = 0; i<headerAndDataBits.length; i++)
				headerAndDataBits[i] = new BitArray();
			
			 // Step 4.5: Append ECI message if applicable
			
			if (mode == Mode.BYTE && !DEFAULT_BYTE_MODE_ENCODING.equals(encoding)) {
			     CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(encoding);
			     if (eci != null) {
			    	 for(int i = 0; i<3; i++)
			    		 Encoder.appendECI(eci, headerAndDataBits[i]);			    	 			       
			     }
			}
			for(int i = 0; i<3; i++){
				Encoder.appendModeInfo(mode, headerAndDataBits[i]);
				int numLetters = mode.equals(Mode.BYTE) ? dataBits[i].getSizeInBytes() : contentArr[i].length();
				Encoder.appendLengthInfo(numLetters, code[i].getVersion(), mode, headerAndDataBits[i]);
			    headerAndDataBits[i].appendBitArray(dataBits[i]);
			    
			    // Step 5: Terminate the bits properly.
			    Encoder.terminateBits(code[i].getNumDataBytes(), headerAndDataBits[i]);
			    
			    // Step 6: Interleave data bits with error correction code.
			    BitArray finalBits = new BitArray();
			    Encoder.interleaveWithECBytes(headerAndDataBits[i], code[i].getNumTotalBytes(), code[i].getNumDataBytes(),
			        code[i].getNumRSBlocks(), finalBits);
			    
			    // Step 7: Choose the mask pattern and set to "qrCode".
			    ByteMatrix matrix = new ByteMatrix(code[i].getMatrixWidth(), code[i].getMatrixWidth());
			    code[i].setMaskPattern(Encoder.chooseMaskPattern(finalBits, code[i].getECLevel(), code[i].getVersion(),
			        matrix));
			    
			    // Step 8.  Build the matrix and set it to "qrCode".
			    MatrixUtil.buildMatrix(finalBits, code[i].getECLevel(), code[i].getVersion(),
			    		code[i].getMaskPattern(), matrix);
			    code[i].setMatrix(matrix);
			    
			    // Step 9.  Make sure we have a valid QR Code.
			    if (!code[i].isValid()) {
			      throw new WriterException("Invalid QR code: " + code[i].toString());
			    }
			}
		}
	}
	
	public static int maxInputBytes(BitArray[] bits){
		int max = 0;
		if(max < bits[0].getSizeInBytes()) max = bits[0].getSizeInBytes();
		if(max < bits[1].getSizeInBytes()) max = bits[1].getSizeInBytes();
		if(max < bits[2].getSizeInBytes()) max = bits[2].getSizeInBytes();
		return max;		
	}
	
	public static String[] splitContent(String content){
		
		String[] contentArr = new String[3];
		
		int divide = content.length()/3;
		
		contentArr[0] = content.substring(0, divide);
		contentArr[1] = content.substring(divide, divide+divide);
		contentArr[2] = content.substring(divide+divide, content.length());
		
		return contentArr;
	}
	
	private static void initQRCode(int maxNumInputBytes, ErrorCorrectionLevel ecLevel, Mode mode,
		      QRCode[] qrCode) throws WriterException {
			for(int i = 0; i<3;i++){
			    qrCode[i].setECLevel(ecLevel);
			    qrCode[i].setMode(mode);
			}

		    // In the following comments, we use numbers of Version 7-H.
		    for (int versionNum = 1; versionNum <= 40; versionNum++) {
		      Version version = Version.getVersionForNumber(versionNum);
		      // numBytes = 196
		      int numBytes = version.getTotalCodewords();
		      // getNumECBytes = 130
		      Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
		      int numEcBytes = ecBlocks.getTotalECCodewords();
		      // getNumRSBlocks = 5
		      int numRSBlocks = ecBlocks.getNumBlocks();
		      // getNumDataBytes = 196 - 130 = 66
		      int numDataBytes = numBytes - numEcBytes;
		      // We want to choose the smallest version which can contain data of "numInputBytes" + some
		      // extra bits for the header (mode info and length info). The header can be three bytes
		      // (precisely 4 + 16 bits) at most. Hence we do +3 here.
		      if (numDataBytes >= maxNumInputBytes + 3) {
		        // Yay, we found the proper rs block info!
		    	for(int i = 0; i<3; i++){  
			        qrCode[i].setVersion(versionNum);
			        qrCode[i].setNumTotalBytes(numBytes);
			        qrCode[i].setNumDataBytes(numDataBytes);
			        qrCode[i].setNumRSBlocks(numRSBlocks);
			        // getNumECBytes = 196 - 66 = 130
			        qrCode[i].setNumECBytes(numEcBytes);
			        // matrix width = 21 + 6 * 4 = 45
			        qrCode[i].setMatrixWidth(version.getDimensionForVersion());
		    	}
		        return;
		      }
		    }
		    throw new WriterException("Cannot find proper rs block info (input data too big?)");
		  }
}
