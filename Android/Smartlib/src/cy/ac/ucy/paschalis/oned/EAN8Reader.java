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
import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.common.BitArray;

/**
 * <p>Implements decoding of the EAN-8 format.</p>
 *
 * @author Sean Owen
 */
public final class EAN8Reader extends UPCEANReader {

  private final int[] decodeMiddleCounters;

  public EAN8Reader() {
    decodeMiddleCounters = new int[4];
  }

  @Override
  protected int decodeMiddle(BitArray row,
                             int[] startRange,
                             StringBuilder result) throws NotFoundException {
    int[] counters = decodeMiddleCounters;
    counters[0] = 0;
    counters[1] = 0;
    counters[2] = 0;
    counters[3] = 0;
    int end = row.getSize();
    int rowOffset = startRange[1];

    for (int x = 0; x < 4 && rowOffset < end; x++) {
      int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
      result.append((char) ('0' + bestMatch));
      for (int counter : counters) {
        rowOffset += counter;
      }
    }

    int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
    rowOffset = middleRange[1];

    for (int x = 0; x < 4 && rowOffset < end; x++) {
      int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
      result.append((char) ('0' + bestMatch));
      for (int counter : counters) {
        rowOffset += counter;
      }
    }

    return rowOffset;
  }

  @Override
  BarcodeFormat getBarcodeFormat() {
    return BarcodeFormat.EAN_8;
  }

}
