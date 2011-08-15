/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.j2se;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

/**
 * Writes a {@link BitMatrix} to {@link BufferedImage},
 * file or stream. Provided here instead of core since it depends on
 * Java SE libraries.
 *
 * @author Sean Owen
 */
public final class MatrixToImageWriter {

  private static final int BLACK = 0xFF000000;
  private static final int WHITE = 0xFFFFFFFF;
  private static final int RED 	 = 0xFF00FFFF;
  private static final int GREEN = 0xFFFF00FF;
  private static final int BLUE  = 0xFFFFFF00;

  private MatrixToImageWriter() {}

  /**
   * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
   * as white, and "true" bits are rendered as black.
   */
  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? WHITE&RED&GREEN&BLUE : WHITE);        
      }
    }
    return image;
  }
  
  /**
   * adding
   * @param matrix
   * @return
   */
  public static BufferedImage toBufferedImage(BitMatrix[] matrix){
	  int width = matrix[0].getWidth();
	  int height = matrix[0].getHeight();
	  
	  BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	  
	  for(int x = 0; x<width; x++){
		  for(int y = 0; y<height; y++){
			  int color = WHITE;
			  if(matrix[0].get(x, y)) color &= RED;
			  if(matrix[1].get(x, y)) color &= GREEN;
			  if(matrix[2].get(x, y)) color &= BLUE;
			  
			  image.setRGB(x, y, color);			  
		  }
	  }
	  return image;
  }
  
  /**
   * adding
   * @param matrix
   * @param format
   * @param stream
   * @throws IOException
   */
  public static void writeToStream(BitMatrix[] matrix, String format, OutputStream stream)  throws IOException {
	  BufferedImage image = toBufferedImage(matrix);
	  ImageIO.write(image, format, stream);
  }

  /**
   * Writes a {@link BitMatrix} to a file.
   *
   * @see #toBufferedImage(BitMatrix)
   */
  public static void writeToFile(BitMatrix matrix, String format, File file)
          throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    ImageIO.write(image, format, file);
  }

  /**
   * Writes a {@link BitMatrix} to a stream.
   *
   * @see #toBufferedImage(BitMatrix)
   */
  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
          throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    ImageIO.write(image, format, stream);
  }

}
