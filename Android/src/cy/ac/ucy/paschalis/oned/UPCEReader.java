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
import cy.ac.ucy.paschalis.ChecksumException;
import cy.ac.ucy.paschalis.FormatException;
import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.common.BitArray;

/**
 * <p>Implements decoding of the UPC-E format.</p>
 * <p/>
 * <p><a href="http://www.barcodeisland.com/upce.phtml">This</a> is a great reference for
 * UPC-E information.</p>
 *
 * @author Sean Owen
 */
public final class UPCEReader extends UPCEANReader {

  /**
   * The pattern that marks the middle, and end, of a UPC-E pattern.
   * There is no "second half" to a UPC-E barcode.
   */
  private static final int[] MIDDLE_END_PATTERN = {1, 1, 1, 1, 1, 1};

  /**
   * See {@link #L_AND_G_PATTERNS}; these values similarly represent patterns of
   * even-odd parity encodings of digits that imply both the number system (0 or 1)
   * used, and the check digit.
   */
  private static final int[][] NUMSYS_AND_CHECK_DIGIT_PATTERNS = {
      {0x38, 0x34, 0x32, 0x31, 0x2C, 0x26, 0x23, 0x2A, 0x29, 0x25},
      {0x07, 0x0B, 0x0D, 0x0E, 0x13, 0x19, 0x1C, 0x15, 0x16, 0x1A}
  };

  private final int[] decodeMiddleCounters;

  public UPCEReader() {
    decodeMiddleCounters = new int[4];
  }

  @Override
  protected int decodeMiddle(BitArray row, int[] startRange, StringBuilder result)
      throws NotFoundException {
    int[] counters = decodeMiddleCounters;
    counters[0] = 0;
    counters[1] = 0;
    counters[2] = 0;
    counters[3] = 0;
    int end = row.getSize();
    int rowOffset = startRange[1];

    int lgPatternFound = 0;

    for (int x = 0; x < 6 && rowOffset < end; x++) {
      int bestMatch = decodeDigit(row, counters, rowOffset, L_AND_G_PATTERNS);
      result.append((char) ('0' + bestMatch % 10));
      for (int counter : counters) {
        rowOffset += counter;
      }
      if (bestMatch >= 10) {
        lgPatternFound |= 1 << (5 - x);
      }
    }

    determineNumSysAndCheckDigit(result, lgPatternFound);

    return rowOffset;
  }

  @Override
  protected int[] decodeEnd(BitArray row, int endStart) throws NotFoundException {
    return findGuardPattern(row, endStart, true, MIDDLE_END_PATTERN);
  }

  @Override
  protected boolean checkChecksum(String s) throws FormatException, ChecksumException {
    return super.checkChecksum(convertUPCEtoUPCA(s));
  }

  private static void determineNumSysAndCheckDigit(StringBuilder resultString, int lgPatternFound)
      throws NotFoundException {

    for (int numSys = 0; numSys <= 1; numSys++) {
      for (int d = 0; d < 10; d++) {
        if (lgPatternFound == NUMSYS_AND_CHECK_DIGIT_PATTERNS[numSys][d]) {
          resultString.insert(0, (char) ('0' + numSys));
          resultString.append((char) ('0' + d));
          return;
        }
      }
    }
    throw NotFoundException.getNotFoundInstance();
  }

  @Override
  BarcodeFormat getBarcodeFormat() {
    return BarcodeFormat.UPC_E;
  }

  /**
   * Expands a UPC-E value back into its full, equivalent UPC-A code value.
   *
   * @param upce UPC-E code as string of digits
   * @return equivalent UPC-A code as string of digits
   */
  public static String convertUPCEtoUPCA(String upce) {
    char[] upceChars = new char[6];
    upce.getChars(1, 7, upceChars, 0);
    StringBuilder result = new StringBuilder(12);
    result.append(upce.charAt(0));
    char lastChar = upceChars[5];
    switch (lastChar) {
      case '0':
      case '1':
      case '2':
        result.append(upceChars, 0, 2);
        result.append(lastChar);
        result.append("0000");
        result.append(upceChars, 2, 3);
        break;
      case '3':
        result.append(upceChars, 0, 3);
        result.append("00000");
        result.append(upceChars, 3, 2);
        break;
      case '4':
        result.append(upceChars, 0, 4);
        result.append("00000");
        result.append(upceChars[4]);
        break;
      default:
        result.append(upceChars, 0, 5);
        result.append("0000");
        result.append(lastChar);
        break;
    }
    result.append(upce.charAt(7));
    return result.toString();
  }

}