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

package cy.ac.ucy.pmpeis01.oned;

import java.util.Map;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.EncodeHintType;
import cy.ac.ucy.pmpeis01.WriterException;
import cy.ac.ucy.pmpeis01.common.BitMatrix;

/**
 * This object renders a CODE39 code as a {@link BitMatrix}.
 * 
 * @author erik.barbara@gmail.com (Erik Barbara)
 */
public final class Code39Writer extends UPCEANWriter {

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.CODE_39) {
      throw new IllegalArgumentException("Can only encode CODE_39, but got " + format);
    }
    return super.encode(contents, format, width, height, hints);
  }

  @Override
  public byte[] encode(String contents) {
    int length = contents.length();
    if (length > 80) {
      throw new IllegalArgumentException(
          "Requested contents should be less than 80 digits long, but got " + length);
    }

    int[] widths = new int[9];
    int codeWidth = 24 + 1 + length;
    for (int i = 0; i < length; i++) {
      int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
      toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
      for (int width : widths) {
        codeWidth += width;
      }
    }
    byte[] result = new byte[codeWidth];
    toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], widths);
    int pos = appendPattern(result, 0, widths, 1);
    int[] narrowWhite = {1};
    pos += appendPattern(result, pos, narrowWhite, 0);
    //append next character to bytematrix
    for(int i = length-1; i >= 0; i--) {
      int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
      toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
      pos += appendPattern(result, pos, widths, 1);
      pos += appendPattern(result, pos, narrowWhite, 0);
    }
    toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], widths);
    pos += appendPattern(result, pos, widths, 1);
    return result;
  }

  private static void toIntArray(int a, int[] toReturn) {
    for (int i = 0; i < 9; i++) {
      int temp = a & (1 << i);
      toReturn[i] = temp == 0 ? 1 : 2;
    }
  }

}