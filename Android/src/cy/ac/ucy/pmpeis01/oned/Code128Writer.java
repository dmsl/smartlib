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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.EncodeHintType;
import cy.ac.ucy.pmpeis01.WriterException;
import cy.ac.ucy.pmpeis01.common.BitMatrix;

/**
 * This object renders a CODE128 code as a {@link BitMatrix}.
 * 
 * @author erik.barbara@gmail.com (Erik Barbara)
 */
public final class Code128Writer extends UPCEANWriter {

  private static final int CODE_START_B = 104;
  private static final int CODE_START_C = 105;
  private static final int CODE_CODE_B = 100;
  private static final int CODE_CODE_C = 99;
  private static final int CODE_STOP = 106;

  // Dummy characters used to specify control characters in input
  private static final char ESCAPE_FNC_1 = '\u00f1';
  private static final char ESCAPE_FNC_2 = '\u00f2';
  private static final char ESCAPE_FNC_3 = '\u00f3';
  private static final char ESCAPE_FNC_4 = '\u00f4';

  private static final int CODE_FNC_1 = 102;   // Code A, Code B, Code C
  private static final int CODE_FNC_2 = 97;    // Code A, Code B
  private static final int CODE_FNC_3 = 96;    // Code A, Code B
  private static final int CODE_FNC_4_B = 100; // Code B

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.CODE_128) {
      throw new IllegalArgumentException("Can only encode CODE_128, but got " + format);
    }
    return super.encode(contents, format, width, height, hints);
  }

  @Override
  public byte[] encode(String contents) {
    int length = contents.length();
    // Check length
    if (length < 1 || length > 80) {
      throw new IllegalArgumentException(
          "Contents length should be between 1 and 80 characters, but got " + length);
    }
    // Check content
    for (int i = 0; i < length; i++) {
      char c = contents.charAt(i);
      if (c < ' ' || c > '~') {
        switch (c) {
          case ESCAPE_FNC_1:
          case ESCAPE_FNC_2:
          case ESCAPE_FNC_3:
          case ESCAPE_FNC_4:
            break;
          default:
            throw new IllegalArgumentException("Bad character in input: " + c);
        }
      }
    }
    
    Collection<int[]> patterns = new ArrayList<int[]>(); // temporary storage for patterns
    int checkSum = 0;
    int checkWeight = 1;
    int codeSet = 0; // selected code (CODE_CODE_B or CODE_CODE_C)
    int position = 0; // position in contents
    
    while (position < length) {
      //Select code to use
      int requiredDigitCount = codeSet == CODE_CODE_C ? 2 : 4;
      int newCodeSet;
      if (isDigits(contents, position, requiredDigitCount)) {
        newCodeSet = CODE_CODE_C;
      } else {
        newCodeSet = CODE_CODE_B;
      }
      
      //Get the pattern index
      int patternIndex;
      if (newCodeSet == codeSet) {
        // Encode the current character
        if (codeSet == CODE_CODE_B) {
          patternIndex = contents.charAt(position) - ' ';
          position += 1;
        } else { // CODE_CODE_C
          switch (contents.charAt(position)) {
            case ESCAPE_FNC_1:
              patternIndex = CODE_FNC_1;
              position++;
              break;
            case ESCAPE_FNC_2:
              patternIndex = CODE_FNC_2;
              position++;
              break;
            case ESCAPE_FNC_3:
              patternIndex = CODE_FNC_3;
              position++;
              break;
            case ESCAPE_FNC_4:
              patternIndex = CODE_FNC_4_B;
              position++;
              break;
            default:
              patternIndex = Integer.parseInt(contents.substring(position, position + 2));
              position += 2;
              break;
          }
        }
      } else {
        // Should we change the current code?
        // Do we have a code set?
        if (codeSet == 0) {
          // No, we don't have a code set
          if (newCodeSet == CODE_CODE_B) {
            patternIndex = CODE_START_B;
          } else {
            // CODE_CODE_C
            patternIndex = CODE_START_C;
          }
        } else {
          // Yes, we have a code set
          patternIndex = newCodeSet;
        }
        codeSet = newCodeSet;
      }
      
      // Get the pattern
      patterns.add(Code128Reader.CODE_PATTERNS[patternIndex]);
      
      // Compute checksum
      checkSum += patternIndex * checkWeight;
      if (position != 0) {
        checkWeight++;
      }
    }
    
    // Compute and append checksum
    checkSum %= 103;
    patterns.add(Code128Reader.CODE_PATTERNS[checkSum]);
    
    // Append stop code
    patterns.add(Code128Reader.CODE_PATTERNS[CODE_STOP]);
    
    // Compute code width
    int codeWidth = 0;
    for (int[] pattern : patterns) {
      for (int width : pattern) {
        codeWidth += width;
      }
    }
    
    // Compute result
    byte[] result = new byte[codeWidth];
    int pos = 0;
    for (int[] pattern : patterns) {
      pos += appendPattern(result, pos, pattern, 1);
    }
    
    return result;
  }

  private static boolean isDigits(CharSequence value, int start, int length) {
    int end = start + length;
    int last = value.length();
    for (int i = start; i < end && i < last; i++) {
      char c = value.charAt(i);
      if (c < '0' || c > '9') {
        if (c != ESCAPE_FNC_1) {
          return false;
        }
        end++; // ignore FNC_1
      }
    }
    return end <= last; // end > last if we've run out of string
  }

}
