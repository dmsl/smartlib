/*
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
	

 */

package cy.ac.ucy.pmpeis01.pdf417.encoder;

import java.util.EnumMap;
import java.util.Map;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.EncodeHintType;
import cy.ac.ucy.pmpeis01.Writer;
import cy.ac.ucy.pmpeis01.WriterException;
import cy.ac.ucy.pmpeis01.common.BitMatrix;

/**
 * @author Jacob Haynes
 * @author qwandor@google.com (Andrew Walbran)
 */
public final class PDF417Writer implements Writer {

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.PDF_417) {
      throw new IllegalArgumentException("Can only encode PDF_417, but got " + format);
    }

    PDF417 encoder = new PDF417();

    if (hints != null) {
      if (hints.containsKey(EncodeHintType.PDF417_COMPACT)) {
        encoder.setCompact((Boolean) hints.get(EncodeHintType.PDF417_COMPACT));
      }
      if (hints.containsKey(EncodeHintType.PDF417_COMPACTION)) {
        encoder.setCompaction((Compaction) hints.get(EncodeHintType.PDF417_COMPACTION));
      }
      if (hints.containsKey(EncodeHintType.PDF417_DIMENSIONS)) {
        Dimensions dimensions = (Dimensions) hints.get(EncodeHintType.PDF417_DIMENSIONS);
        encoder.setDimensions(dimensions.getMaxCols(),
                              dimensions.getMinCols(),
                              dimensions.getMaxRows(),
                              dimensions.getMinRows());
      }
    }

    return bitMatrixFromEncoder(encoder, contents, width, height);
  }

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height) throws WriterException {
    return encode(contents, format, width, height, null);
  }

  /**
   * @deprecated Use {@link #encode(String, BarcodeFormat, int, int, Map)} instead, with hints to
   * specify the encoding options.
   */
  @Deprecated
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          boolean compact,
                          int width,
                          int height,
                          int minCols,
                          int maxCols,
                          int minRows,
                          int maxRows,
                          Compaction compaction) throws WriterException {
    Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType,Object>(EncodeHintType.class);
    hints.put(EncodeHintType.PDF417_COMPACT, compact);
    hints.put(EncodeHintType.PDF417_COMPACTION, compaction);
    hints.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(minCols, maxCols, minRows, maxRows));
    return encode(contents, format, width, height, hints);
  }

  /**
   * Takes encoder, accounts for width/height, and retrieves bit matrix
   */
  private static BitMatrix bitMatrixFromEncoder(PDF417 encoder,
                                                String contents,
                                                int width,
                                                int height) throws WriterException {
    int errorCorrectionLevel = 2;
    encoder.generateBarcodeLogic(contents, errorCorrectionLevel);

    int lineThickness = 2;
    int aspectRatio = 4;
    byte[][] originalScale = encoder.getBarcodeMatrix().getScaledMatrix(lineThickness, aspectRatio * lineThickness);
    boolean rotated = false;
    if ((height > width) ^ (originalScale[0].length < originalScale.length)) {
      originalScale = rotateArray(originalScale);
      rotated = true;
    }

    int scaleX = width / originalScale[0].length;
    int scaleY = height / originalScale.length;

    int scale;
    if (scaleX < scaleY) {
      scale = scaleX;
    } else {
      scale = scaleY;
    }

    if (scale > 1) {
      byte[][] scaledMatrix =
          encoder.getBarcodeMatrix().getScaledMatrix(scale * lineThickness, scale * aspectRatio * lineThickness);
      if (rotated) {
        scaledMatrix = rotateArray(scaledMatrix);
      }
      return bitMatrixFrombitArray(scaledMatrix);
    }
    return bitMatrixFrombitArray(originalScale);
  }

  /**
   * This takes an array holding the values of the PDF 417
   *
   * @param input a byte array of information with 0 is black, and 1 is white
   * @return BitMatrix of the input
   */
  private static BitMatrix bitMatrixFrombitArray(byte[][] input) {
    // Creates a small whitespace border around the barcode
    int whiteSpace = 30;

    // Creates the bitmatrix with extra space for whitespace
    BitMatrix output = new BitMatrix(input[0].length + 2 * whiteSpace, input.length + 2 * whiteSpace);
    output.clear();
    for (int y = 0, yOutput = output.getHeight() - whiteSpace; y < input.length; y++, yOutput--) {
      for (int x = 0; x < input[0].length; x++) {
        // Zero is white in the bytematrix
        if (input[y][x] == 1) {
          output.set(x + whiteSpace, yOutput);
        }
      }
    }
    return output;
  }

  /**
   * Takes and rotates the it 90 degrees
   */
  private static byte[][] rotateArray(byte[][] bitarray) {
    byte[][] temp = new byte[bitarray[0].length][bitarray.length];
    for (int ii = 0; ii < bitarray.length; ii++) {
      // This makes the direction consistent on screen when rotating the
      // screen;
      int inverseii = bitarray.length - ii - 1;
      for (int jj = 0; jj < bitarray[0].length; jj++) {
        temp[jj][inverseii] = bitarray[ii][jj];
      }
    }
    return temp;
  }

}
