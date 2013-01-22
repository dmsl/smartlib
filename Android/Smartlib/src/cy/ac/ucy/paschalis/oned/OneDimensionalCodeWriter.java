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

package cy.ac.ucy.paschalis.oned;

import cy.ac.ucy.paschalis.BarcodeFormat;
import cy.ac.ucy.paschalis.EncodeHintType;
import cy.ac.ucy.paschalis.Writer;
import cy.ac.ucy.paschalis.WriterException;
import cy.ac.ucy.paschalis.common.BitMatrix;

import java.util.Map;

/**
 * <p>Encapsulates functionality and implementation that is common to one-dimensional barcodes.</p>
 *
 * @author dsbnatut@gmail.com (Kazuki Nishiura)
 */
public abstract class OneDimensionalCodeWriter implements Writer {

  private final int sidesMargin;

  protected OneDimensionalCodeWriter(int sidesMargin) {
    this.sidesMargin = sidesMargin;
  }

  @Override
  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException {
    return encode(contents, format, width, height, null);
  }

  /**
   * Encode the contents following specified format.
   * {@code width} and {@code height} are required size. This method may return bigger size
   * {@code BitMatrix} when specified size is too small. The user can set both {@code width} and
   * {@code height} to zero to get minimum size barcode. If negative value is set to {@code width}
   * or {@code height}, {@code IllegalArgumentException} is thrown.
   */
  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (contents.length() == 0) {
      throw new IllegalArgumentException("Found empty contents");
    }

    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Negative size is not allowed. Input: "
                                             + width + 'x' + height);
    }

    byte[] code = encode(contents);
    return renderResult(code, width, height);
  }

  /**
   * @return a byte array of horizontal pixels (0 = white, 1 = black)
   */
  private BitMatrix renderResult(byte[] code, int width, int height) {
    int inputWidth = code.length;
    // Add quiet zone on both sides.
    int fullWidth = inputWidth + sidesMargin;
    int outputWidth = Math.max(width, fullWidth);
    int outputHeight = Math.max(1, height);

    int multiple = outputWidth / fullWidth;
    int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;

    BitMatrix output = new BitMatrix(outputWidth, outputHeight);
    for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
      if (code[inputX] == 1) {
        output.setRegion(outputX, 0, multiple, outputHeight);
      }
    }
    return output;
  }


  /**
   * Appends the given pattern to the target array starting at pos.
   *
   * @param startColor starting color - 0 for white, 1 for black
   * @return the number of elements added to target.
   */
  protected static int appendPattern(byte[] target, int pos, int[] pattern, int startColor) {
    if (startColor != 0 && startColor != 1) {
      throw new IllegalArgumentException(
          "startColor must be either 0 or 1, but got: " + startColor);
    }

    byte color = (byte) startColor;
    int numAdded = 0;
    for (int len : pattern) {
      for (int j = 0; j < len; j++) {
        target[pos] = color;
        pos += 1;
        numAdded += 1;
      }
      color ^= 1; // flip color after each segment
    }
    return numAdded;
  }

  /**
   * Encode the contents to byte array expression of one-dimensional barcode.
   * Start code and end code should be included in result, and side margins should not be included.
   *
   * @return a byte array of horizontal pixels (0 = white, 1 = black)
   */
  public abstract byte[] encode(String contents);
}

