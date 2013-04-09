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

package cy.ac.ucy.paschalis.qrcode.decoder;

/**
 * <p>See ISO 18004:2006, 6.4.1, Tables 2 and 3. This enum encapsulates the various modes in which
 * data can be encoded to bits in the QR code standard.</p>
 *
 * @author Sean Owen
 */
public enum Mode {

  TERMINATOR(new int[]{0, 0, 0}, 0x00), // Not really a mode...
  NUMERIC(new int[]{10, 12, 14}, 0x01),
  ALPHANUMERIC(new int[]{9, 11, 13}, 0x02),
  STRUCTURED_APPEND(new int[]{0, 0, 0}, 0x03), // Not supported
  BYTE(new int[]{8, 16, 16}, 0x04),
  ECI(new int[]{0, 0, 0}, 0x07), // character counts don't apply
  KANJI(new int[]{8, 10, 12}, 0x08),
  FNC1_FIRST_POSITION(new int[]{0, 0, 0}, 0x05),
  FNC1_SECOND_POSITION(new int[]{0, 0, 0}, 0x09),
  /** See GBT 18284-2000; "Hanzi" is a transliteration of this mode name. */
  HANZI(new int[]{8, 10, 12}, 0x0D);

  private final int[] characterCountBitsForVersions;
  private final int bits;

  Mode(int[] characterCountBitsForVersions, int bits) {
    this.characterCountBitsForVersions = characterCountBitsForVersions;
    this.bits = bits;
  }

  /**
   * @param bits four bits encoding a QR Code data mode
   * @return Mode encoded by these bits
   * @throws IllegalArgumentException if bits do not correspond to a known mode
   */
  public static Mode forBits(int bits) {
    switch (bits) {
      case 0x0:
        return TERMINATOR;
      case 0x1:
        return NUMERIC;
      case 0x2:
        return ALPHANUMERIC;
      case 0x3:
        return STRUCTURED_APPEND;
      case 0x4:
        return BYTE;
      case 0x5:
        return FNC1_FIRST_POSITION;
      case 0x7:
        return ECI;
      case 0x8:
        return KANJI;
      case 0x9:
        return FNC1_SECOND_POSITION;
      case 0xD:
        // 0xD is defined in GBT 18284-2000, may not be supported in foreign country
        return HANZI;
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * @param version version in question
   * @return number of bits used, in this QR Code symbol {@link Version}, to encode the
   *         count of characters that will follow encoded in this Mode
   */
  public int getCharacterCountBits(Version version) {
    int number = version.getVersionNumber();
    int offset;
    if (number <= 9) {
      offset = 0;
    } else if (number <= 26) {
      offset = 1;
    } else {
      offset = 2;
    }
    return characterCountBitsForVersions[offset];
  }

  public int getBits() {
    return bits;
  }

}
