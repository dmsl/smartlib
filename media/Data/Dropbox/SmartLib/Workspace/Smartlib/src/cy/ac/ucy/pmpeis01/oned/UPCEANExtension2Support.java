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

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.NotFoundException;
import cy.ac.ucy.pmpeis01.Result;
import cy.ac.ucy.pmpeis01.ResultMetadataType;
import cy.ac.ucy.pmpeis01.ResultPoint;
import cy.ac.ucy.pmpeis01.common.BitArray;

import java.util.EnumMap;
import java.util.Map;

/**
 * @see UPCEANExtension5Support
 */
final class UPCEANExtension2Support {

  private final int[] decodeMiddleCounters = new int[4];
  private final StringBuilder decodeRowStringBuffer = new StringBuilder();

  Result decodeRow(int rowNumber, BitArray row, int[] extensionStartRange) throws NotFoundException {

    StringBuilder result = decodeRowStringBuffer;
    result.setLength(0);
    int end = decodeMiddle(row, extensionStartRange, result);

    String resultString = result.toString();
    Map<ResultMetadataType,Object> extensionData = parseExtensionString(resultString);

    Result extensionResult =
        new Result(resultString,
                   null,
                   new ResultPoint[] {
                       new ResultPoint((extensionStartRange[0] + extensionStartRange[1]) / 2.0f, (float) rowNumber),
                       new ResultPoint((float) end, (float) rowNumber),
                   },
                   BarcodeFormat.UPC_EAN_EXTENSION);
    if (extensionData != null) {
      extensionResult.putAllMetadata(extensionData);
    }
    return extensionResult;
  }

  int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
    int[] counters = decodeMiddleCounters;
    counters[0] = 0;
    counters[1] = 0;
    counters[2] = 0;
    counters[3] = 0;
    int end = row.getSize();
    int rowOffset = startRange[1];

    int checkParity = 0;

    for (int x = 0; x < 2 && rowOffset < end; x++) {
      int bestMatch = UPCEANReader.decodeDigit(row, counters, rowOffset, UPCEANReader.L_AND_G_PATTERNS);
      resultString.append((char) ('0' + bestMatch % 10));
      for (int counter : counters) {
        rowOffset += counter;
      }
      if (bestMatch >= 10) {
        checkParity |= 1 << (1 - x);
      }
      if (x != 1) {
        // Read off separator if not last
        rowOffset = row.getNextSet(rowOffset);
        rowOffset = row.getNextUnset(rowOffset);
      }
    }

    if (resultString.length() != 2) {
      throw NotFoundException.getNotFoundInstance();
    }

    if (Integer.parseInt(resultString.toString()) % 4 != checkParity) {
      throw NotFoundException.getNotFoundInstance();
    }
    
    return rowOffset;
  }

  /**
   * @param raw raw content of extension
   * @return formatted interpretation of raw content as a {@link Map} mapping
   *  one {@link ResultMetadataType} to appropriate value, or {@code null} if not known
   */
  private static Map<ResultMetadataType,Object> parseExtensionString(String raw) {
    if (raw.length() != 2) {
      return null;
    }
    Map<ResultMetadataType,Object> result = new EnumMap<ResultMetadataType,Object>(ResultMetadataType.class);
    result.put(ResultMetadataType.ISSUE_NUMBER, Integer.valueOf(raw));
    return result;
  }

}
