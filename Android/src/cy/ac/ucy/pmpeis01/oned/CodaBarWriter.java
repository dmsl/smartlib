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

import cy.ac.ucy.pmpeis01.common.BitMatrix;

/**
 * This class renders CodaBar as {@link BitMatrix}.
 *
 * @author dsbnatut@gmail.com (Kazuki Nishiura)
 */
public final class CodaBarWriter extends OneDimensionalCodeWriter {

  public CodaBarWriter() {
    // Super constructor requires the sum of the left and right margin length.
    // CodaBar spec requires a side margin to be more than ten times wider than narrow space.
    // In this implementation, narrow space has a unit length, so 20 is required minimum.
    super(20);
  }

  /*
   * @see OneDimensionalCodeWriter#encode(java.lang.String)
   */
  @Override
  public byte[] encode(String contents) {

    // Verify input and calculate decoded length.
    if (!CodaBarReader.arrayContains(
        new char[]{'A', 'B', 'C', 'D'}, Character.toUpperCase(contents.charAt(0)))) {
      throw new IllegalArgumentException(
          "Codabar should start with one of the following: 'A', 'B', 'C' or 'D'");
    }
    if (!CodaBarReader.arrayContains(new char[]{'T', 'N', '*', 'E'},
                                     Character.toUpperCase(contents.charAt(contents.length() - 1)))) {
      throw new IllegalArgumentException(
          "Codabar should end with one of the following: 'T', 'N', '*' or 'E'");
    }
    // The start character and the end character are decoded to 10 length each.
    int resultLength = 20;
    char[] charsWhichAreTenLengthEachAfterDecoded = {'/', ':', '+', '.'};
    for (int i = 1; i < contents.length() - 1; i++) {
      if (Character.isDigit(contents.charAt(i)) || contents.charAt(i) == '-'
          || contents.charAt(i) == '$') {
        resultLength += 9;
      } else if (CodaBarReader.arrayContains(
          charsWhichAreTenLengthEachAfterDecoded, contents.charAt(i))) {
        resultLength += 10;
      } else {
        throw new IllegalArgumentException("Cannot encode : '" + contents.charAt(i) + '\'');
      }
    }
    // A blank is placed between each character.
    resultLength += contents.length() - 1;

    byte[] result = new byte[resultLength];
    int position = 0;
    for (int index = 0; index < contents.length(); index++) {
      char c = Character.toUpperCase(contents.charAt(index));
      if (index == contents.length() - 1) {
        // The end chars are not in the CodaBarReader.ALPHABET.
        switch (c) {
          case 'T':
            c = 'A';
            break;
          case 'N':
            c = 'B';
            break;
          case '*':
            c = 'C';
            break;
          case 'E':
            c = 'D';
            break;
        }
      }
      int code = 0;
      for (int i = 0; i < CodaBarReader.ALPHABET.length; i++) {
        // Found any, because I checked above.
        if (c == CodaBarReader.ALPHABET[i]) {
          code = CodaBarReader.CHARACTER_ENCODINGS[i];
          break;
        }
      }
      byte color = 1;
      int counter = 0;
      int bit = 0;
      while (bit < 7) { // A character consists of 7 digit.
        result[position] = color;
        position++;
        if (((code >> (6 - bit)) & 1) == 0 || counter == 1) {
          color ^= 1; // Flip the color.
          bit++;
          counter = 0;
        } else {
          counter++;
        }
      }
      if (index < contents.length() - 1) {
        result[position] = 0;
        position++;
      }
    }
    return result;
  }
}

