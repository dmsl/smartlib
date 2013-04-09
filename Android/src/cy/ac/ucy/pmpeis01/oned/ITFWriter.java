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
 * This object renders a ITF code as a {@link BitMatrix}.
 * 
 * @author erik.barbara@gmail.com (Erik Barbara)
 */
public final class ITFWriter extends UPCEANWriter {

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.ITF) {
      throw new IllegalArgumentException("Can only encode ITF, but got " + format);
    }

    return super.encode(contents, format, width, height, hints);
  }

  @Override
  public byte[] encode(String contents) {
    int length = contents.length();
    if (length % 2 != 0) {
      throw new IllegalArgumentException("The lenght of the input should be even");
    }
    if (length > 80) {
      throw new IllegalArgumentException(
          "Requested contents should be less than 80 digits long, but got " + length);
    }
    byte[] result = new byte[9 + 9 * length];
    int[] start = {1, 1, 1, 1};
    int pos = appendPattern(result, 0, start, 1);
    for (int i = 0; i < length; i += 2) {
      int one = Character.digit(contents.charAt(i), 10);
      int two = Character.digit(contents.charAt(i+1), 10);
      int[] encoding = new int[18];
      for (int j = 0; j < 5; j++) {
        encoding[(j << 1)] = ITFReader.PATTERNS[one][j];
        encoding[(j << 1) + 1] = ITFReader.PATTERNS[two][j];
      }
      pos += appendPattern(result, pos, encoding, 1);
    }
    int[] end = {3, 1, 1};
    appendPattern(result, pos, end, 1);

    return result;
  }

}