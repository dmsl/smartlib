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

package cy.ac.ucy.pmpeis01.qrcode.decoder;

/**
 * <p>See ISO 18004:2006, 6.5.1. This enum encapsulates the four error correction levels
 * defined by the QR code standard.</p>
 *
 * @author Sean Owen
 */
public enum ErrorCorrectionLevel {

  /** L = ~7% correction */
  L(0x01),
  /** M = ~15% correction */
  M(0x00),
  /** Q = ~25% correction */
  Q(0x03),
  /** H = ~30% correction */
  H(0x02);

  private static final ErrorCorrectionLevel[] FOR_BITS = {M, L, H, Q};

  private final int bits;

  ErrorCorrectionLevel(int bits) {
    this.bits = bits;
  }

  public int getBits() {
    return bits;
  }

  /**
   * @param bits int containing the two bits encoding a QR Code's error correction level
   * @return ErrorCorrectionLevel representing the encoded error correction level
   */
  public static ErrorCorrectionLevel forBits(int bits) {
    if (bits < 0 || bits >= FOR_BITS.length) {
      throw new IllegalArgumentException();
    }
    return FOR_BITS[bits];
  }


}
