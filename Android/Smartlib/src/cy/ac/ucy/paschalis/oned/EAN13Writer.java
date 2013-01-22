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
import cy.ac.ucy.paschalis.WriterException;
import cy.ac.ucy.paschalis.common.BitMatrix;

import java.util.Map;

/**
 * This object renders an EAN13 code as a {@link BitMatrix}.
 *
 * @author aripollak@gmail.com (Ari Pollak)
 */
public final class EAN13Writer extends UPCEANWriter {

  private static final int CODE_WIDTH = 3 + // start guard
      (7 * 6) + // left bars
      5 + // middle guard
      (7 * 6) + // right bars
      3; // end guard

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.EAN_13) {
      throw new IllegalArgumentException("Can only encode EAN_13, but got " + format);
    }

    return super.encode(contents, format, width, height, hints);
  }

  @Override
  public byte[] encode(String contents) {
    if (contents.length() != 13) {
      throw new IllegalArgumentException(
          "Requested contents should be 13 digits long, but got " + contents.length());
    }

    int firstDigit = Integer.parseInt(contents.substring(0, 1));
    int parities = EAN13Reader.FIRST_DIGIT_ENCODINGS[firstDigit];
    byte[] result = new byte[CODE_WIDTH];
    int pos = 0;

    pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, 1);

    // See {@link #EAN13Reader} for a description of how the first digit & left bars are encoded
    for (int i = 1; i <= 6; i++) {
      int digit = Integer.parseInt(contents.substring(i, i + 1));
      if ((parities >> (6 - i) & 1) == 1) {
        digit += 10;
      }
      pos += appendPattern(result, pos, UPCEANReader.L_AND_G_PATTERNS[digit], 0);
    }

    pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, 0);

    for (int i = 7; i <= 12; i++) {
      int digit = Integer.parseInt(contents.substring(i, i + 1));
      pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], 1);
    }
    pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, 1);

    return result;
  }

}
